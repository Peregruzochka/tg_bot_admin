package ru.peregruzochka.tg_bot_admin.handler.start;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.AdminChatIdSaver;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;
import ru.peregruzochka.tg_bot_admin.tools.AuthUserChecker;

@Component
@RequiredArgsConstructor
public class StartMessageHandler implements UpdateHandler {
    private final TelegramBot bot;
    private final StartAttribute startAttribute;
    private final AdminChatIdSaver adminChatIdSaver;
    private final AuthUserChecker authUserChecker;

    @Value("${attr.auth-error-text}")
    private String authErrorText;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasMessage() && update.getMessage().getText().equals("/start");
    }

    @Override
    public void compute(Update update) {
        Long chatId = update.getMessage().getChatId();

        if (authUserChecker.check(chatId)) {
            adminChatIdSaver.setChatId(chatId);
            bot.send(
                    startAttribute.getText(),
                    startAttribute.createMarkup(),
                    update
            );

        } else {
            bot.send(authErrorText, update);
        }
    }
}
