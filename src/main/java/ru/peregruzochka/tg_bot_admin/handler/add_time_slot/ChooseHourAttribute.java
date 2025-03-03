package ru.peregruzochka.tg_bot_admin.handler.add_time_slot;

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
import java.util.stream.IntStream;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "attr.choose-hour")
public class ChooseHourAttribute extends BaseAttribute {
    private int startHour;
    private int endHour;
    private String chooseHourCallback;
    private String chooseHourButtonText;
    private String exceptionText;
    private String availableTimeslot;
    private String notAvailableTimeslot;

    public String generateTextAfterException(String teacherName, String date, List<TimeSlotDto> timeSlots) {
        String timeSlotList = convertToTimeList(timeSlots);
        return exceptionText
                .replace("{0}", teacherName)
                .replace("{1}", date)
                .replace("{2}", timeSlotList);
    }

    public String generateText(String teacherName, String date, List<TimeSlotDto> timeSlots) {
        String timeSlotList = convertToTimeList(timeSlots);
        return text
                .replace("{0}", teacherName)
                .replace("{1}", date)
                .replace("{2}", timeSlotList);
    }

    public InlineKeyboardMarkup generateChooseHourMarkup() {
        return super.generateMarkup(generateChooseHourButtons());
    }

    private String convertToTimeList(List<TimeSlotDto> timeSlots) {
        StringBuilder timeSlotText = new StringBuilder();
        int i = 1;
        for (TimeSlotDto timeSlotDto : timeSlots) {
            String startTime = timeSlotDto.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            String endTime = timeSlotDto.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            String timeSlotMark = timeSlotDto.isAvailable() ? availableTimeslot : notAvailableTimeslot;
            timeSlotText.append(i++).append(". ")
                    .append(startTime).append(" - ").append(endTime)
                    .append(" ").append(timeSlotMark)
                    .append("\n");
        }
        return timeSlotText.toString();
    }

    private List<List<InlineKeyboardButton>> generateChooseHourButtons() {
        return IntStream.range(startHour, endHour)
                .mapToObj(i -> super.createButton(createChooseHourButtonText(i), createChooseHourCallback(i)))
                .map(List::of)
                .toList();
    }

    private String createChooseHourButtonText(int hour) {
        return chooseHourButtonText.replace("{0}", String.valueOf(hour)).replace("{1}", String.valueOf(hour + 1));
    }

    private String createChooseHourCallback(int hour) {
        return chooseHourCallback + hour;
    }

}
