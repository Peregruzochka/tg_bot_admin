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
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AddRegGroupTimeSlotHandler implements UpdateHandler {

    private final BotBackendClient botBackendClient;
    private final GroupRegistrationDtoSaver groupRegistrationDtoSaver;
    private final InputGroupPhoneFlag inputGroupPhoneFlag;
    private final AddRegGroupInputUserPhoneAttribute addRegGroupInputUserPhoneAttribute;
    private final TelegramBot telegramBot;

    @Override
    public boolean isApplicable(Update update) {
        return callbackStartWith(update, "/add-reg-group-timeslot:");
    }

    @Override
    public void compute(Update update) {
        UUID timeSlotID = UUID.fromString(getPayload(update, "/add-reg-group-timeslot:"));
        GroupTimeSlotDto groupTimeSlot = botBackendClient.getGroupTimeSlot(timeSlotID);

        GroupRegistrationDto groupRegistration = GroupRegistrationDto.builder()
                .timeSlot(groupTimeSlot)
                .build();
        groupRegistrationDtoSaver.setRegistration(groupRegistration);

        inputGroupPhoneFlag.setFlag(true);

        String text = addRegGroupInputUserPhoneAttribute.generateText(groupTimeSlot);
        InlineKeyboardMarkup markup = addRegGroupInputUserPhoneAttribute.createMarkup();
        telegramBot.edit(text, markup, update);
    }
}
