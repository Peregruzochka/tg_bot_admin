package ru.peregruzochka.tg_bot_admin.handler.rm_time_slot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.LocalDateSaver;
import ru.peregruzochka.tg_bot_admin.cache.TeacherDtoCache;
import ru.peregruzochka.tg_bot_admin.cache.TeacherSaver;
import ru.peregruzochka.tg_bot_admin.client.BotBackendClient;
import ru.peregruzochka.tg_bot_admin.dto.GroupTimeSlotDto;
import ru.peregruzochka.tg_bot_admin.dto.TimeSlotDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DateRmHandler implements UpdateHandler {
    private final TelegramBot bot;
    private final ChooseRmTimeSlotAttribute chooseRmTimeSlotAttribute;
    private final TeacherDtoCache teacherDtoCache;
    private final LocalDateSaver localDateSaver;
    private final TeacherSaver teacherSaver;
    private final BotBackendClient botBackendClient;

    @Override
    public boolean isApplicable(Update update) {
        return callbackStartWith(update, "/date-rm:");
    }

    @Override
    public void compute(Update update) {
        LocalDate date = LocalDate.parse(getPayload(update, "/date-rm:"));
        UUID teacherId = teacherSaver.getTeacherId();
        String teacherName = teacherDtoCache.get(teacherId).getName();

        List<TimeSlotDto> timeslots = botBackendClient.getTeacherTimeSlotsByDate(teacherId, date);
        List<GroupTimeSlotDto> groupTimeslots = botBackendClient.getTeacherGroupTimeSlotsByDate(teacherId, date);

        localDateSaver.setDate(date);

        bot.edit(
                chooseRmTimeSlotAttribute.generateText(teacherName, date.toString()),
                chooseRmTimeSlotAttribute.generateChooseRmTimeSlotMarkup(timeslots, groupTimeslots),
                update
        );
    }
}
