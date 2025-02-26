package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.InputChildNameFlag;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

@Component
@RequiredArgsConstructor
public class AddRegInputChildNameHandler implements UpdateHandler {
    private final InputChildNameFlag inputChildNameFlag;
    private final TelegramBot telegramBot;
    private final AddRegInputChildNameAttribute addRegInputChildNameAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("/input-child-name");
    }

    @Override
    public void compute(Update update) {
        inputChildNameFlag.setFlag(true);
        telegramBot.edit(
                addRegInputChildNameAttribute.getText(),
                addRegInputChildNameAttribute.createMarkup(),
                update
        );
    }
}
