package ru.peregruzochka.tg_bot_admin.handler.add_time_slot;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.TeacherDtoCache;
import ru.peregruzochka.tg_bot_admin.cache.TimeSlotSaver;
import ru.peregruzochka.tg_bot_admin.client.BotBackendClient;
import ru.peregruzochka.tg_bot_admin.dto.GroupTimeSlotDto;
import ru.peregruzochka.tg_bot_admin.dto.TeacherDto;
import ru.peregruzochka.tg_bot_admin.dto.TimeSlotDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChooseMinuteHandler implements UpdateHandler {
    private final TelegramBot bot;
    private final TimeSlotSaver timeSlotSaver;
    private final BotBackendClient botBackendClient;
    private final ChooseTimeSlotTypeAttribute chooseTimeSlotTypeAttribute;
    private final ChooseHourAttribute chooseHourAttribute;
    private final TeacherDtoCache teacherDtoCache;
    private final ViewTimeSlotAttribute viewTimeSlotAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return callbackStartWith(update, "/minute");
    }

    @Override
    public void compute(Update update) {
        long minute = Long.parseLong(getPayload(update, "/minute:"));
        LocalDateTime startTime = timeSlotSaver.getTimeSlotDto().getStartTime();
        int hour = startTime.getHour();
        startTime = startTime.toLocalDate().atStartOfDay().plusHours(hour);
        startTime = startTime.plusMinutes(minute);
        timeSlotSaver.getTimeSlotDto().setStartTime(startTime);

        UUID teacherId = timeSlotSaver.getTimeSlotDto().getTeacherId();
        List<TeacherDto> groupTeachers = botBackendClient.getGroupTeacher();
        List<UUID> groupTeachersIds = groupTeachers.stream()
                .map(TeacherDto::getId)
                .toList();
        if (groupTeachersIds.contains(teacherId)) {
            String text = chooseTimeSlotTypeAttribute.getText();
            InlineKeyboardMarkup markup = chooseTimeSlotTypeAttribute.createMarkup();
            bot.edit(text, markup, update);
            return;
        }

        TimeSlotDto timeSlotDto = timeSlotSaver.getTimeSlotDto();
        String teacherName = teacherDtoCache.get(teacherId).getName();
        LocalDate localDate = timeSlotDto.getStartTime().toLocalDate();

        try {
            botBackendClient.addTimeSlot(timeSlotDto);
        } catch (FeignException exception) {
            List<TimeSlotDto> timeSlots = botBackendClient.getTeacherTimeSlotsByDate(teacherId, localDate);
            List<GroupTimeSlotDto> groupTimeSlots = botBackendClient.getTeacherGroupTimeSlotsByDate(teacherId, localDate);
            bot.edit(
                    chooseHourAttribute.generateTextAfterException(teacherName, localDate.toString(), timeSlots, groupTimeSlots),
                    chooseHourAttribute.generateChooseHourMarkup(),
                    update
            );
            return;
        }

        List<TimeSlotDto> timeSlots = botBackendClient.getTeacherTimeSlotsByDate(teacherId, localDate);
        List<GroupTimeSlotDto> groupTimeSlots = botBackendClient.getTeacherGroupTimeSlotsByDate(teacherId, localDate);

        bot.edit(
                viewTimeSlotAttribute.generateText(teacherName, localDate.toString(), timeSlots, groupTimeSlots),
                viewTimeSlotAttribute.createMarkup(),
                update
        );
    }
}
