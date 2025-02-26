package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.RegistrationDtoSaver;
import ru.peregruzochka.tg_bot_admin.cache.TeacherDtoCache;
import ru.peregruzochka.tg_bot_admin.client.BotBackendClient;
import ru.peregruzochka.tg_bot_admin.dto.TeacherDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AddRegChooseTeacherHandler implements UpdateHandler {
    private final RegistrationDtoSaver registrationDtoSaver;
    private final AddRegChooseTeacherAttribute addRegChooseTeacherAttribute;
    private final TelegramBot telegramBot;
    private final BotBackendClient botBackendClient;
    private final TeacherDtoCache teacherDtoCache;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("/add-reg-choose-teacher");
    }

    @Override
    public void compute(Update update) {
        LocalDate date = registrationDtoSaver.getRegistrationDto().getSlot().getStartTime().toLocalDate();
        List<TeacherDto> teachers = botBackendClient.getAllTeachers();
        teachers.forEach(teacher -> teacherDtoCache.put(teacher.getId(), teacher));

        telegramBot.edit(
                addRegChooseTeacherAttribute.generateText(date),
                addRegChooseTeacherAttribute.generateChooseTeacherMarkup(teachers),
                update
        );
    }
}
