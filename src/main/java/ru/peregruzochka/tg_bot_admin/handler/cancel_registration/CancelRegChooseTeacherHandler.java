package ru.peregruzochka.tg_bot_admin.handler.cancel_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.LocalDateSaver;
import ru.peregruzochka.tg_bot_admin.cache.TeacherSaver;
import ru.peregruzochka.tg_bot_admin.client.BotBackendClient;
import ru.peregruzochka.tg_bot_admin.dto.GroupRegistrationDto;
import ru.peregruzochka.tg_bot_admin.dto.RegistrationDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CancelRegChooseTeacherHandler implements UpdateHandler {

    private final TelegramBot telegramBot;
    private final CancelRegChooseRegAttribute cancelRegChooseRegAttribute;
    private final TeacherSaver teacherSaver;
    private final BotBackendClient botBackendClient;
    private final LocalDateSaver localDateSaver;


    @Override
    public boolean isApplicable(Update update) {
        return callbackStartWith(update, "/cancel-reg-choose-teacher:");
    }

    @Override
    public void compute(Update update) {
        UUID teacherId = UUID.fromString(getPayload(update, "/cancel-reg-choose-teacher:"));
        LocalDate date = localDateSaver.getDate();
        teacherSaver.setTeacherId(teacherId);

        List<RegistrationDto> registrations = botBackendClient.getAllActualRegistrationsByTeacherByDate(teacherId, date);
        List<GroupRegistrationDto> groupRegistrations = botBackendClient.getAllActualGroupRegistrationsByTeacherByDate(teacherId, date);



        telegramBot.edit(
                cancelRegChooseRegAttribute.generateText(registrations,groupRegistrations),
                cancelRegChooseRegAttribute.generateChooseRegMarkup(registrations, groupRegistrations),
                update
        );
    }
}
