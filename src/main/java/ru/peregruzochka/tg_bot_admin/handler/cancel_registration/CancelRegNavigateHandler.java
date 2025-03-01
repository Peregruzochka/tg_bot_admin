package ru.peregruzochka.tg_bot_admin.handler.cancel_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

@Component
@RequiredArgsConstructor
public class CancelRegNavigateHandler implements UpdateHandler {
    private final TelegramBot telegramBot;
    private final CancelRegChooseDayAttribute cancelRegChooseDayAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("/cancel-reg-navigate:");
    }

    @Override
    public void compute(Update update) {
        int offset = Integer.parseInt(update.getCallbackQuery().getData().replace("/cancel-reg-navigate:", ""));
        telegramBot.edit(
                cancelRegChooseDayAttribute.getText(),
                cancelRegChooseDayAttribute.generateChooseDayMarkup(offset),
                update
        );
    }
}
