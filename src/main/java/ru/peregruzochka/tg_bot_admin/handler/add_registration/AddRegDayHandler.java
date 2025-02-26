package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.RegistrationDtoSaver;
import ru.peregruzochka.tg_bot_admin.cache.TeacherDtoCache;
import ru.peregruzochka.tg_bot_admin.client.BotBackendClient;
import ru.peregruzochka.tg_bot_admin.dto.TeacherDto;
import ru.peregruzochka.tg_bot_admin.dto.TimeSlotDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AddRegDayHandler implements UpdateHandler {
    private final TelegramBot bot;
    private final BotBackendClient botBackendClient;
    private final RegistrationDtoSaver registrationDtoSaver;
    private final AddRegChooseTeacherAttribute addRegChooseTeacherAttribute;
    private final TeacherDtoCache teacherDtoCache;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("/add-reg-day:");
    }

    @Override
    public void compute(Update update) {
        String day = update.getCallbackQuery().getData().replace("/add-reg-day:", "");
        LocalDate localDate = LocalDate.parse(day);
        LocalDateTime time = localDate.atStartOfDay();

        TimeSlotDto timeSlotDto = new TimeSlotDto();
        timeSlotDto.setStartTime(time);

        registrationDtoSaver.getRegistrationDto().setSlot(timeSlotDto);
        List<TeacherDto> teachers = botBackendClient.getAllTeachers();
        teachers.forEach(teacher -> teacherDtoCache.put(teacher.getId(), teacher));

        bot.edit(
                addRegChooseTeacherAttribute.generateText(localDate),
                addRegChooseTeacherAttribute.generateChooseTeacherMarkup(teachers),
                update
        );
    }
}
