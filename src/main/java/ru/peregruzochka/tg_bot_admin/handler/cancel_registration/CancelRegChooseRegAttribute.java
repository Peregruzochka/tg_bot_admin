package ru.peregruzochka.tg_bot_admin.handler.cancel_registration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.peregruzochka.tg_bot_admin.dto.RegistrationDto;
import ru.peregruzochka.tg_bot_admin.handler.BaseAttribute;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "attr.cancel-reg-choose-reg")
public class CancelRegChooseRegAttribute extends BaseAttribute {
    private String registrationText;
    private String cancelRegCallback;

    public String generateText(List<RegistrationDto> registrations) {
        String teacherName = registrations.get(0).getTeacher().getName();
        String localDate = registrations.get(0).getSlot().getStartTime().toLocalDate().toString();

        StringBuilder registrationList = new StringBuilder();
        for (RegistrationDto registration : registrations) {
            registrationList.append(generateRegistrationText(registration));
            registrationList.append("\n");
        }

        return text
                .replace("{0}", localDate)
                .replace("{1}", teacherName)
                .replace("{2}", registrationList.toString());
    }

    public InlineKeyboardMarkup generateChooseRegMarkup(List<RegistrationDto> registrations) {
        List<List<InlineKeyboardButton>> registrationsButtons = registrations.stream()
                .map(registration -> {
                    String buttonText = createButtonText(registration);
                    String callback = cancelRegCallback + registration.getId();

                    return createButton(buttonText, callback);
                })
                .map(List::of)
                .toList();

        return generateMarkup(registrationsButtons);
    }

    private String convertTime(LocalDateTime start, LocalDateTime end) {
        String startTime = start.format(DateTimeFormatter.ofPattern("HH:mm"));
        String endTime = end.format(DateTimeFormatter.ofPattern("HH:mm"));
        return startTime + " - " + endTime;
    }

    private String generateRegistrationText(RegistrationDto registration) {
        String time = convertTime(registration.getSlot().getStartTime(), registration.getSlot().getEndTime());
        String lesson = registration.getLesson().getName();
        String childName = registration.getChild().getName();
        String childBirthday = registration.getChild().getBirthday();
        String parentName = registration.getUser().getName();

        return registrationText
                .replace("{0}", time)
                .replace("{1}", lesson)
                .replace("{2}", childName)
                .replace("{3}", childBirthday)
                .replace("{4}", parentName);
    }

    private String createButtonText(RegistrationDto registration) {
        return convertTime(registration.getSlot().getStartTime(), registration.getSlot().getEndTime())
                + " "
                + registration.getChild().getName();
    }
}
