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
public class NavigateHandler implements UpdateHandler {
    private final TelegramBot bot;
    private final ChooseDayAttribute chooseDayAttribute;
    private final TimeSlotSaver timeSlotSaver;
    private final TeacherDtoCache teacherDtoCache;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("/navigate:");
    }

    @Override
    public void compute(Update update) {
        int offset = Integer.parseInt(update.getCallbackQuery().getData().replace("/navigate:", ""));
        UUID teacherId = timeSlotSaver.getTimeSlotDto().getTeacherId();
        String teacherName = teacherDtoCache.get(teacherId).getName();

        bot.edit(
                chooseDayAttribute.generateText(teacherName),
                chooseDayAttribute.generateChooseDayMarkup(offset),
                update
        );
    }
}
