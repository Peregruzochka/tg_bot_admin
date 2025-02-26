package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.peregruzochka.tg_bot_admin.dto.LessonDto;
import ru.peregruzochka.tg_bot_admin.dto.RegistrationDto;
import ru.peregruzochka.tg_bot_admin.dto.TimeSlotDto;
import ru.peregruzochka.tg_bot_admin.handler.BaseAttribute;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "attr.add-reg-choose-lessons")
public class AddRegChooseLessonAttribute extends BaseAttribute {
    private String chooseLessonCallback;

    public String generateText(RegistrationDto registration) {
        String date = registration.getSlot().getStartTime().toLocalDate().toString();
        String time = slotToString(registration.getSlot());
        String teacher = registration.getTeacher().getName();

        return super.getText()
                .replace("{0}", date)
                .replace("{1}", time)
                .replace("{2}", teacher);
    }

    public InlineKeyboardMarkup generateLessonMarkup(List<LessonDto> lessons) {
        List<List<InlineKeyboardButton>> lessonsButtons = lessons.stream()
                .map(lesson -> createButton(lesson.getName(), chooseLessonCallback + lesson.getId()))
                .map(List::of)
                .toList();

        return super.generateMarkup(lessonsButtons);
    }

    private String slotToString(TimeSlotDto timeSlotDto) {
        String startTime = timeSlotDto.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        String endTime = timeSlotDto.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        return startTime + " - " + endTime;
    }
}
