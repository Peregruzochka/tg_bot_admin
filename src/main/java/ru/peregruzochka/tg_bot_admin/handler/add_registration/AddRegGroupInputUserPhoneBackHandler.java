package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.GroupRegistrationDtoSaver;
import ru.peregruzochka.tg_bot_admin.cache.InputGroupPhoneFlag;
import ru.peregruzochka.tg_bot_admin.dto.GroupTimeSlotDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

@Component
@RequiredArgsConstructor
public class AddRegGroupInputUserPhoneBackHandler implements UpdateHandler {
    private final TelegramBot telegramBot;
    private final AddRegGroupInputUserPhoneAttribute addRegGroupInputUserPhoneAttribute;
    private final GroupRegistrationDtoSaver groupRegistrationDtoSaver;
    private final InputGroupPhoneFlag inputGroupPhoneFlag;


    @Override
    public boolean isApplicable(Update update) {
        return hasCallback(update, "/add-reg-group-input-user-phone-back");
    }

    @Override
    public void compute(Update update) {
        GroupTimeSlotDto timeslot = groupRegistrationDtoSaver.getRegistration().getTimeSlot();

        inputGroupPhoneFlag.setFlag(true);
        telegramBot.edit(
                addRegGroupInputUserPhoneAttribute.generateText(timeslot),
                addRegGroupInputUserPhoneAttribute.createMarkup(),
                update
        );
    }
}
