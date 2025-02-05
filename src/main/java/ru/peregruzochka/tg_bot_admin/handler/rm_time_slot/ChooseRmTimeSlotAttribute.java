package ru.peregruzochka.tg_bot_admin.handler.rm_time_slot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.peregruzochka.tg_bot_admin.dto.TimeSlotDto;
import ru.peregruzochka.tg_bot_admin.handler.BaseAttribute;

import java.time.format.DateTimeFormatter;
import java.util.List;


@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "attr.choose-rm-time-slot")
public class ChooseRmTimeSlotAttribute extends BaseAttribute {
    private String rmTimeSlotCallback;

    public String generateText(String teacherName, String date) {
        return super.getText().replace("{0}", teacherName).replace("{1}", date);
    }

    public InlineKeyboardMarkup generateChooseRmTimeSlotMarkup(List<TimeSlotDto> timeSlotDtoByDate) {
        List<List<InlineKeyboardButton>> buttons = timeSlotDtoByDate.stream()
                .map(timeSlotDto -> createButton(timeSlotToString(timeSlotDto), rmTimeSlotCallback + timeSlotDto.getId()))
                .map(List::of)
                .toList();
        return super.generateMarkup(buttons);
    }

    private String timeSlotToString(TimeSlotDto timeSlotDto) {
        String startTime = timeSlotDto.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        String endTime = timeSlotDto.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        return startTime + " - " + endTime;
    }
}
