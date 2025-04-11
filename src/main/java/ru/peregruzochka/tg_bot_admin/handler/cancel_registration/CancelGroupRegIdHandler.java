package ru.peregruzochka.tg_bot_admin.handler.cancel_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.client.BotBackendClient;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CancelGroupRegIdHandler implements UpdateHandler {

    private final BotBackendClient botBackendClient;
    private final TelegramBot telegramBot;
    private final FinishCancelRegistrationAttribute finishCancelRegistrationAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return callbackStartWith(update, "/cancel-group-reg-id:");
    }

    @Override
    public void compute(Update update) {
        UUID registrationId = UUID.fromString(getPayload(update, "/cancel-group-reg-id:"));

        botBackendClient.addGroupCancel(registrationId, "Отмена занятия администратором");

        telegramBot.edit(
                finishCancelRegistrationAttribute.getText(),
                finishCancelRegistrationAttribute.createMarkup(),
                update
        );
    }
}
