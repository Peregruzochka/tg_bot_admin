package ru.peregruzochka.tg_bot_admin.handler.add_time_slot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.TeacherDtoCache;
import ru.peregruzochka.tg_bot_admin.dto.TeacherDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AddTimeSlotHandler implements UpdateHandler {
    private final TelegramBot bot;
    private final ChooseTeacherAttribute chooseTeacherAttribute;
    private final TeacherDtoCache teacherDtoCache;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("/add-time-slot");
    }

    @Override
    public void compute(Update update) {
        TeacherDto teacherDto = TeacherDto.builder()
                .id(UUID.randomUUID())
                .name("Татьяна")
                .imageID(UUID.randomUUID())
                .build();
        List<TeacherDto> list = new ArrayList<>() {{
            add(teacherDto);
        }};

        //TODO: backend List<TeacherDto> getAllTeachers()

        for (TeacherDto teacher : list) {
            teacherDtoCache.put(teacher.getId(), teacher);
        }

        bot.edit(
                chooseTeacherAttribute.getText(),
                chooseTeacherAttribute.generateTeacherMarkup(list),
                update
        );
    }
}
