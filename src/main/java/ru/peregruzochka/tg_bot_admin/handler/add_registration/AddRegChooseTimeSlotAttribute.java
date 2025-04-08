package ru.peregruzochka.tg_bot_admin.handler.add_registration;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.peregruzochka.tg_bot_admin.dto.GroupTimeSlotDto;
import ru.peregruzochka.tg_bot_admin.dto.TimeSlotDto;
import ru.peregruzochka.tg_bot_admin.handler.BaseAttribute;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "attr.add-reg-choose-timeslot")
public class AddRegChooseTimeSlotAttribute extends BaseAttribute {
    private String chooseTimeslotCallback;
    private String chooseGroupTimeSlotCallback;
    private String individualRegPatternText;
    private String groupRegPatternText;

    public String generateText(String date, String teacherName, List<TimeSlotDto> timeSlots, List<GroupTimeSlotDto> groupTimeSlots) {
        return super.getText()
                .replace("{0}", date)
                .replace("{1}", teacherName)
                .replace("{2}", generateSlotText(timeSlots, groupTimeSlots));
    }


    private String generateSlotText(List<TimeSlotDto> timeSlots, List<GroupTimeSlotDto> groupTimeSlots) {
        List<Pair<LocalDateTime, String>> pairs = new ArrayList<>();

        timeSlots.stream()
                .map(slot -> Pair.of(slot.getStartTime(), convertTimeSlot(slot)))
                .forEach(pairs::add);

        groupTimeSlots.stream()
                .map(slot -> Pair.of(slot.getStartTime(), convertGroupTimeSlot(slot)))
                .forEach(pairs::add);

        StringBuilder text = new StringBuilder();
        pairs.stream()
                .sorted(Comparator.comparing(Pair::getFirst))
                .map(Pair::getSecond)
                .forEach(text::append);
        return text.toString();

    }

    private String convertTimeSlot(TimeSlotDto timeSlotDto) {
        return individualRegPatternText.replace("{}", timeSlotToString(timeSlotDto));
    }

    private String convertGroupTimeSlot(GroupTimeSlotDto groupTimeSlotDto) {
        return groupRegPatternText
                .replace("{0}", groupTimeSlotToString(groupTimeSlotDto))
                .replace("{1}", String.valueOf(groupTimeSlotDto.getRegistrationAmount()))
                .replace("{2}", String.valueOf(groupTimeSlotDto.getGroupLesson().getGroupSize()))
                .replace("{3}", groupTimeSlotDto.getGroupLesson().getName());
    }

    public InlineKeyboardMarkup generateChooseTimeSlotMarkup(List<TimeSlotDto> timeSlots, List<GroupTimeSlotDto> groupTimeSlots) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();

        timeSlots.stream()
                .map(timeSlotDto -> {
                    String button = timeSlotToString(timeSlotDto);
                    String callback = chooseTimeslotCallback + timeSlotDto.getId();
                    return createButton(button, callback);
                })
                .forEach(buttons::add);

        groupTimeSlots.stream()
                .map(slot -> {
                    String button = groupTimeSlotToString(slot);
                    String callback = chooseGroupTimeSlotCallback + slot.getId();
                    return createButton(button, callback);
                })
                .forEach(buttons::add);

        List<List<InlineKeyboardButton>> newButtons = buttons.stream()
                .sorted(Comparator.comparing(InlineKeyboardButton::getText))
                .map(List::of)
                .toList();

        return super.generateMarkup(newButtons);
    }


    private String timeSlotToString(TimeSlotDto timeSlotDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String startTime = timeSlotDto.getStartTime().format(formatter);
        String endTime = timeSlotDto.getEndTime().format(formatter);
        return startTime + " - " + endTime;
    }

    private String groupTimeSlotToString(GroupTimeSlotDto timeSlotDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String startTime = timeSlotDto.getStartTime().format(formatter);
        String endTime = timeSlotDto.getEndTime().format(formatter);
        return startTime + " - " + endTime;
    }


}
