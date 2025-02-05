package ru.peregruzochka.tg_bot_admin.handler.rm_time_slot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.LocalDateSaver;
import ru.peregruzochka.tg_bot_admin.cache.TeacherDtoCache;
import ru.peregruzochka.tg_bot_admin.cache.TeacherSaver;
import ru.peregruzochka.tg_bot_admin.cache.TeacherTimeSlotPool;
import ru.peregruzochka.tg_bot_admin.client.BotBackendClient;
import ru.peregruzochka.tg_bot_admin.dto.TimeSlotDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TimeSlotRmHandler implements UpdateHandler {
    private final TelegramBot bot;
    private final BotBackendClient botBackendClient;
    private final TeacherTimeSlotPool teacherTimeSlotPool;
    private final TeacherSaver teacherSaver;
    private final LocalDateSaver localDateSaver;
    private final ChooseRmTimeSlotAttribute chooseRmTimeSlotAttribute;
    private final TeacherDtoCache teacherDtoCache;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("/time-slot-rm:");
    }

    @Override
    public void compute(Update update) {
        UUID timeSlotId = UUID.fromString(update.getCallbackQuery().getData().replace("/time-slot-rm:", ""));
        botBackendClient.deleteTimeSlot(timeSlotId);
        UUID teacherId = teacherSaver.getTeacherId();
        String teacherName = teacherDtoCache.get(teacherId).getName();
        teacherTimeSlotPool.get(teacherId).removeIf(timeSlot -> timeSlot.getId().equals(timeSlotId));

        List<TimeSlotDto> timeSlotsByDate = teacherTimeSlotPool.get(teacherId).stream()
                .filter(slot -> slot.getStartTime().toLocalDate().equals(localDateSaver.getDate()))
                .toList();

        bot.edit(
                chooseRmTimeSlotAttribute.generateText(teacherName, localDateSaver.getDate().toString()),
                chooseRmTimeSlotAttribute.generateChooseRmTimeSlotMarkup(timeSlotsByDate),
                update
        );
    }
}
