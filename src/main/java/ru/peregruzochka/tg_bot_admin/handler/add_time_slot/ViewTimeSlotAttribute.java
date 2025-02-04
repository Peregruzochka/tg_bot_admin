package ru.peregruzochka.tg_bot_admin.handler.add_time_slot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.peregruzochka.tg_bot_admin.dto.TimeSlotDto;
import ru.peregruzochka.tg_bot_admin.handler.BaseAttribute;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "attr.view-time-slot")
public class ViewTimeSlotAttribute extends BaseAttribute {

    public String generateText(String teacherName, String localDate, List<TimeSlotDto> timeSlotDtos) {
        StringBuilder timeSlotText = new StringBuilder();
        int i = 1;
        for (TimeSlotDto timeSlotDto : timeSlotDtos) {
            String startTime = convertTime(timeSlotDto.getStartTime());
            String endTime = convertTime(timeSlotDto.getEndTime());
            timeSlotText.append(i++).append(". ").append(startTime).append(" - ").append(endTime).append("\n");
        }
        return super.getText()
                .replace("{0}", teacherName)
                .replace("{1}", localDate)
                .replace("{2}", timeSlotText.toString());
    }

    private String convertTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return localDateTime.format(formatter);
    }
}
