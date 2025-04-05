package ru.peregruzochka.tg_bot_admin.handler.add_time_slot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.TeacherDtoCache;
import ru.peregruzochka.tg_bot_admin.cache.TimeSlotSaver;
import ru.peregruzochka.tg_bot_admin.dto.TeacherDto;
import ru.peregruzochka.tg_bot_admin.dto.TimeSlotDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChooseTeacherHandler implements UpdateHandler {
    private final TelegramBot bot;
    private final TeacherDtoCache cache;
    private final ChooseDayAttribute chooseDayAttribute;
    private final TimeSlotSaver timeSlotSaver;

    @Override
    public boolean isApplicable(Update update) {
        return callbackStartWith(update, "/teacher:");
    }

    @Override
    public void compute(Update update) {
        UUID teacherId = UUID.fromString(getPayload(update, "/teacher:"));
        TimeSlotDto timeSlotDto = new TimeSlotDto();
        timeSlotDto.setTeacherId(teacherId);
        timeSlotSaver.setTimeSlotDto(timeSlotDto);

        TeacherDto teacherDto = cache.get(teacherId);

        bot.edit(
                chooseDayAttribute.generateText(teacherDto.getName()),
                chooseDayAttribute.generateChooseDayMarkup(0),
                update
        );
    }
}
