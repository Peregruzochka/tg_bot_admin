package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.RegistrationDtoSaver;
import ru.peregruzochka.tg_bot_admin.client.BotBackendClient;
import ru.peregruzochka.tg_bot_admin.dto.RegistrationDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;


@Component
@RequiredArgsConstructor
public class AddRegSendToBackendHandler implements UpdateHandler {
    private final RegistrationDtoSaver registrationDtoSaver;
    private final BotBackendClient botBackendClient;
    private final TelegramBot telegramBot;
    private final AddRegSendToBackendAttribute addRegSendToBackendAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("/send-to-backend");
    }

    @Override
    public void compute(Update update) {
        RegistrationDto newRegistration = registrationDtoSaver.getRegistrationDto();
        botBackendClient.addRegistration(newRegistration);

        telegramBot.edit(
                addRegSendToBackendAttribute.getText(),
                addRegSendToBackendAttribute.createMarkup(),
                update
        );
    }
}
