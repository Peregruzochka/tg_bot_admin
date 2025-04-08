package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.GroupRegistrationDtoSaver;
import ru.peregruzochka.tg_bot_admin.cache.InputGroupPhoneFlag;
import ru.peregruzochka.tg_bot_admin.client.BotBackendClient;
import ru.peregruzochka.tg_bot_admin.dto.GroupRegistrationDto;
import ru.peregruzochka.tg_bot_admin.dto.GroupTimeSlotDto;
import ru.peregruzochka.tg_bot_admin.dto.UserDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;
import ru.peregruzochka.tg_bot_admin.tools.PhoneNumberFormatter;
import ru.peregruzochka.tg_bot_admin.tools.PhoneNumberValidator;

@Component
@RequiredArgsConstructor
public class AddRegGroupInputPhoneFlagHandler implements UpdateHandler {
    private final InputGroupPhoneFlag inputGroupPhoneFlag;
    private final BotBackendClient botBackendClient;
    private final PhoneNumberValidator phoneNumberValidator;
    private final PhoneNumberFormatter phoneNumberFormatter;
    private final GroupRegistrationDtoSaver groupRegistrationDtoSaver;
    private final AddRegGroupInputUserPhoneAttribute addRegGroupInputUserPhoneAttribute;
    private final TelegramBot telegramBot;
    private final AddRegGroupFinishAttribute addRegGroupFinishAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return hasMessage(update, inputGroupPhoneFlag.isTrue());
    }

    @Override
    public void compute(Update update) {
        inputGroupPhoneFlag.setFlag(false);
        String phone = update.getMessage().getText();

        if (phoneNumberValidator.isValidPhoneNumber(phone)) {
            String formattedPhone = phoneNumberFormatter.formatPhoneNumber(phone);
            UserDto user = botBackendClient.getUserByPhone(formattedPhone);

            if (user != null) {
                GroupRegistrationDto registration = groupRegistrationDtoSaver.getRegistration();
                registration.setUser(user);
                registration.setChild(user.getChildren().get(0));
                groupRegistrationDtoSaver.setRegistration(registration);

                String text = addRegGroupFinishAttribute.generateText(registration);
                InlineKeyboardMarkup markup = addRegGroupFinishAttribute.generateFinishMarkup(user);
                telegramBot.send(text, markup, update);

            } else {
                inputGroupPhoneFlag.setFlag(true);

                GroupTimeSlotDto timeslot = groupRegistrationDtoSaver.getRegistration().getTimeSlot();
                String text = addRegGroupInputUserPhoneAttribute.generateWrongUserText(timeslot);
                InlineKeyboardMarkup markup = addRegGroupInputUserPhoneAttribute.createMarkup();

                telegramBot.send(text, markup, update);
            }
        } else {
            inputGroupPhoneFlag.setFlag(true);

            GroupTimeSlotDto timeslot = groupRegistrationDtoSaver.getRegistration().getTimeSlot();
            String text = addRegGroupInputUserPhoneAttribute.generateWrongNumberText(timeslot);
            InlineKeyboardMarkup markup = addRegGroupInputUserPhoneAttribute.createMarkup();

            telegramBot.send(text, markup, update);
        }
    }
}
