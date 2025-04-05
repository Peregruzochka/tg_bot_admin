package ru.peregruzochka.tg_bot_admin.handler.add_time_slot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.peregruzochka.tg_bot_admin.dto.GroupTimeSlotDto;
import ru.peregruzochka.tg_bot_admin.dto.TimeSlotDto;
import ru.peregruzochka.tg_bot_admin.handler.BaseAttribute;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private String groupTimeslot;
    private String linePattern;

    public String generateTextAfterException(String teacherName,
                                             String date,
                                             List<TimeSlotDto> timeSlots,
                                             List<GroupTimeSlotDto> groupTimeSlots) {

        String timeSlotList = convertToTimeList(timeSlots, groupTimeSlots);
        return exceptionText
                .replace("{0}", teacherName)
                .replace("{1}", date)
                .replace("{2}", timeSlotList);
    }

    public String generateText(String teacherName,
                               String date,
                               List<TimeSlotDto> timeSlots,
                               List<GroupTimeSlotDto> groupTimeSlots) {

        String timeSlotList = convertToTimeList(timeSlots, groupTimeSlots);
        return text
                .replace("{0}", teacherName)
                .replace("{1}", date)
                .replace("{2}", timeSlotList);
    }

    public InlineKeyboardMarkup generateChooseHourMarkup() {
        return super.generateMarkup(generateChooseHourButtons());
    }

    private String convertToTimeList(List<TimeSlotDto> timeSlots, List<GroupTimeSlotDto> groupTimeSlots) {
        List<String> timeSlotLines = new ArrayList<>(timeSlots.stream()
                .map(this::convertToLine)
                .toList());

        List<String> groupTimeSlotLines = groupTimeSlots.stream()
                .map(this::convertToLine)
                .toList();

        timeSlotLines.addAll(groupTimeSlotLines);
        List<String> sortedLines = timeSlotLines.stream()
                .sorted()
                .toList();


        StringBuilder timeSlotBuilder = new StringBuilder();
        int i = 1;
        for (String line : sortedLines) {
            String numericLine = line.replace("{0}", String.valueOf(i++));
            timeSlotBuilder.append(numericLine);
        }
        return timeSlotBuilder.toString();

    }

    private String convertToLine(TimeSlotDto timeSlotDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String start = timeSlotDto.getStartTime().format(formatter);
        String end = timeSlotDto.getEndTime().format(formatter);
        String available = timeSlotDto.isAvailable() ? availableTimeslot : notAvailableTimeslot;
        return linePattern
                .replace("{1}", start)
                .replace("{2}", end)
                .replace("{3}", available);
    }

    private String convertToLine(GroupTimeSlotDto timeSlotDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String start = timeSlotDto.getStartTime().format(formatter);
        String end = timeSlotDto.getEndTime().format(formatter);
        String regAmount = String.valueOf(timeSlotDto.getRegistrationAmount());
        String capacity = String.valueOf(timeSlotDto.getGroupLesson().getGroupSize());
        String lessonName = timeSlotDto.getGroupLesson().getName();
        String groupStatus = groupTimeslot
                .replace("{0}", regAmount)
                .replace("{1}", capacity)
                + "\n<i>    " + lessonName + "</i>";
        return linePattern
                .replace("{1}", start)
                .replace("{2}", end)
                .replace("{3}", groupStatus);
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
