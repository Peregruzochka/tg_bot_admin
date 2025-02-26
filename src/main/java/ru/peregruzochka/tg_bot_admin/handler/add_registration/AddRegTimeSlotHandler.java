package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.LessonDtoCache;
import ru.peregruzochka.tg_bot_admin.cache.RegistrationDtoSaver;
import ru.peregruzochka.tg_bot_admin.client.BotBackendClient;
import ru.peregruzochka.tg_bot_admin.dto.LessonDto;
import ru.peregruzochka.tg_bot_admin.dto.TimeSlotDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AddRegTimeSlotHandler implements UpdateHandler {

    private final BotBackendClient botBackendClient;
    private final RegistrationDtoSaver registrationDtoSaver;
    private final LessonDtoCache lessonDtoCache;
    private final TelegramBot telegramBot;
    private final AddRegChooseLessonAttribute addRegChooseLessonAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("/add-reg-timeslot:");
    }

    @Override
    public void compute(Update update) {
        UUID timeSlotID = UUID.fromString(update.getCallbackQuery().getData().replace("/add-reg-timeslot:", ""));
        TimeSlotDto timeSlotDto = botBackendClient.getTimeSlot(timeSlotID);
        registrationDtoSaver.getRegistrationDto().setSlot(timeSlotDto);

        List<LessonDto> lessons = botBackendClient.getAllLessons();
        lessons.forEach(lesson -> lessonDtoCache.put(lesson.getId(), lesson));

        telegramBot.edit(
                addRegChooseLessonAttribute.generateText(registrationDtoSaver.getRegistrationDto()),
                addRegChooseLessonAttribute.generateLessonMarkup(lessons),
                update
        );
    }
}
