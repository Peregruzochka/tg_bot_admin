package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.InputPhoneFlag;
import ru.peregruzochka.tg_bot_admin.cache.RegistrationDtoSaver;
import ru.peregruzochka.tg_bot_admin.client.BotBackendClient;
import ru.peregruzochka.tg_bot_admin.dto.ChildDto;
import ru.peregruzochka.tg_bot_admin.dto.UserDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;
import ru.peregruzochka.tg_bot_admin.tools.PhoneNumberFormatter;
import ru.peregruzochka.tg_bot_admin.tools.PhoneNumberValidator;

import java.util.List;
import java.util.Objects;

import static ru.peregruzochka.tg_bot_admin.dto.RegistrationDto.RegistrationType.NEW_USER;
import static ru.peregruzochka.tg_bot_admin.dto.RegistrationDto.RegistrationType.REGULAR_USER;

@Component
@RequiredArgsConstructor
public class AddRegInputUserPhoneHandler implements UpdateHandler {


    private final InputPhoneFlag inputPhoneFlag;
    private final PhoneNumberValidator phoneNumberValidator;
    private final BotBackendClient botBackendClient;
    private final PhoneNumberFormatter phoneNumberFormatter;
    private final TelegramBot telegramBot;
    private final RegistrationDtoSaver registrationDtoSaver;
    private final AddRegFinishAttribute addRegFinishAttribute;
    private final AddRegInputUserPhoneAttribute addRegInputUserPhoneAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasMessage() && inputPhoneFlag.isTrue();
    }

    @Override
    public void compute(Update update) {
        inputPhoneFlag.setFlag(false);
        String phone = update.getMessage().getText();
        if (phoneNumberValidator.isValidPhoneNumber(phone)) {
            String formattedPhone = phoneNumberFormatter.formatPhoneNumber(phone);
            UserDto user = botBackendClient.getUserByPhone(formattedPhone);
            registrationDtoSaver.getRegistrationDto().setType(REGULAR_USER);

            if (Objects.isNull(user)) {
                user = new UserDto();
                user.setPhone(formattedPhone);
                ChildDto child = new ChildDto();
                user.setChildren(List.of(child));
                registrationDtoSaver.getRegistrationDto().setType(NEW_USER);
            }

            registrationDtoSaver.getRegistrationDto().setUser(user);
            registrationDtoSaver.getRegistrationDto().setChild(user.getChildren().get(0));

            telegramBot.send(
                    addRegFinishAttribute.generateText(registrationDtoSaver.getRegistrationDto()),
                    addRegFinishAttribute.createMarkup(),
                    update
            );

        } else {

            inputPhoneFlag.setFlag(true);
            telegramBot.send(
                    addRegInputUserPhoneAttribute.generateWrongNumberText(registrationDtoSaver.getRegistrationDto()),
                    addRegInputUserPhoneAttribute.createMarkup(),
                    update
            );
        }
    }
}

