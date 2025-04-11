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
public class CancelRegBackTeacherChooseHandler implements UpdateHandler {

    private final TelegramBot telegramBot;
    private final CancelRegChooseTeacherAttribute cancelRegChooseTeacherAttribute;
    private final BotBackendClient botBackendClient;
    private final LocalDateSaver localDateSaver;

    @Override
    public boolean isApplicable(Update update) {
        return hasCallback(update, "/cancel-reg-back-teacher-choose");
    }

    @Override
    public void compute(Update update) {
        LocalDate date = localDateSaver.getDate();

        List<RegistrationDto> registrations = botBackendClient.getAllActualRegistrationsByDate(date);
        List<GroupRegistrationDto> groupRegistrations = botBackendClient.getAllActualGroupRegistrationsByDate(date);

        telegramBot.edit(
                cancelRegChooseTeacherAttribute.generateText(date),
                cancelRegChooseTeacherAttribute.generateChooseTeacher(registrations, groupRegistrations),
                update
        );
    }
}
