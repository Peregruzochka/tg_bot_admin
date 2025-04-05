package ru.peregruzochka.tg_bot_admin.handler.add_time_slot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

@Component
@RequiredArgsConstructor
public class BackChooseTimeSlotTypeHandler implements UpdateHandler {

    private final TelegramBot telegramBot;
    private final ChooseTimeSlotTypeAttribute chooseTimeSlotTypeAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return hasCallback(update, "/back-choose-timeslot-type");
    }

    @Override
    public void compute(Update update) {
        String text = chooseTimeSlotTypeAttribute.getText();
        InlineKeyboardMarkup markup = chooseTimeSlotTypeAttribute.createMarkup();

        telegramBot.edit(text, markup, update);
    }
}
