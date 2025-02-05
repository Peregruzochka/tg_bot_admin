package ru.peregruzochka.tg_bot_admin.handler.rm_time_slot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
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
public class ChooseDateRmHandler implements UpdateHandler {

    private final TelegramBot bot;
    private final TeacherSaver teacherSaver;
    private final TeacherDtoCache teacherDtoCache;
    private final ChooseDateRmTimeSlotAttribute chooseDateRmTimeSlotAttribute;
    private final BotBackendClient botBackendClient;
    private final TeacherTimeSlotPool teacherTimeSlotPool;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("/choose-date-rm");
    }

    @Override
    public void compute(Update update) {
        UUID teacherId = teacherSaver.getTeacherId();
        String teacherName = teacherDtoCache.get(teacherId).getName();
        List<TimeSlotDto> timeSlotDtoList = botBackendClient.getTeacherTimeSlotsInNextMonth(teacherId);
        teacherTimeSlotPool.put(teacherId, timeSlotDtoList);

        bot.edit(
                chooseDateRmTimeSlotAttribute.generateText(teacherName),
                chooseDateRmTimeSlotAttribute.generateChooseDateRmTimeSlot(timeSlotDtoList),
                update
        );
    }
}
