package ru.peregruzochka.tg_bot_admin.handler.add_time_slot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.TeacherDtoCache;
import ru.peregruzochka.tg_bot_admin.cache.TimeSlotSaver;
import ru.peregruzochka.tg_bot_admin.client.BotBackendClient;
import ru.peregruzochka.tg_bot_admin.dto.GroupLessonDto;
import ru.peregruzochka.tg_bot_admin.dto.TeacherDto;
import ru.peregruzochka.tg_bot_admin.dto.TimeSlotDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GroupHandler implements UpdateHandler {
    private final TimeSlotSaver timeSlotSaver;
    private final TeacherDtoCache teacherDtoCache;
    private final TelegramBot telegramBot;
    private final ChooseGroupLessonAttribute chooseGroupLessonAttribute;
    private final BotBackendClient botBackendClient;

    @Override
    public boolean isApplicable(Update update) {
        return hasCallback(update, "/group");
    }

    @Override
    public void compute(Update update) {
        TimeSlotDto timeSlot = timeSlotSaver.getTimeSlotDto();
        UUID teacherId = timeSlot.getTeacherId();
        TeacherDto teacher = teacherDtoCache.get(teacherId);
        LocalDateTime startTime = timeSlot.getStartTime();

        List<GroupLessonDto> lessons = botBackendClient.getGroupLessonsByTeacher(teacherId);

        String text = chooseGroupLessonAttribute.generateChooseGroupText(teacher.getName(), startTime);
        InlineKeyboardMarkup markup = chooseGroupLessonAttribute.generateChooseGroupLessonMarkup(lessons);
        telegramBot.edit(text, markup, update);
    }
}
