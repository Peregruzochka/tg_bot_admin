package ru.peregruzochka.tg_bot_admin.handler.add_time_slot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.TeacherDtoCache;
import ru.peregruzochka.tg_bot_admin.cache.TimeSlotSaver;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChooseHourHandler implements UpdateHandler {

    private final TimeSlotSaver timeSlotSaver;
    private final TelegramBot bot;
    private final ChooseMinuteAttribute chooseMinuteAttribute;
    private final TeacherDtoCache teacherDtoCache;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("/hour:");
    }

    @Override
    public void compute(Update update) {
        int hour = Integer.parseInt(update.getCallbackQuery().getData().replace("/hour:", ""));
        LocalDateTime startTime = timeSlotSaver.getTimeSlotDto().getStartTime().toLocalDate().atStartOfDay();
        startTime = startTime.plusHours(hour);
        timeSlotSaver.getTimeSlotDto().setStartTime(startTime);

        UUID teacherId = timeSlotSaver.getTimeSlotDto().getTeacherId();
        String teacherName = teacherDtoCache.get(teacherId).getName();
        String localDate = timeSlotSaver.getTimeSlotDto().getStartTime().toLocalDate().toString();

        bot.edit(
                chooseMinuteAttribute.generateText(teacherName, localDate),
                chooseMinuteAttribute.generateChooseMinuteMarkup(hour),
                update
        );
    }
}
