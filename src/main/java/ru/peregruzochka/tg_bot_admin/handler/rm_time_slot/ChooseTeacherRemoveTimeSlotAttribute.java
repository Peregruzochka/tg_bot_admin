package ru.peregruzochka.tg_bot_admin.handler.rm_time_slot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.peregruzochka.tg_bot_admin.dto.TeacherDto;
import ru.peregruzochka.tg_bot_admin.handler.BaseAttribute;

import java.util.List;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "attr.choose-teacher-rm-time-slot")
public class ChooseTeacherRemoveTimeSlotAttribute extends BaseAttribute {
    private String chooseTeacherRmTimeSlotCallback;

    public InlineKeyboardMarkup generateChooseTeacherMarkup(List<TeacherDto> teacherDtoList) {
        return generateMarkup(createTeacherButtons(teacherDtoList));
    }

    private List<List<InlineKeyboardButton>> createTeacherButtons(List<TeacherDto> teacherDtoList) {
        return teacherDtoList.stream()
                .map(teacherDto -> super.createButton(teacherDto.getName(), chooseTeacherRmTimeSlotCallback + teacherDto.getId()))
                .map(List::of)
                .toList();
    }
}
