package ru.peregruzochka.tg_bot_admin.handler.search_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.client.BotBackendClient;
import ru.peregruzochka.tg_bot_admin.dto.RegistrationDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SearchByDayDayHandler implements UpdateHandler {

    private final BotBackendClient botBackendClient;
    private final TelegramBot telegramBot;
    private final ShowRegistrationByDayAttribute showRegistrationByDayAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("/search-by-day-day:");
    }

    @Override
    public void compute(Update update) {
        String day = update.getCallbackQuery().getData().replace("/search-by-day-day:", "");
        LocalDate localDate = LocalDate.parse(day);

        List<RegistrationDto> registrations = botBackendClient.getAllRegistrationsByDate(localDate);
        Map<String, List<RegistrationDto>> registrationsByTeacherId =
                registrations.stream()
                        .sorted(Comparator.comparing(reg -> reg.getSlot().getStartTime()))
                        .collect(Collectors.groupingBy(reg -> reg.getTeacher().getName()));

        telegramBot.edit(
                showRegistrationByDayAttribute.generateText(registrationsByTeacherId, localDate),
                showRegistrationByDayAttribute.createMarkup(),
                update
        );
    }
}
