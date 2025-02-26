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
@ConfigurationProperties(prefix = "attr.add-reg-finish-attribute")
public class AddRegFinishAttribute extends BaseAttribute {
    private String inputUserNameButtonText;
    private String inputUserNameButtonCallback;

    private String inputChildNameButtonText;
    private String inputChildNameButtonCallback;

    private String inputChildBirthdayButtonText;
    private String inputChildBirthdayButtonCallback;

    private String changeChildButtonText;
    private String changeChildButtonCallback;

    private String infoAboutReqInput;

    public String generateText(RegistrationDto registration) {
        String date = registration.getSlot().getStartTime().toLocalDate().toString();
        String time = slotToString(registration.getSlot());
        String teacher = registration.getTeacher().getName();
        String lesson = registration.getLesson().getName();
        String clientName = registration.getUser().getName();
        String clientPhone = registration.getUser().getPhone();
        String childName = registration.getChild().getName();
        String childBirthday = registration.getChild().getBirthday();

        clientName = clientName == null ? infoAboutReqInput : clientName;
        childName = childName == null ? infoAboutReqInput : childName;
        childBirthday = childBirthday == null ? infoAboutReqInput : childBirthday;

        return super.getText()
                .replace("{0}", date)
                .replace("{1}", time)
                .replace("{2}", teacher)
                .replace("{3}", lesson)
                .replace("{4}", clientName)
                .replace("{5}", clientPhone)
                .replace("{6}", childName)
                .replace("{7}", childBirthday);
    }


    private String slotToString(TimeSlotDto timeSlotDto) {
        String startTime = timeSlotDto.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        String endTime = timeSlotDto.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        return startTime + " - " + endTime;
    }
}

