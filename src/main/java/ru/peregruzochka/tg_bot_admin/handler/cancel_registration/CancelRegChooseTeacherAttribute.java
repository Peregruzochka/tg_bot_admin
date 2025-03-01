package ru.peregruzochka.tg_bot_admin.handler.cancel_registration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.peregruzochka.tg_bot_admin.dto.TeacherDto;
import ru.peregruzochka.tg_bot_admin.handler.BaseAttribute;

import java.time.LocalDate;
import java.util.List;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "attr.cancel-reg-choose-teacher")
public class CancelRegChooseTeacherAttribute extends BaseAttribute {
    private String cancelRegChooseTeacherCallback;

    public String generateText(LocalDate localDate) {
        return text.replace("{}", localDate.toString());
    }

    public InlineKeyboardMarkup generateChooseTeacher(List<TeacherDto> teachers) {
        List<List<InlineKeyboardButton>> teacherButtons = teachers.stream()
                .map(teacher -> createButton(teacher.getName(), cancelRegChooseTeacherCallback + teacher.getId()))
                .map(List::of)
                .toList();

        return generateMarkup(teacherButtons);
    }
}
