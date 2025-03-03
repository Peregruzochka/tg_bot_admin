package ru.peregruzochka.tg_bot_admin.handler.add_time_slot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.TeacherDtoCache;
import ru.peregruzochka.tg_bot_admin.cache.TimeSlotSaver;
import ru.peregruzochka.tg_bot_admin.client.BotBackendClient;
import ru.peregruzochka.tg_bot_admin.dto.TimeSlotDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.time.LocalDate;
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
        UUID teacherId = timeSlotSaver.getTimeSlotDto().getTeacherId();
        String teacherName = teacherDtoCache.get(teacherId).getName();
        LocalDate localDate = timeSlotSaver.getTimeSlotDto().getStartTime().toLocalDate();
        List<TimeSlotDto> timeSlots = botBackendClient.getTeacherTimeSlotsByDate(teacherId, localDate);

        bot.edit(
                chooseHourAttribute.generateText(teacherName, localDate.toString(), timeSlots),
                chooseHourAttribute.generateChooseHourMarkup(),
                update
        );
    }
}
