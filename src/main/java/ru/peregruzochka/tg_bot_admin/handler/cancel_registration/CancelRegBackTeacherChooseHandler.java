package ru.peregruzochka.tg_bot_admin.handler.cancel_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.CancelRegistrationPool;
import ru.peregruzochka.tg_bot_admin.dto.RegistrationDto;
import ru.peregruzochka.tg_bot_admin.dto.TeacherDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CancelRegBackTeacherChooseHandler implements UpdateHandler {

    private final TelegramBot telegramBot;
    private final CancelRegistrationPool cancelRegistrationPool;
    private final CancelRegChooseTeacherAttribute cancelRegChooseTeacherAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("/cancel-reg-back-teacher-choose");
    }

    @Override
    public void compute(Update update) {
        List<RegistrationDto> registrations = cancelRegistrationPool.getRegistrations();
        List<TeacherDto> teachers = registrations.stream()
                .map(RegistrationDto::getTeacher)
                .distinct()
                .toList();

        LocalDate localDate = registrations.get(0).getSlot().getStartTime().toLocalDate();

        telegramBot.edit(
                cancelRegChooseTeacherAttribute.generateText(localDate),
                cancelRegChooseTeacherAttribute.generateChooseTeacher(teachers),
                update
        );
    }
}
