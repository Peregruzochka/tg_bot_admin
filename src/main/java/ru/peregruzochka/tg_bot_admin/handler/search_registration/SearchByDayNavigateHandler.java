package ru.peregruzochka.tg_bot_admin.handler.search_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

@Component
@RequiredArgsConstructor
public class SearchByDayNavigateHandler implements UpdateHandler {
    private final TelegramBot telegramBot;
    private final SearchByDayChooseDayAttribute searchByDayChooseDayAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("/search-by-day-navigate:");
    }

    @Override
    public void compute(Update update) {
        int offset = Integer.parseInt(update.getCallbackQuery().getData().replace("/search-by-day-navigate:", ""));
        telegramBot.edit(
                searchByDayChooseDayAttribute.getText(),
                searchByDayChooseDayAttribute.generateChooseDayMarkup(offset),
                update
        );
    }
}
