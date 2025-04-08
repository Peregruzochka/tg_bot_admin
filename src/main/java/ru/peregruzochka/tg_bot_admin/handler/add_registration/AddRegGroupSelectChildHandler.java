package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.GroupRegistrationDtoSaver;
import ru.peregruzochka.tg_bot_admin.dto.ChildDto;
import ru.peregruzochka.tg_bot_admin.dto.GroupRegistrationDto;
import ru.peregruzochka.tg_bot_admin.dto.UserDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AddRegGroupSelectChildHandler implements UpdateHandler {
    private final GroupRegistrationDtoSaver groupRegistrationDtoSaver;
    private final AddRegGroupFinishAttribute addRegGroupFinishAttribute;
    private final TelegramBot telegramBot;

    @Override
    public boolean isApplicable(Update update) {
        return callbackStartWith(update, "/add-reg-group-select-child:");
    }

    @Override
    public void compute(Update update) {
        UUID childId = UUID.fromString(getPayload(update, "/add-reg-group-select-child:"));

        GroupRegistrationDto registration = groupRegistrationDtoSaver.getRegistration();
        UserDto user = registration.getUser();

        ChildDto selectedChild = user.getChildren().stream()
                .filter(child -> childId.equals(child.getId())).findFirst()
                .orElse(null);

        if (selectedChild == null) {
            return;
        }

        registration.setChild(selectedChild);
        groupRegistrationDtoSaver.setRegistration(registration);

        String text = addRegGroupFinishAttribute.generateText(registration);
        InlineKeyboardMarkup markup = addRegGroupFinishAttribute.generateFinishMarkup(user);
        telegramBot.edit(text, markup, update);
    }
}
