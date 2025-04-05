package ru.peregruzochka.tg_bot_admin.handler.add_time_slot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.peregruzochka.tg_bot_admin.dto.GroupLessonDto;
import ru.peregruzochka.tg_bot_admin.handler.BaseAttribute;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "attr.choose-group-lesson")
public class ChooseGroupLessonAttribute extends BaseAttribute {
    private String groupLessonCallback;

    public String generateChooseGroupText(String teacher, LocalDateTime start) {
        String data = start.toLocalDate().toString();
        String time = convertToTime(start);
        return text
                .replace("{0}", teacher)
                .replace("{1}", data)
                .replace("{2}", time);
    }

    public InlineKeyboardMarkup generateChooseGroupLessonMarkup(List<GroupLessonDto> lessons) {
        List<List<InlineKeyboardButton>> rows = lessons.stream()
                .map(lesson -> {
                    String button = lesson.getName();
                    String callback = groupLessonCallback + lesson.getId();
                    return createButton(button, callback);
                })
                .map(List::of)
                .toList();
        return generateMarkup(rows);
    }

    private String convertToTime(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String start = time.format(formatter);
        String end = time.plusMinutes(45).format(formatter);
        return start + " - " + end;
    }
}
