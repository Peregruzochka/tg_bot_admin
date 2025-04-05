package ru.peregruzochka.tg_bot_admin.handler.rm_time_slot;

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
@ConfigurationProperties(prefix = "attr.choose-rm-time-slot")
public class ChooseRmTimeSlotAttribute extends BaseAttribute {
    private String rmTimeSlotCallback;
    private String rmGroupTimeSlotCallback;
    private String buttonTextPattern;
    private String individualLabel;
    private String groupLabel;

    public String generateText(String teacherName, String date) {
        return super.getText().replace("{0}", teacherName).replace("{1}", date);
    }

    public InlineKeyboardMarkup generateChooseRmTimeSlotMarkup(List<TimeSlotDto> timeslots,
                                                               List<GroupTimeSlotDto> groupTimeslots) {

        List<Pair<String, String>> pairs = new ArrayList<>(timeslots.stream()
                .filter(TimeSlotDto::isAvailable)
                .map(this::convertToStringPair)
                .toList());

        List<Pair<String, String>> groupPairs = groupTimeslots.stream()
                .filter(slot -> slot.getRegistrationAmount() == 0)
                .map(this::convertToStringPair)
                .toList();

        pairs.addAll(groupPairs);

        List<Pair<String, String>> sortedPairs = pairs.stream()
                .sorted(Comparator.comparing(Pair::getFirst))
                .toList();

        int i = 1;
        List<Pair<String, String>> enumeratedPairs = new ArrayList<>();
        for (Pair<String, String> pair : sortedPairs) {
            enumeratedPairs.add(Pair.of(pair.getFirst().replace("{0}", String.valueOf(i++)), pair.getSecond()));
        }

        List<List<InlineKeyboardButton>> newButtons = enumeratedPairs.stream()
                .map(pair -> createButton(pair.getFirst(), pair.getSecond()))
                .map(List::of)
                .toList();

        return generateMarkup(newButtons);
    }

    Pair<String, String> convertToStringPair(TimeSlotDto timeslots) {
        String start = convertTimeToString(timeslots.getStartTime());
        String end = convertTimeToString(timeslots.getEndTime());
        String buttonText = buttonTextPattern
                .replace("{1}", start)
                .replace("{2}", end)
                .replace("{3}", individualLabel);
        return Pair.of(buttonText, rmTimeSlotCallback + timeslots.getId());
    }

    Pair<String, String> convertToStringPair(GroupTimeSlotDto timeslots) {
        String start = convertTimeToString(timeslots.getStartTime());
        String end = convertTimeToString(timeslots.getEndTime());
        String buttonText = buttonTextPattern
                .replace("{1}", start)
                .replace("{2}", end)
                .replace("{3}", groupLabel);
        return Pair.of(buttonText, rmGroupTimeSlotCallback + timeslots.getId());
    }

    private String convertTimeToString(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }
}
