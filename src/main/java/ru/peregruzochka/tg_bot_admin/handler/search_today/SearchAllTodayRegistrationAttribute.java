package ru.peregruzochka.tg_bot_admin.handler.search_today;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.peregruzochka.tg_bot_admin.dto.RegistrationDto;
import ru.peregruzochka.tg_bot_admin.handler.BaseAttribute;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "attr.search-all-today-registration")
public class SearchAllTodayRegistrationAttribute extends BaseAttribute {
    private String specialText;

    public String generateText(List<RegistrationDto> registrations) {
        String today = getTodayDate();

        StringBuilder text = new StringBuilder();
        if (registrations == null || registrations.isEmpty()) {
            return super.getText().replace("{0}", today).replace("{1}", specialText);
        }
        
        Map<UUID, List<RegistrationDto>> registrationsByTeacher = registrations.stream()
                .collect(Collectors.groupingBy(reg -> reg.getTeacher().getId()));
        for (Map.Entry<UUID, List<RegistrationDto>> entry : registrationsByTeacher.entrySet()) {
            String teacherName = entry.getValue().get(0).getTeacher().getName();
            text.append(teacherName).append(":\n");
            for (RegistrationDto registration : entry.getValue()) {
                String startTime = timeToString(registration.getSlot().getStartTime());
                String endTime = timeToString(registration.getSlot().getEndTime());
                String lessonName = registration.getLesson().getName();
                String childName = registration.getChild().getName();
                String birthday = registration.getChild().getBirthday();
                String userName = registration.getUser().getName();
                text.append(startTime).append(" - ").append(endTime).append(" ").append(lessonName).append("\n");
                text.append("Ребенок: ").append(childName).append(" (").append(birthday).append(")\n");
                text.append("Родитель: ").append(userName).append("\n\n");
            }
        }
        return super.getText().replace("{0}", today).replace("{1}", text.toString());
    }

    private String getTodayDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", new Locale("ru"));
        return LocalDate.now().format(formatter);
    }

    private String timeToString(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm", new Locale("ru"));
        return time.format(formatter);
    }
}
