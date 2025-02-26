package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.peregruzochka.tg_bot_admin.dto.RegistrationDto;
import ru.peregruzochka.tg_bot_admin.dto.TimeSlotDto;
import ru.peregruzochka.tg_bot_admin.handler.BaseAttribute;

import java.time.format.DateTimeFormatter;


@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "attr.add-reg-input-user-phone")
public class AddRegInputUserPhoneAttribute extends BaseAttribute {

    private String wrongNumberText;

    public String generateText(RegistrationDto registration) {
        String date = registration.getSlot().getStartTime().toLocalDate().toString();
        String time = slotToString(registration.getSlot());
        String teacher = registration.getTeacher().getName();
        String lesson = registration.getLesson().getName();

        return super.getText()
                .replace("{0}", date)
                .replace("{1}", time)
                .replace("{2}", teacher)
                .replace("{3}", lesson);
    }

    public String generateWrongNumberText(RegistrationDto registration) {
        return wrongNumberText.replace("{}", this.generateText(registration));
    }

    private String slotToString(TimeSlotDto timeSlotDto) {
        String startTime = timeSlotDto.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        String endTime = timeSlotDto.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        return startTime + " - " + endTime;
    }
}
