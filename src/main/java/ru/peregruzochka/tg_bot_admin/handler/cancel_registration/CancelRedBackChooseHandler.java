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
public class CancelRedBackChooseHandler implements UpdateHandler {

    private final BotBackendClient botBackendClient;
    private final LocalDateSaver localDateSaver;
    private final TelegramBot telegramBot;
    private final CancelRegChooseRegAttribute cancelRegChooseRegAttribute;
    private final CancelRegChooseTeacherAttribute cancelRegChooseTeacherAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("/cancel-reg-back-choose");
    }

    @Override
    public void compute(Update update) {
        LocalDate localDate = localDateSaver.getDate();

        List<RegistrationDto> registrations = botBackendClient.getAllActualRegistrationsByDate(localDate);
        List<GroupRegistrationDto> groupRegistrations = botBackendClient.getAllActualGroupRegistrationsByDate(localDate);


        if (registrations.isEmpty() && groupRegistrations.isEmpty()) {
            telegramBot.edit(
                    cancelRegChooseTeacherAttribute.generateText(localDate),
                    cancelRegChooseTeacherAttribute.generateChooseTeacher(registrations, groupRegistrations),
                    update
            );

        } else {
            telegramBot.edit(
                    cancelRegChooseRegAttribute.generateText(registrations, groupRegistrations),
                    cancelRegChooseRegAttribute.generateChooseRegMarkup(registrations, groupRegistrations),
                    update
            );
        }
    }
}
