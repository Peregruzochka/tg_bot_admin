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
public class TeacherRmHandler implements UpdateHandler {

    private final TelegramBot bot;
    private final BotBackendClient botBackendClient;
    private final ChooseDateRmTimeSlotAttribute chooseDateRmTimeSlotAttribute;
    private final TeacherDtoCache teacherDtoCache;
    private final TeacherTimeSlotPool teacherTimeSlotPool;
    private final TeacherSaver teacherSaver;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("/teacher-rm:");
    }

    @Override
    public void compute(Update update) {
        UUID teacherId = UUID.fromString(update.getCallbackQuery().getData().replace("/teacher-rm:", ""));
        String teacherName = teacherDtoCache.get(teacherId).getName();
        List<TimeSlotDto> timeSlotDtoList = botBackendClient.getTeacherTimeSlotsInNextMonth(teacherId);
        teacherTimeSlotPool.put(teacherId, timeSlotDtoList);
        teacherSaver.setTeacherId(teacherId);

        bot.edit(
                chooseDateRmTimeSlotAttribute.generateText(teacherName),
                chooseDateRmTimeSlotAttribute.generateChooseDateRmTimeSlot(timeSlotDtoList),
                update
        );
    }
}
