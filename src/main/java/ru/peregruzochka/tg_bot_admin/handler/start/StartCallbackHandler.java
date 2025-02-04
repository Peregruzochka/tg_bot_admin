package ru.peregruzochka.tg_bot_admin.handler.start;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

@Component
@RequiredArgsConstructor
public class StartCallbackHandler implements UpdateHandler {
    private final TelegramBot bot;
    private final StartAttribute startAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("/start");
    }

    @Override
    public void compute(Update update) {
        bot.edit(startAttribute.getText(), startAttribute.createMarkup(), update);
    }
}
