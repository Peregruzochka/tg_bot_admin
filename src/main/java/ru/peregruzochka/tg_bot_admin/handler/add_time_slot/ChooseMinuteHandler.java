package ru.peregruzochka.tg_bot_admin.handler.add_time_slot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.TeacherDtoCache;
import ru.peregruzochka.tg_bot_admin.cache.TimeSlotSaver;
import ru.peregruzochka.tg_bot_admin.dto.TimeSlotDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChooseMinuteHandler implements UpdateHandler {
    private final TelegramBot bot;
    private final TimeSlotSaver timeSlotSaver;
    private final TeacherDtoCache teacherDtoCache;
    private final ViewTimeSlotAttribute viewTimeSlotAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("/minute");
    }

    @Override
    public void compute(Update update) {
        long minute = Long.parseLong(update.getCallbackQuery().getData().replace("/minute:", ""));
        LocalDateTime startTime = timeSlotSaver.getTimeSlotDto().getStartTime();
        startTime = startTime.plusMinutes(minute);
        timeSlotSaver.getTimeSlotDto().setStartTime(startTime);

        TimeSlotDto timeSlotDto = timeSlotSaver.getTimeSlotDto();

        //todo: TimeSlotDto addTimeSlot(TimeSlotDto timeSlotDto)
        //todo: List<TimeSlotDto> getTeacherTimeSlotByDate(UUID teacherId, LocalDate localDate)
        timeSlotDto.setEndTime(timeSlotDto.getStartTime().plusMinutes(45));
        List<TimeSlotDto> teacherTimeSlot = List.of(timeSlotDto);

        UUID teacherId = timeSlotDto.getTeacherId();
        String teacherName = teacherDtoCache.get(teacherId).getName();
        String localDate = timeSlotDto.getStartTime().toLocalDate().toString();

        bot.edit(
                viewTimeSlotAttribute.generateText(teacherName, localDate, teacherTimeSlot),
                viewTimeSlotAttribute.createMarkup(),
                update
        );
    }
}
