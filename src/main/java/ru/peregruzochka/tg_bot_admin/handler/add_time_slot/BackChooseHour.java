package ru.peregruzochka.tg_bot_admin.handler.add_time_slot;

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
public class BackChooseHour implements UpdateHandler {
    private final TelegramBot bot;
    private final ChooseHourAttribute chooseHourAttribute;
    private final TimeSlotSaver timeSlotSaver;
    private final TeacherDtoCache teacherDtoCache;
    private final BotBackendClient botBackendClient;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("/back-choose-hour");
    }

    @Override
    public void compute(Update update) {
        TimeSlotDto timeSlot = timeSlotSaver.getTimeSlotDto();
        UUID teacherId = timeSlot.getTeacherId();
        String teacherName = teacherDtoCache.get(teacherId).getName();
        LocalDateTime startTime = timeSlot.getStartTime();
        LocalDate localDate = startTime.toLocalDate();
        startTime = startTime.toLocalDate().atStartOfDay();
        timeSlotSaver.getTimeSlotDto().setStartTime(startTime);

        List<TimeSlotDto> timeSlots = botBackendClient.getTeacherTimeSlotsByDate(teacherId, localDate);
        List<GroupTimeSlotDto> groupTimeSlots = botBackendClient.getTeacherGroupTimeSlotsByDate(teacherId, localDate);

        bot.edit(
                chooseHourAttribute.generateText(teacherName, localDate.toString(), timeSlots, groupTimeSlots),
                chooseHourAttribute.generateChooseHourMarkup(),
                update
        );
    }
}
