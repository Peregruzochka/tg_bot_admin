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
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AddRegTeacherHandler implements UpdateHandler {
    private final RegistrationDtoSaver registrationDtoSaver;
    private final BotBackendClient botBackendClient;
    private final TelegramBot telegramBot;
    private final AddRegChooseTimeSlotAttribute addRegChooseTimeSlotAttribute;
    private final TeacherDtoCache teacherDtoCache;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("/add-reg-teacher:");
    }

    @Override
    public void compute(Update update) {
        UUID teacherId = UUID.fromString(update.getCallbackQuery().getData().replace("/add-reg-teacher:", ""));

        TeacherDto teacher = teacherDtoCache.get(teacherId);
        registrationDtoSaver.getRegistrationDto().setTeacher(teacher);

        LocalDate date = registrationDtoSaver.getRegistrationDto().getSlot().getStartTime().toLocalDate();
        List<TimeSlotDto> timeSlotDtoList = botBackendClient.getTeacherAvailableTimeSlotsByDate(teacherId, date);

        telegramBot.edit(
                addRegChooseTimeSlotAttribute.generateText(registrationDtoSaver.getRegistrationDto()),
                addRegChooseTimeSlotAttribute.generateChooseTimeSlotMarkup(timeSlotDtoList),
                update
        );
    }
}
