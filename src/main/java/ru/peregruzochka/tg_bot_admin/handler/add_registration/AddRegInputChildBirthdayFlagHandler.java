package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.InputChildBirthdayFlag;
import ru.peregruzochka.tg_bot_admin.cache.RegistrationDtoSaver;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

@Component
@RequiredArgsConstructor
public class AddRegInputChildBirthdayFlagHandler implements UpdateHandler {
    private final InputChildBirthdayFlag inputChildBirthdayFlag;
    private final RegistrationDtoSaver registrationDtoSaver;
    private final TelegramBot telegramBot;
    private final AddRegFinishAttribute addRegFinishAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasMessage() && inputChildBirthdayFlag.isTrue();
    }

    @Override
    public void compute(Update update) {
        inputChildBirthdayFlag.setFlag(false);

        String childBirthday = update.getMessage().getText();
        registrationDtoSaver.getRegistrationDto().getChild().setBirthday(childBirthday);

        telegramBot.send(
                addRegFinishAttribute.generateText(registrationDtoSaver.getRegistrationDto()),
                addRegFinishAttribute.createMarkup(),
                update
        );
    }
}
