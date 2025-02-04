package ru.peregruzochka.tg_bot_admin.handler.add_time_slot;

import feign.FeignException;
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
    private final BotBackendClient botBackendClient;
    private final ChooseHourAttribute chooseHourAttribute;

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
        UUID teacherId = timeSlotDto.getTeacherId();
        String teacherName = teacherDtoCache.get(teacherId).getName();
        LocalDate localDate = timeSlotDto.getStartTime().toLocalDate();

        try {
            botBackendClient.addTimeSlot(timeSlotDto);
        } catch (FeignException exception) {
            List<TimeSlotDto> timeSlotDtos = botBackendClient.getTeacherTimeSlotsByDate(teacherId, localDate);
            LocalDateTime newStartTime = timeSlotSaver.getTimeSlotDto().getStartTime().toLocalDate().atStartOfDay();
            timeSlotSaver.getTimeSlotDto().setStartTime(newStartTime);
            bot.edit(
                    chooseHourAttribute.generateTextAfterException(teacherName, localDate.toString(), timeSlotDtos),
                    chooseHourAttribute.generateChooseHourMarkup(),
                    update
            );
            return;
        }

        List<TimeSlotDto> teacherTimeSlot = botBackendClient.getTeacherTimeSlotsByDate(teacherId, localDate);

        bot.edit(
                viewTimeSlotAttribute.generateText(teacherName, localDate.toString(), teacherTimeSlot),
                viewTimeSlotAttribute.createMarkup(),
                update
        );
    }
}
