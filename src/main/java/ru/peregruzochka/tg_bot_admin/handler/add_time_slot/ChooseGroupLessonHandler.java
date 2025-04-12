package ru.peregruzochka.tg_bot_admin.handler.add_time_slot;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.TeacherDtoCache;
import ru.peregruzochka.tg_bot_admin.cache.TimeSlotSaver;
import ru.peregruzochka.tg_bot_admin.client.BotBackendClient;
import ru.peregruzochka.tg_bot_admin.dto.GroupTimeSlotDto;
import ru.peregruzochka.tg_bot_admin.dto.TimeSlotDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChooseGroupLessonHandler implements UpdateHandler {

    private final TimeSlotSaver timeSlotSaver;
    private final BotBackendClient botBackendClient;
    private final ChooseHourAttribute chooseHourAttribute;
    private final TeacherDtoCache teacherDtoCache;
    private final TelegramBot telegramBot;
    private final ViewTimeSlotAttribute viewTimeSlotAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return callbackStartWith(update, "/choose-group-lesson:");
    }

    @Override
    public void compute(Update update) {
        UUID lessonId = UUID.fromString(getPayload(update, "/choose-group-lesson:"));
        TimeSlotDto slotDto = timeSlotSaver.getTimeSlotDto();
        UUID teacherId = slotDto.getTeacherId();
        LocalDateTime startTime = slotDto.getStartTime();
        String teacherName = teacherDtoCache.get(teacherId).getName();

        try {
            botBackendClient.addGroupTimeSlot(teacherId, lessonId, startTime);
        } catch (FeignException.InternalServerError e) {
            int httpCode = e.status();
            String responceBody = e.contentUTF8();
            if (httpCode == 500 && responceBody.contains("Overlapping times slots")) {
                log.error("{}", e.status());
                List<TimeSlotDto> timeslots = botBackendClient.getTeacherTimeSlotsByDate(teacherId, startTime.toLocalDate());
                List<GroupTimeSlotDto> groupTimeSlots = botBackendClient.getTeacherGroupTimeSlotsByDate(teacherId, startTime.toLocalDate());

                String text = chooseHourAttribute.generateTextAfterException(
                        teacherName,
                        startTime.toLocalDate().toString(),
                        timeslots,
                        groupTimeSlots
                );
                InlineKeyboardMarkup inlineKeyboardMarkup = chooseHourAttribute.generateChooseHourMarkup();
                telegramBot.edit(text, inlineKeyboardMarkup, update);
                return;
            }
        }

        List<TimeSlotDto> timeslots = botBackendClient.getTeacherTimeSlotsByDate(teacherId, startTime.toLocalDate());
        List<GroupTimeSlotDto> groupTimeSlots = botBackendClient.getTeacherGroupTimeSlotsByDate(teacherId, startTime.toLocalDate());

        String text = viewTimeSlotAttribute.generateText(
                teacherName,
                startTime.toLocalDate().toString(),
                timeslots,
                groupTimeSlots
        );

        InlineKeyboardMarkup markup = viewTimeSlotAttribute.createMarkup();
        telegramBot.edit(text, markup, update);
    }
}
