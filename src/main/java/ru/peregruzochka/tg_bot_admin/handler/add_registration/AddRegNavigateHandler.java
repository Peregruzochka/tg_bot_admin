package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

@Component
@RequiredArgsConstructor
public class AddRegNavigateHandler implements UpdateHandler {
    private final TelegramBot telegramBot;
    private final AddRegChooseDayAttribute addRegChooseDayAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("/add-reg-navigate:");
    }

    @Override
    public void compute(Update update) {
        int offset = Integer.parseInt(update.getCallbackQuery().getData().replace("/add-reg-navigate:", ""));
        telegramBot.edit(
                addRegChooseDayAttribute.getText(),
                addRegChooseDayAttribute.generateChooseDayMarkup(offset),
                update
        );
    }
}
