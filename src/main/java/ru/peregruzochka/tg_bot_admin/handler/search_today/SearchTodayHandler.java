package ru.peregruzochka.tg_bot_admin.handler.search_today;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.client.BotBackendClient;
import ru.peregruzochka.tg_bot_admin.dto.RegistrationDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchTodayHandler implements UpdateHandler {
    private final TelegramBot bot;
    private final BotBackendClient botBackendClient;
    private final SearchAllTodayRegistrationAttribute searchAllTodayRegistrationAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("/search-today");
    }

    @Override
    public void compute(Update update) {
        List<RegistrationDto> registrations = botBackendClient.getAllRegistrationsByToday();
        bot.edit(
                searchAllTodayRegistrationAttribute.generateText(registrations),
                searchAllTodayRegistrationAttribute.createMarkup(),
                update
        );
    }
}
