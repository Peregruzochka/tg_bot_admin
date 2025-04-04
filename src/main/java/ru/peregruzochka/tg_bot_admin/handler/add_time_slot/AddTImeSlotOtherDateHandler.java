package ru.peregruzochka.tg_bot_admin.handler.add_time_slot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.TeacherDtoCache;
import ru.peregruzochka.tg_bot_admin.cache.TimeSlotSaver;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AddTImeSlotOtherDateHandler implements UpdateHandler {
    private final TelegramBot bot;
    private final TimeSlotSaver timeSlotSaver;
    private final TeacherDtoCache teacherDtoCache;
    private final ChooseDayAttribute chooseDayAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("/add-time-slot-other-date");
    }

    @Override
    public void compute(Update update) {
        UUID teacherId = timeSlotSaver.getTimeSlotDto().getTeacherId();
        String teacherName = teacherDtoCache.get(teacherId).getName();

        bot.edit(
                chooseDayAttribute.generateText(teacherName),
                chooseDayAttribute.generateChooseDayMarkup(0),
                update
        );
    }
}
