package ru.peregruzochka.tg_bot_admin.handler.search_registration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.peregruzochka.tg_bot_admin.dto.RegistrationDto;
import ru.peregruzochka.tg_bot_admin.handler.BaseAttribute;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "attr.show-reg-by-day")
public class ShowRegistrationByDayAttribute extends BaseAttribute {
    private String registrationPattern;

    public String generateText(Map<String, List<RegistrationDto>> registrationsByTeacher, LocalDate date) {
        StringBuilder regText = new StringBuilder();
        regText.append(convertDateToString(date));
        regText.append("\n\n");
        for (Map.Entry<String, List<RegistrationDto>> entry : registrationsByTeacher.entrySet()) {
            String teacher = entry.getKey();
            List<RegistrationDto> registrations = entry.getValue();
            regText.append(teacher).append("\n\n");
            for (RegistrationDto registration : registrations) {
                regText.append(createRegText(registration)).append("\n");
            }
            regText.append("\n");
        }
        return regText.toString();
    }

    private String createRegText(RegistrationDto registrationDto) {
        String time = convertTimeToString(registrationDto.getSlot().getStartTime(), registrationDto.getSlot().getEndTime());
        String lesson = registrationDto.getLesson().getName();
        String childName = registrationDto.getChild().getName();
        String childBirthday = registrationDto.getChild().getBirthday();
        String parent = registrationDto.getUser().getName();
        String phone = registrationDto.getUser().getPhone();

        return registrationPattern
                .replace("{1}", time)
                .replace("{2}", lesson)
                .replace("{3}", childName)
                .replace("{4}", childBirthday)
                .replace("{5}", parent)
                .replace("{6}", phone);

    }

    private String convertTimeToString(LocalDateTime startDate, LocalDateTime endDate) {
        String start = startDate.format(DateTimeFormatter.ofPattern("HH:mm"));
        String end = endDate.format(DateTimeFormatter.ofPattern("HH:mm"));
        return start + " - " + end;
    }

    private String convertDateToString(LocalDate date) {
        String dayOfWeek = date.getDayOfWeek()
                .getDisplayName(TextStyle.FULL, new Locale("ru", "RU"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM", new Locale("ru", "RU"));
        String formattedDate = date.format(formatter);

        return dayOfWeek + ", " + formattedDate;
    }
}
