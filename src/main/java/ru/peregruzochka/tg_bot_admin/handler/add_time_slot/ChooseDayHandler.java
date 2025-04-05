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
public class ChooseDayHandler implements UpdateHandler {
    private final TelegramBot bot;
    private final TimeSlotSaver timeSlotSaver;
    private final ChooseHourAttribute chooseHourAttribute;
    private final TeacherDtoCache teacherDtoCache;
    private final BotBackendClient botBackendClient;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("/day:");
    }

    @Override
    public void compute(Update update) {
        String day = update.getCallbackQuery().getData().replace("/day:", "");
        LocalDate localDate = LocalDate.parse(day);
        LocalDateTime localDateTime = localDate.atStartOfDay();
        timeSlotSaver.getTimeSlotDto().setStartTime(localDateTime);

        UUID teacherId = timeSlotSaver.getTimeSlotDto().getTeacherId();
        String teacherName = teacherDtoCache.get(teacherId).getName();
        List<TimeSlotDto> timeSlots = botBackendClient.getTeacherTimeSlotsByDate(teacherId, localDate);
        List<GroupTimeSlotDto> groupTimeSlots = botBackendClient.getTeacherGroupTimeSlotsByDate(teacherId, localDate);

        bot.edit(
                chooseHourAttribute.generateText(teacherName, localDate.toString(), timeSlots, groupTimeSlots),
                chooseHourAttribute.generateChooseHourMarkup(),
                update
        );
    }
}
