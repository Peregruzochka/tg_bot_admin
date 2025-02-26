package ru.peregruzochka.tg_bot_admin.handler.add_registration;

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
@ConfigurationProperties(prefix = "attr.add-reg-choose-teacher")
public class AddRegChooseTeacherAttribute extends BaseAttribute {
    private String chooseTeacherCallback;

    public String generateText(LocalDate date) {
        return super.getText().replace("{0}", date.toString());
    }

    public InlineKeyboardMarkup generateChooseTeacherMarkup(List<TeacherDto> teachers) {
        List<List<InlineKeyboardButton>> teacherButtons = teachers.stream()
                .map(teacher -> super.createButton(teacher.getName(), chooseTeacherCallback + teacher.getId()))
                .map(List::of)
                .toList();

        return super.generateMarkup(teacherButtons);
    }
}
