package ru.peregruzochka.tg_bot_admin.handler.cancel_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.CancelRegistrationPool;
import ru.peregruzochka.tg_bot_admin.cache.TeacherSaver;
import ru.peregruzochka.tg_bot_admin.dto.RegistrationDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CancelRegChooseTeacherHandler implements UpdateHandler {

    private final TelegramBot telegramBot;
    private final CancelRegistrationPool cancelRegistrationPool;
    private final CancelRegChooseRegAttribute cancelRegChooseRegAttribute;
    private final TeacherSaver teacherSaver;


    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("/cancel-reg-choose-teacher:");
    }

    @Override
    public void compute(Update update) {
        UUID teacherId = UUID.fromString(update.getCallbackQuery().getData().replace("/cancel-reg-choose-teacher:", ""));

        List<RegistrationDto> registrations = cancelRegistrationPool.getRegistrations().stream()
                .filter(reg -> reg.getTeacher().getId().equals(teacherId))
                .filter(reg -> reg.getSlot().getStartTime().isAfter(LocalDateTime.now()))
                .toList();

        teacherSaver.setTeacherId(teacherId);

        telegramBot.edit(
                cancelRegChooseRegAttribute.generateText(registrations),
                cancelRegChooseRegAttribute.generateChooseRegMarkup(registrations),
                update
        );
    }
}
