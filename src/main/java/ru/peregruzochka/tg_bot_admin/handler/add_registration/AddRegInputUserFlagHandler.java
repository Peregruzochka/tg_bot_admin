package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.InputUserNameFlag;
import ru.peregruzochka.tg_bot_admin.cache.RegistrationDtoSaver;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

@Component
@RequiredArgsConstructor
public class AddRegInputUserFlagHandler implements UpdateHandler {
    private final InputUserNameFlag inputUserNameFlag;
    private final RegistrationDtoSaver registrationDtoSaver;
    private final TelegramBot telegramBot;
    private final AddRegFinishAttribute addRegFinishAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasMessage() && inputUserNameFlag.isTrue();
    }

    @Override
    public void compute(Update update) {
        inputUserNameFlag.setFlag(false);
        String userName = update.getMessage().getText();

        registrationDtoSaver.getRegistrationDto().getUser().setName(userName);

        telegramBot.send(
                addRegFinishAttribute.generateText(registrationDtoSaver.getRegistrationDto()),
                addRegFinishAttribute.createMarkup(),
                update
        );
    }
}
