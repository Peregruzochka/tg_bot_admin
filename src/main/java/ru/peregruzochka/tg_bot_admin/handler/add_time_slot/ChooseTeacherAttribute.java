package ru.peregruzochka.tg_bot_admin.handler.add_time_slot;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.peregruzochka.tg_bot_admin.dto.TeacherDto;
import ru.peregruzochka.tg_bot_admin.handler.BaseAttribute;

@Component
@ConfigurationProperties(prefix = "attr.add-time-slot")
@Getter
@Setter
public class ChooseTeacherAttribute extends BaseAttribute {
    private String chooseTeacherCallback;

    public InlineKeyboardMarkup generateTeacherMarkup(List<TeacherDto> teachers) {
        return super.generateMarkup(generateButtons(teachers));
    }

    private List<List<InlineKeyboardButton>> generateButtons(List<TeacherDto> teachers) {
        return teachers.stream()
                .map(teacherDto -> {
                    String buttonText = teacherDto.getName();
                    String buttonCallback = chooseTeacherCallback + teacherDto.getId();
                    return super.createButton(buttonText, buttonCallback);
                })
                .map(List::of)
                .toList();
    }
}
