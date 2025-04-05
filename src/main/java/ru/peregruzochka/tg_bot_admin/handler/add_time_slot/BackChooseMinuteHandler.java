package ru.peregruzochka.tg_bot_admin.handler.add_time_slot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.TeacherDtoCache;
import ru.peregruzochka.tg_bot_admin.cache.TimeSlotSaver;
import ru.peregruzochka.tg_bot_admin.dto.TimeSlotDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BackChooseMinuteHandler implements UpdateHandler {


    private final ChooseMinuteAttribute chooseMinuteAttribute;
    private final TimeSlotSaver timeSlotSaver;
    private final TeacherDtoCache teacherDtoCache;
    private final TelegramBot telegramBot;

    @Override
    public boolean isApplicable(Update update) {
        return hasCallback(update, "/back-choose-minute");
    }

    @Override
    public void compute(Update update) {
        TimeSlotDto timeSlotDto = timeSlotSaver.getTimeSlotDto();
        UUID teacherId = timeSlotDto.getTeacherId();
        String teacherName = teacherDtoCache.get(teacherId).getName();
        LocalDateTime startTime = timeSlotDto.getStartTime();
        int hour = startTime.getHour();
        startTime = startTime.toLocalDate().atStartOfDay().plusHours(hour);
        timeSlotDto.setStartTime(startTime);

        String text = chooseMinuteAttribute.generateText(teacherName, startTime.toLocalDate().toString());
        InlineKeyboardMarkup markup = chooseMinuteAttribute.generateChooseMinuteMarkup(startTime.getHour());

        telegramBot.edit(text, markup, update);
    }
}
