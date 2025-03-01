package ru.peregruzochka.tg_bot_admin.handler.cancel_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.LocalDateSaver;
import ru.peregruzochka.tg_bot_admin.cache.TeacherSaver;
import ru.peregruzochka.tg_bot_admin.client.BotBackendClient;
import ru.peregruzochka.tg_bot_admin.dto.RegistrationDto;
import ru.peregruzochka.tg_bot_admin.dto.TeacherDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static ru.peregruzochka.tg_bot_admin.dto.RegistrationDto.RegistrationType.CANCEL;

@Component
@RequiredArgsConstructor
public class CancelRedBackChooseHandler implements UpdateHandler {

    private final BotBackendClient botBackendClient;
    private final LocalDateSaver localDateSaver;
    private final TeacherSaver teacherSaver;
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
        UUID teacherId = teacherSaver.getTeacherId();

        List<RegistrationDto> registrations = botBackendClient.getAllRegistrationsByDate(localDate);

        List<TeacherDto> teachers = registrations.stream()
                .map(RegistrationDto::getTeacher)
                .distinct()
                .toList();

        List<RegistrationDto> teacherRegistrationByDate = registrations.stream()
                .filter(reg -> !reg.getType().equals(CANCEL))
                .filter(reg -> reg.getTeacher().getId().equals(teacherId))
                .toList();

        if (teacherRegistrationByDate.isEmpty()) {
            telegramBot.edit(
                    cancelRegChooseTeacherAttribute.generateText(localDate),
                    cancelRegChooseTeacherAttribute.generateChooseTeacher(teachers),
                    update
            );

        } else {
            telegramBot.edit(
                    cancelRegChooseRegAttribute.generateText(teacherRegistrationByDate),
                    cancelRegChooseRegAttribute.generateChooseRegMarkup(teacherRegistrationByDate),
                    update
            );
        }

    }
}
