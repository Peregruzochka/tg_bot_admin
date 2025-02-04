package ru.peregruzochka.tg_bot_admin.handler.add_time_slot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.TeacherDtoCache;
import ru.peregruzochka.tg_bot_admin.client.BotBackendClient;
import ru.peregruzochka.tg_bot_admin.dto.TeacherDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AddTimeSlotHandler implements UpdateHandler {
    private final TelegramBot bot;
    private final ChooseTeacherAttribute chooseTeacherAttribute;
    private final TeacherDtoCache teacherDtoCache;
    private final BotBackendClient botBackendClient;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("/add-time-slot");
    }

    @Override
    public void compute(Update update) {
        List<TeacherDto> teachers = botBackendClient.getAllTeachers();

        for (TeacherDto teacher : teachers) {
            teacherDtoCache.put(teacher.getId(), teacher);
        }

        bot.edit(
                chooseTeacherAttribute.getText(),
                chooseTeacherAttribute.generateTeacherMarkup(teachers),
                update
        );
    }


}
