package ru.peregruzochka.tg_bot_admin.handler.search_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.client.BotBackendClient;
import ru.peregruzochka.tg_bot_admin.dto.GroupRegistrationDto;
import ru.peregruzochka.tg_bot_admin.dto.RegistrationDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchByDayDayHandler implements UpdateHandler {

    private final BotBackendClient botBackendClient;
    private final TelegramBot telegramBot;
    private final ShowRegistrationByDayAttribute showRegistrationByDayAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return callbackStartWith(update, "/search-by-day-day:");
    }

    @Override
    public void compute(Update update) {
        String day = getPayload(update, "/search-by-day-day:");
        LocalDate localDate = LocalDate.parse(day);

        List<RegistrationDto> registrations = botBackendClient.getAllActualRegistrationsByDate(localDate);
        List<GroupRegistrationDto> groupRegistrations = botBackendClient.getAllActualGroupRegistrationsByDate(localDate);

        telegramBot.edit(
                showRegistrationByDayAttribute.generateText(localDate, registrations, groupRegistrations),
                showRegistrationByDayAttribute.createMarkup(),
                update
        );
    }
}
