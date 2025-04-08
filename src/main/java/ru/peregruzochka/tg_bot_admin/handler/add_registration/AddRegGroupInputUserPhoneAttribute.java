package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.peregruzochka.tg_bot_admin.dto.GroupTimeSlotDto;
import ru.peregruzochka.tg_bot_admin.handler.BaseAttribute;

import java.time.format.DateTimeFormatter;


@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "attr.add-reg-group-input-user-phone")
public class AddRegGroupInputUserPhoneAttribute extends BaseAttribute {

    private String wrongNumberText;
    private String wrongUserText;

    public String generateText(GroupTimeSlotDto timeslot) {
        String date = timeslot.getStartTime().toLocalDate().toString();
        String time = slotToString(timeslot);
        String teacher = timeslot.getTeacher().getName();
        String lesson = timeslot.getGroupLesson().getName();
        String amount = String.valueOf(timeslot.getRegistrationAmount());
        String capacity = String.valueOf(timeslot.getGroupLesson().getGroupSize());

        return super.getText()
                .replace("{0}", date)
                .replace("{1}", time)
                .replace("{2}", teacher)
                .replace("{3}", lesson)
                .replace("{4}", amount)
                .replace("{5}", capacity);
    }

    public String generateWrongNumberText(GroupTimeSlotDto timeslot) {
        return wrongNumberText.replace("{}", this.generateText(timeslot));
    }

    public String generateWrongUserText(GroupTimeSlotDto timeslot) {
        return wrongUserText.replace("{}", this.generateText(timeslot));
    }

    private String slotToString(GroupTimeSlotDto timeSlotDto) {
        String startTime = timeSlotDto.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        String endTime = timeSlotDto.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        return startTime + " - " + endTime;
    }
}
