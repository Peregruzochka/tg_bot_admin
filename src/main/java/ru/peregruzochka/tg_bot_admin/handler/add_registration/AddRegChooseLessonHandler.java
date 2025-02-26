package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.InputPhoneFlag;
import ru.peregruzochka.tg_bot_admin.cache.RegistrationDtoSaver;
import ru.peregruzochka.tg_bot_admin.client.BotBackendClient;
import ru.peregruzochka.tg_bot_admin.dto.LessonDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AddRegChooseLessonHandler implements UpdateHandler {

    private final BotBackendClient botBackendClient;
    private final TelegramBot telegramBot;
    private final AddRegChooseLessonAttribute addRegChooseLessonAttribute;
    private final RegistrationDtoSaver registrationDtoSaver;
    private final InputPhoneFlag inputPhoneFlag;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("/add-reg-choose-lesson");
    }

    @Override
    public void compute(Update update) {
        inputPhoneFlag.setFlag(false);
        List<LessonDto> lessons = botBackendClient.getAllLessons();

        telegramBot.edit(
                addRegChooseLessonAttribute.generateText(registrationDtoSaver.getRegistrationDto()),
                addRegChooseLessonAttribute.generateLessonMarkup(lessons),
                update
        );
    }
}
