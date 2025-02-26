package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.RegistrationDtoSaver;
import ru.peregruzochka.tg_bot_admin.client.BotBackendClient;
import ru.peregruzochka.tg_bot_admin.dto.TimeSlotDto;
import ru.peregruzochka.tg_bot_admin.handler.UpdateHandler;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AddRegChooseTimeslotHandler implements UpdateHandler {
    private final RegistrationDtoSaver registrationDtoSaver;
    private final BotBackendClient botBackendClient;
    private final TelegramBot telegramBot;
    private final AddRegChooseTimeSlotAttribute addRegChooseTimeSlotAttribute;

    @Override
    public boolean isApplicable(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("/add-reg-choose-timeslot");
    }

    @Override
    public void compute(Update update) {
        LocalDate localDate = registrationDtoSaver.getRegistrationDto().getSlot().getStartTime().toLocalDate();
        UUID teacherId = registrationDtoSaver.getRegistrationDto().getTeacher().getId();

        List<TimeSlotDto> timeslots = botBackendClient.getTeacherTimeSlotsByDate(teacherId, localDate);

        telegramBot.edit(
                addRegChooseTimeSlotAttribute.generateText(registrationDtoSaver.getRegistrationDto()),
                addRegChooseTimeSlotAttribute.generateChooseTimeSlotMarkup(timeslots),
                update
        );
    }
}
