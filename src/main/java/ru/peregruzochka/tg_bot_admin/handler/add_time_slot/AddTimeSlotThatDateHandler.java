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
public class AddTimeSlotThatDateHandler implements UpdateHandler {
    private final TelegramBot bot;
    private final TimeSlotSaver timeSlotSaver;
    private final ChooseHourAttribute chooseHourAttribute;
    private final TeacherDtoCache teacherDtoCache;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("/add-time-slot-that-date");
    }

    @Override
    public void compute(Update update) {
        LocalDateTime date = timeSlotSaver.getTimeSlotDto().getStartTime().toLocalDate().atStartOfDay();
        timeSlotSaver.getTimeSlotDto().setStartTime(date);
        UUID teacherId = timeSlotSaver.getTimeSlotDto().getTeacherId();
        String teacherName = teacherDtoCache.get(teacherId).getName();

        bot.send(
                chooseHourAttribute.generateText(teacherName, date.toLocalDate().toString()),
                chooseHourAttribute.generateChooseHourMarkup(),
                update
        );
    }
}
