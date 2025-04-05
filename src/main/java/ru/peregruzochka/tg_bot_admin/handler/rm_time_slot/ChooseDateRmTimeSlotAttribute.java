package ru.peregruzochka.tg_bot_admin.handler.rm_time_slot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.peregruzochka.tg_bot_admin.dto.GroupTimeSlotDto;
import ru.peregruzochka.tg_bot_admin.dto.TimeSlotDto;
import ru.peregruzochka.tg_bot_admin.handler.BaseAttribute;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "attr.choose-date-rm-time-slot")
public class ChooseDateRmTimeSlotAttribute extends BaseAttribute {
    private String rmDateCallback;

    public String generateText(String teacherName) {
        return super.getText().replace("{0}", teacherName);
    }

    public InlineKeyboardMarkup generateChooseDateRmTimeSlot(List<TimeSlotDto> timeSlotDtoList, List<GroupTimeSlotDto> groupTimeslots) {
        Set<LocalDate> dates = timeSlotDtoList.stream()
                .map(TimeSlotDto::getStartTime)
                .map(LocalDateTime::toLocalDate)
                .collect(Collectors.toSet());

        Set<LocalDate> groupDates = groupTimeslots.stream()
                .map(GroupTimeSlotDto::getStartTime)
                .map(LocalDateTime::toLocalDate)
                .collect(Collectors.toSet());

        dates.addAll(groupDates);

        List<LocalDate> sortedDates = dates.stream().sorted().toList();

        List<List<InlineKeyboardButton>> newButtons = sortedDates.stream()
                .map(date -> super.createButton(date.toString(), rmDateCallback + date))
                .map(List::of)
                .toList();
        return super.generateMarkup(newButtons);
    }
}

