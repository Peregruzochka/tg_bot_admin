package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.InputChildBirthdayFlag;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

@Component
@RequiredArgsConstructor
public class AddRegInputChildBirthdayHandler implements UpdateHandler {

    private final InputChildBirthdayFlag inputChildBirthdayFlag;
    private final TelegramBot telegramBot;
    private final AddRegInputChildBirthdayAttribute addRegInputChildBirthdayAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("/input-child-birthday");
    }

    @Override
    public void compute(Update update) {
        inputChildBirthdayFlag.setFlag(true);

        telegramBot.edit(
                addRegInputChildBirthdayAttribute.getText(),
                addRegInputChildBirthdayAttribute.createMarkup(),
                update
        );
    }
}
