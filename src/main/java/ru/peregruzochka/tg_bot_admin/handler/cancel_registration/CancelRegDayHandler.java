package ru.peregruzochka.tg_bot_admin.handler.cancel_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.LocalDateSaver;
import ru.peregruzochka.tg_bot_admin.client.BotBackendClient;
import ru.peregruzochka.tg_bot_admin.dto.GroupRegistrationDto;
import ru.peregruzochka.tg_bot_admin.dto.RegistrationDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.time.LocalDate;
import java.util.List;


@Component
@RequiredArgsConstructor
public class CancelRegDayHandler implements UpdateHandler {
    private final TelegramBot telegramBot;
    private final CancelRegChooseDayAttribute cancelRegChooseDayAttribute;
    private final BotBackendClient botBackendClient;
    private final CancelRegChooseTeacherAttribute cancelRegChooseTeacherAttribute;
    private final LocalDateSaver localDateSaver;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("/cancel-reg-day:");
    }

    @Override
    public void compute(Update update) {
        String day = update.getCallbackQuery().getData().replace("/cancel-reg-day:", "");
        LocalDate localDate = LocalDate.parse(day);
        localDateSaver.setDate(localDate);

        if (localDate.isBefore(LocalDate.now())) {
            telegramBot.edit(
                    cancelRegChooseDayAttribute.getErrorText(),
                    cancelRegChooseDayAttribute.generateChooseDayMarkup(0),
                    update
            );
        }

        else {
            List<RegistrationDto> registrations = botBackendClient.getAllActualRegistrationsByDate(localDate);
            List<GroupRegistrationDto> groupRegistrations = botBackendClient.getAllActualGroupRegistrationsByDate(localDate);

            telegramBot.edit(
                    cancelRegChooseTeacherAttribute.generateText(localDate),
                    cancelRegChooseTeacherAttribute.generateChooseTeacher(registrations, groupRegistrations),
                    update
            );
        }
    }
}
