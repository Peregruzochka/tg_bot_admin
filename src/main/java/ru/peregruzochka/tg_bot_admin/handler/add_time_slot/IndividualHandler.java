package ru.peregruzochka.tg_bot_admin.handler.add_time_slot;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.TeacherDtoCache;
import ru.peregruzochka.tg_bot_admin.cache.TimeSlotSaver;
import ru.peregruzochka.tg_bot_admin.client.BotBackendClient;
import ru.peregruzochka.tg_bot_admin.dto.GroupTimeSlotDto;
import ru.peregruzochka.tg_bot_admin.dto.TimeSlotDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class IndividualHandler implements UpdateHandler {

    private final TimeSlotSaver timeSlotSaver;
    private final TeacherDtoCache teacherDtoCache;
    private final BotBackendClient botBackendClient;
    private final TelegramBot telegramBot;
    private final ChooseHourAttribute chooseHourAttribute;
    private final ViewTimeSlotAttribute viewTimeSlotAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return hasCallback(update, "/individual");
    }

    @Override
    public void compute(Update update) {
        TimeSlotDto timeSlotDto = timeSlotSaver.getTimeSlotDto();
        UUID teacherId = timeSlotDto.getTeacherId();
        String teacherName = teacherDtoCache.get(teacherId).getName();
        LocalDate localDate = timeSlotDto.getStartTime().toLocalDate();

        try {
            botBackendClient.addTimeSlot(timeSlotDto);
        } catch (FeignException exception) {
            List<TimeSlotDto> timeSlots = botBackendClient.getTeacherTimeSlotsByDate(teacherId, localDate);
            List<GroupTimeSlotDto> groupTimeSlots = botBackendClient.getTeacherGroupTimeSlotsByDate(teacherId, localDate);
            LocalDateTime newStartTime = timeSlotSaver.getTimeSlotDto().getStartTime().toLocalDate().atStartOfDay();
            timeSlotSaver.getTimeSlotDto().setStartTime(newStartTime);
            telegramBot.edit(
                    chooseHourAttribute.generateTextAfterException(teacherName, localDate.toString(), timeSlots, groupTimeSlots),
                    chooseHourAttribute.generateChooseHourMarkup(),
                    update
            );
            return;
        }

        List<TimeSlotDto> timeSlots = botBackendClient.getTeacherTimeSlotsByDate(teacherId, localDate);
        List<GroupTimeSlotDto> groupTimeSlots = botBackendClient.getTeacherGroupTimeSlotsByDate(teacherId, localDate);

        telegramBot.edit(
                viewTimeSlotAttribute.generateText(teacherName, localDate.toString(), timeSlots, groupTimeSlots),
                viewTimeSlotAttribute.createMarkup(),
                update
        );
    }
}
