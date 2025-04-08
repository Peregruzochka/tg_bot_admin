package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.GroupRegistrationDtoSaver;
import ru.peregruzochka.tg_bot_admin.dto.GroupRegistrationDto;
import ru.peregruzochka.tg_bot_admin.dto.UserDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

@Component
@RequiredArgsConstructor
public class AddRegGroupChangeChildHandler implements UpdateHandler {

    private final GroupRegistrationDtoSaver groupRegistrationDtoSaver;
    private final AddRegGroupChangeChildAttribute addRegGroupChangeChildAttribute;
    private final TelegramBot telegramBot;

    @Override
    public boolean isApplicable(Update update) {
        return hasCallback(update, "/group-change-child");
    }

    @Override
    public void compute(Update update) {
        GroupRegistrationDto registration = groupRegistrationDtoSaver.getRegistration();
        UserDto user = registration.getUser();

        String text = addRegGroupChangeChildAttribute.getText();
        InlineKeyboardMarkup markup = addRegGroupChangeChildAttribute.generateChildMarkup(user.getChildren());
        telegramBot.edit(text, markup, update);
    }
}
