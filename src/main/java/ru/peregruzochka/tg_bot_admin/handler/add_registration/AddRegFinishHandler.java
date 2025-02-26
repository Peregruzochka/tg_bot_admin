package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.Flag;
import ru.peregruzochka.tg_bot_admin.cache.RegistrationDtoSaver;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AddRegFinishHandler implements UpdateHandler {
    private final TelegramBot telegramBot;
    private final AddRegFinishAttribute addRegFinishAttribute;
    private final RegistrationDtoSaver registrationDtoSaver;
    private final List<Flag> flags;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("/add-reg-finish");
    }

    @Override
    public void compute(Update update) {
        flags.forEach(flag -> flag.setFlag(false));

        telegramBot.edit(
                addRegFinishAttribute.generateText(registrationDtoSaver.getRegistrationDto()),
                addRegFinishAttribute.createMarkup(),
                update
        );
    }
}
