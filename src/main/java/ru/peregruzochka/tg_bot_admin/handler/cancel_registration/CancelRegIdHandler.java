package ru.peregruzochka.tg_bot_admin.handler.cancel_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.client.BotBackendClient;
import ru.peregruzochka.tg_bot_admin.dto.CancelDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CancelRegIdHandler implements UpdateHandler {

    private final BotBackendClient botBackendClient;
    private final TelegramBot telegramBot;
    private final FinishCancelRegistrationAttribute finishCancelRegistrationAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("/cancel-reg-id:");
    }

    @Override
    public void compute(Update update) {
        UUID registrationId = UUID.fromString(update.getCallbackQuery().getData().replace("/cancel-reg-id:", ""));

        CancelDto cancelDto = CancelDto.builder()
                .registrationId(registrationId)
                .caseDescription("Отмена занятия администратором")
                .build();

        botBackendClient.addCancel(cancelDto);

        telegramBot.edit(
                finishCancelRegistrationAttribute.getText(),
                finishCancelRegistrationAttribute.createMarkup(),
                update
        );
    }
}
