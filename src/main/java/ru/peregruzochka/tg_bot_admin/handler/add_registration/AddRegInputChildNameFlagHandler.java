package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.InputChildNameFlag;
import ru.peregruzochka.tg_bot_admin.cache.RegistrationDtoSaver;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

@Controller
@RequiredArgsConstructor
public class AddRegInputChildNameFlagHandler implements UpdateHandler {
    private final InputChildNameFlag inputChildNameFlag;
    private final RegistrationDtoSaver registrationDtoSaver;
    private final TelegramBot telegramBot;
    private final AddRegFinishAttribute addRegFinishAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasMessage() && inputChildNameFlag.isTrue();
    }

    @Override
    public void compute(Update update) {
        inputChildNameFlag.setFlag(false);
        String childName = update.getMessage().getText();

        registrationDtoSaver.getRegistrationDto().getChild().setName(childName);

        telegramBot.send(
                addRegFinishAttribute.generateText(registrationDtoSaver.getRegistrationDto()),
                addRegFinishAttribute.createMarkup(),
                update
        );
    }
}
