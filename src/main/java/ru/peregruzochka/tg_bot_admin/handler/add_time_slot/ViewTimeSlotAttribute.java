package ru.peregruzochka.tg_bot_admin.handler.add_time_slot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.peregruzochka.tg_bot_admin.dto.TimeSlotDto;
import ru.peregruzochka.tg_bot_admin.handler.BaseAttribute;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "attr.view-time-slot")
public class ViewTimeSlotAttribute extends BaseAttribute {
    private String availableTimeslot;
    private String notAvailableTimeslot;

    public String generateText(String teacherName, String localDate, List<TimeSlotDto> timeSlots) {
        String timeSlotText = convertToTimeList(timeSlots);
        return super.getText()
                .replace("{0}", teacherName)
                .replace("{1}", localDate)
                .replace("{2}", timeSlotText);
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
}
