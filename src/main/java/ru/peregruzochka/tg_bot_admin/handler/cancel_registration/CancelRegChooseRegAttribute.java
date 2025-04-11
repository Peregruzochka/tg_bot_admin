package ru.peregruzochka.tg_bot_admin.handler.cancel_registration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.peregruzochka.tg_bot_admin.dto.GroupRegistrationDto;
import ru.peregruzochka.tg_bot_admin.dto.RegistrationDto;
import ru.peregruzochka.tg_bot_admin.handler.BaseAttribute;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "attr.cancel-reg-choose-reg")
public class CancelRegChooseRegAttribute extends BaseAttribute {
    private String registrationText;
    private String cancelRegCallback;
    private String cancelGroupRegCallback;
    private String cancelRegButton;

    public String generateText(List<RegistrationDto> registrations,
                               List<GroupRegistrationDto> groupRegistrations) {
        String date = extractDate(registrations, groupRegistrations).toString();
        String teacher = extractTeacherName(registrations, groupRegistrations);
        String registrationsText = generateRegistrationsText(registrations, groupRegistrations);

        return text
                .replace("{0}", date)
                .replace("{1}", teacher)
                .replace("{2}", registrationsText);
    }

    private String generateRegistrationsText(List<RegistrationDto> registrations, List<GroupRegistrationDto> groupRegistrations) {
        List<Pair<LocalDateTime, String>> pairs = new ArrayList<>();

        registrations.stream()
                .map(this::generateTextPair)
                .forEach(pairs::add);

        groupRegistrations.stream()
                .map(this::generateTextPair)
                .forEach(pairs::add);

        StringBuilder result = new StringBuilder();
        pairs.stream()
                .sorted(Comparator.comparing(Pair::getFirst))
                .forEach(pair -> result.append(pair.getSecond()).append("\n"));

        return result.toString();
    }


    public InlineKeyboardMarkup generateChooseRegMarkup(List<RegistrationDto> registrations, List<GroupRegistrationDto> groupRegistrations) {
        List<Pair<LocalDateTime, InlineKeyboardButton>> pairs = new ArrayList<>();

        registrations.stream()
                .map(this::generateButtonPair)
                .forEach(pairs::add);

        groupRegistrations.stream()
                .map(this::generateButtonPair)
                .forEach(pairs::add);

        List<List<InlineKeyboardButton>> newButtons = pairs.stream()
                .sorted(Comparator.comparing(Pair::getFirst))
                .map(Pair::getSecond)
                .map(List::of)
                .toList();

        return generateMarkup(newButtons);
    }


    private LocalDate extractDate(List<RegistrationDto> registrations, List<GroupRegistrationDto> groupRegistrations) {
        if (!registrations.isEmpty()) {
            return registrations.get(0).getSlot().getStartTime().toLocalDate();
        } else if (!groupRegistrations.isEmpty()) {
            return groupRegistrations.get(0).getTimeSlot().getStartTime().toLocalDate();
        } else {
            throw new IllegalStateException("Can't extract date from registrations list");
        }
    }

    private String extractTeacherName(List<RegistrationDto> registrations, List<GroupRegistrationDto> groupRegistrations) {
        if (!registrations.isEmpty()) {
            return registrations.get(0).getTeacher().getName();
        } else if (!groupRegistrations.isEmpty()) {
            return groupRegistrations.get(0).getTimeSlot().getTeacher().getName();
        }
        throw new IllegalStateException("Can't extract teacher name from registrations list");
    }

    private Pair<LocalDateTime, String> generateTextPair(RegistrationDto registration) {
        String regText = convertByPattern(registration);
        LocalDateTime start = registration.getSlot().getStartTime();
        return Pair.of(start, regText);
    }

    private Pair<LocalDateTime, String> generateTextPair(GroupRegistrationDto registration) {
        String regText = convertByPattern(registration);
        LocalDateTime start = registration.getTimeSlot().getStartTime();
        return Pair.of(start, regText);
    }

    private Pair<LocalDateTime, InlineKeyboardButton> generateButtonPair(RegistrationDto registration) {
        LocalDateTime start = registration.getSlot().getStartTime();
        LocalDateTime end = registration.getSlot().getEndTime();
        String time = convertTime(start, end);
        String childName = registration.getChild().getName();
        String buttonText = cancelRegButton
                .replace("{0}", time)
                .replace("{1}", childName);
        String buttonCallback = cancelRegCallback + registration.getId();
        return Pair.of(start, createButton(buttonText, buttonCallback));
    }

    private Pair<LocalDateTime, InlineKeyboardButton> generateButtonPair(GroupRegistrationDto registration) {
        LocalDateTime start = registration.getTimeSlot().getStartTime();
        LocalDateTime end = registration.getTimeSlot().getEndTime();
        String time = convertTime(start, end);
        String childName = registration.getChild().getName();
        String buttonText = cancelRegButton
                .replace("{0}", time)
                .replace("{1}", childName);
        String buttonCallback = cancelGroupRegCallback + registration.getId();
        return Pair.of(start, createButton(buttonText, buttonCallback));
    }

    private String convertByPattern(GroupRegistrationDto registration) {
        LocalDateTime start = registration.getTimeSlot().getStartTime();
        LocalDateTime end = registration.getTimeSlot().getEndTime();
        String time = convertTime(start, end);
        String lesson = registration.getTimeSlot().getGroupLesson().getName();
        String child = registration.getChild().getName();
        String childBirthday = registration.getChild().getBirthday();
        String parent = registration.getUser().getName();

        return registrationText
                .replace("{0}", time)
                .replace("{1}", lesson)
                .replace("{2}", child)
                .replace("{3}", childBirthday)
                .replace("{4}", parent);
    }

    private String convertByPattern(RegistrationDto registration) {
        LocalDateTime start = registration.getSlot().getStartTime();
        LocalDateTime end = registration.getSlot().getEndTime();
        String time = convertTime(start, end);
        String lesson = registration.getLesson().getName();
        String child = registration.getChild().getName();
        String childBirthday = registration.getChild().getBirthday();
        String parent = registration.getUser().getName();

        return registrationText
                .replace("{0}", time)
                .replace("{1}", lesson)
                .replace("{2}", child)
                .replace("{3}", childBirthday)
                .replace("{4}", parent);
    }

    private String convertTime(LocalDateTime start, LocalDateTime end) {
        String startTime = start.format(DateTimeFormatter.ofPattern("HH:mm"));
        String endTime = end.format(DateTimeFormatter.ofPattern("HH:mm"));
        return startTime + " - " + endTime;
    }


}
