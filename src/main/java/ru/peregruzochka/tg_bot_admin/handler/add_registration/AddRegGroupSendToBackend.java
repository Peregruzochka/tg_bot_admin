package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.GroupRegistrationDtoSaver;
import ru.peregruzochka.tg_bot_admin.client.BotBackendClient;
import ru.peregruzochka.tg_bot_admin.dto.GroupRegistrationDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

@Component
@RequiredArgsConstructor
public class AddRegGroupSendToBackend implements UpdateHandler {
    private final GroupRegistrationDtoSaver groupRegistrationDtoSaver;
    private final BotBackendClient botBackendClient;
    private final AddRegSendToBackendAttribute addRegSendToBackendAttribute;
    private final TelegramBot telegramBot;

    @Override
    public boolean isApplicable(Update update) {
        return hasCallback(update, "/add-reg-group-send-to-backend");
    }

    @Override
    public void compute(Update update) {
        GroupRegistrationDto groupRegistrationDto = groupRegistrationDtoSaver.getRegistration();
        botBackendClient.addGroupRegistration(groupRegistrationDto);

        String text = addRegSendToBackendAttribute.getText();
        InlineKeyboardMarkup markup = addRegSendToBackendAttribute.createMarkup();

        telegramBot.edit(text, markup, update);
    }
}
