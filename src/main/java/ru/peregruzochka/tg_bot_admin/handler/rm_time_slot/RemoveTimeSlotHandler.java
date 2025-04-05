package ru.peregruzochka.tg_bot_admin.handler.rm_time_slot;

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
public class RemoveTimeSlotHandler implements UpdateHandler {
    private final TelegramBot bot;
    private final BotBackendClient botBackendClient;
    private final TeacherDtoCache teacherDtoCache;
    private final ChooseTeacherRemoveTimeSlotAttribute chooseTeacherRemoveTimeSlotAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return hasCallback(update, "/rm-time-slot");
    }

    @Override
    public void compute(Update update) {
        List<TeacherDto> teacherDtoList = botBackendClient.getAllTeachers();
        for (TeacherDto teacherDto : teacherDtoList) {
            teacherDtoCache.put(teacherDto.getId(), teacherDto);
        }

        bot.edit(
                chooseTeacherRemoveTimeSlotAttribute.getText(),
                chooseTeacherRemoveTimeSlotAttribute.generateChooseTeacherMarkup(teacherDtoList),
                update
        );
    }
}
