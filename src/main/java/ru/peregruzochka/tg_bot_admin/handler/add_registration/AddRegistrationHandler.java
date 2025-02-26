package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.RegistrationDtoSaver;
import ru.peregruzochka.tg_bot_admin.dto.RegistrationDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

@Component
@RequiredArgsConstructor
public class AddRegistrationHandler implements UpdateHandler {


    private final AddRegChooseDayAttribute addRegChooseDayAttribute;
    private final TelegramBot telegramBot;
    private final RegistrationDtoSaver registrationDtoSaver;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("/add-registration");
    }

    @Override
    public void compute(Update update) {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDtoSaver.setRegistrationDto(registrationDto);

        telegramBot.edit(
                addRegChooseDayAttribute.getText(),
                addRegChooseDayAttribute.generateChooseDayMarkup(0),
                update
        );
    }
}
