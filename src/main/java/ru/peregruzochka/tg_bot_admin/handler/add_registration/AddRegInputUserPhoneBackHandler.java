package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.InputPhoneFlag;
import ru.peregruzochka.tg_bot_admin.cache.RegistrationDtoSaver;
import ru.peregruzochka.tg_bot_admin.dto.RegistrationDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

@Component
@RequiredArgsConstructor
public class AddRegInputUserPhoneBackHandler implements UpdateHandler {
    private final TelegramBot telegramBot;
    private final AddRegInputUserPhoneAttribute addRegInputUserPhoneAttribute;
    private final RegistrationDtoSaver registrationDtoSaver;
    private final InputPhoneFlag inputPhoneFlag;


    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("/add-reg-input-user-phone-back");
    }

    @Override
    public void compute(Update update) {
        RegistrationDto registrationDto = registrationDtoSaver.getRegistrationDto();

        inputPhoneFlag.setFlag(true);
        telegramBot.edit(
                addRegInputUserPhoneAttribute.generateText(registrationDto),
                addRegInputUserPhoneAttribute.createMarkup(),
                update
        );
    }
}
