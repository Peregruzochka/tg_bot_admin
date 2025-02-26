package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.peregruzochka.tg_bot_admin.dto.RegistrationDto;
import ru.peregruzochka.tg_bot_admin.dto.TimeSlotDto;
import ru.peregruzochka.tg_bot_admin.handler.BaseAttribute;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "attr.add-reg-choose-timeslot")
public class AddRegChooseTimeSlotAttribute extends BaseAttribute {
    private String chooseTimeslotCallback;

    public String generateText(RegistrationDto savedRegistration) {
        String date = savedRegistration.getSlot().getStartTime().toLocalDate().toString();
        String teacher = savedRegistration.getTeacher().getName();

        return super.getText()
                .replace("{0}", date)
                .replace("{1}", teacher);
    }

    public InlineKeyboardMarkup generateChooseTimeSlotMarkup(List<TimeSlotDto> timeSlots) {
        List<List<InlineKeyboardButton>> timeSlotButtons = timeSlots.stream()
                .map(timeSlotDto -> {
                    String button = timeSlotToString(timeSlotDto);
                    String callback = chooseTimeslotCallback + timeSlotDto.getId();
                    return createButton(button, callback);
                })
                .map(List::of)
                .toList();

        return super.generateMarkup(timeSlotButtons);
    }


    private String timeSlotToString(TimeSlotDto timeSlotDto) {
        String startTime = timeSlotDto.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        String endTime = timeSlotDto.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        return startTime + " - " + endTime;
    }


}
