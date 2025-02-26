package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.InputPhoneFlag;
import ru.peregruzochka.tg_bot_admin.cache.LessonDtoCache;
import ru.peregruzochka.tg_bot_admin.cache.RegistrationDtoSaver;
import ru.peregruzochka.tg_bot_admin.dto.LessonDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AddRegLessonHandler implements UpdateHandler {
    private final LessonDtoCache lessonDtoCache;
    private final RegistrationDtoSaver registrationDtoSaver;
    private final TelegramBot telegramBot;
    private final AddRegInputUserPhoneAttribute addRegInputUserPhoneAttribute;
    private final InputPhoneFlag inputPhoneFlag;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("/add-reg-lesson:");
    }

    @Override
    public void compute(Update update) {
        UUID lessonId = UUID.fromString(update.getCallbackQuery().getData().replace("/add-reg-lesson:", ""));
        LessonDto lessonDto = lessonDtoCache.get(lessonId);
        registrationDtoSaver.getRegistrationDto().setLesson(lessonDto);
        inputPhoneFlag.setFlag(true);

        telegramBot.edit(
                addRegInputUserPhoneAttribute.generateText(registrationDtoSaver.getRegistrationDto()),
                addRegInputUserPhoneAttribute.createMarkup(),
                update
        );
    }
}
