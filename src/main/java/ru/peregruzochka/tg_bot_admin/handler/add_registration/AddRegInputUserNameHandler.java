package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.InputUserNameFlag;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

@Component
@RequiredArgsConstructor
public class AddRegInputUserNameHandler implements UpdateHandler {

    private final InputUserNameFlag inputUserNameFlag;
    private final TelegramBot telegramBot;
    private final AddRegInputUserNameAttribute addRegInputUserNameAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("/input-user-name");
    }

    @Override
    public void compute(Update update) {
        inputUserNameFlag.setFlag(true);
        telegramBot.edit(
                addRegInputUserNameAttribute.getText(),
                addRegInputUserNameAttribute.createMarkup(),
                update
        );
    }
}
