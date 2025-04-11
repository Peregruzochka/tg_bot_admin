package ru.peregruzochka.tg_bot_admin.handler.search_registration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import ru.peregruzochka.tg_bot_admin.dto.GroupRegistrationDto;
import ru.peregruzochka.tg_bot_admin.dto.GroupTimeSlotDto;
import ru.peregruzochka.tg_bot_admin.dto.RegistrationDto;
import ru.peregruzochka.tg_bot_admin.dto.TeacherDto;
import ru.peregruzochka.tg_bot_admin.handler.BaseAttribute;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "attr.show-reg-by-day")
public class ShowRegistrationByDayAttribute extends BaseAttribute {
    private String registrationPattern;
    private String groupRegistrationPattern;
    private String groupSlotPattern;

    public String generateText(LocalDate localDate,
                               List<RegistrationDto> registrations,
                               List<GroupRegistrationDto> groupRegistrations) {

        return text
                .replace("{0}", localDate.toString())
                .replace("{1}", generateRegistrationText(registrations, groupRegistrations));
    }

    private String generateRegistrationText(List<RegistrationDto> registrations, List<GroupRegistrationDto> groupRegistrations) {
        Map<TeacherDto, List<Pair<LocalDateTime, String>>> teachersRegistrations = new HashMap<>();

        registrations.stream()
                .map(reg -> Pair.of(reg.getTeacher(), createRegPair(reg)))
                .forEach(pair -> teachersRegistrations.computeIfAbsent(pair.getFirst(), k -> new ArrayList<>()).add(pair.getSecond()));

        Map<TeacherDto, Map<GroupTimeSlotDto, List<GroupRegistrationDto>>> groupRegsByTeacherByTimeSlot = new HashMap<>();
        for (GroupRegistrationDto groupReg : groupRegistrations) {
            TeacherDto teacher = groupReg.getTimeSlot().getTeacher();
            GroupTimeSlotDto timeslot = groupReg.getTimeSlot();
            groupRegsByTeacherByTimeSlot
                    .computeIfAbsent(teacher, k -> new HashMap<>())
                    .computeIfAbsent(timeslot, k -> new ArrayList<>())
                    .add(groupReg);
        }

        groupRegsByTeacherByTimeSlot.forEach((teacher, groupRegsByTimeSlot) ->
                groupRegsByTimeSlot.forEach((timeslot, groupRegs) -> {
                    Pair<LocalDateTime, String> group = createRegPair(timeslot, groupRegs);
                    teachersRegistrations.computeIfAbsent(teacher, k -> new ArrayList<>()).add(group);
                }));


        StringBuilder allReg = new StringBuilder();
        teachersRegistrations.entrySet().stream()
                .map(entry -> generateTeacherRegList(entry.getKey().getName(), entry.getValue()))
                .forEach(allReg::append);

        return allReg.toString();
    }


    private String generateTeacherRegList(String teacherName, List<Pair<LocalDateTime, String>> pairs) {
        StringBuilder teacherRegList = new StringBuilder();
        teacherRegList.append(teacherName).append("\n");

        pairs.stream()
                .sorted(Comparator.comparing(Pair::getFirst))
                .map(Pair::getSecond)
                .forEach(pair -> teacherRegList.append(pair).append("\n"));
        return teacherRegList.toString();
    }


    private Pair<LocalDateTime, String> createRegPair(RegistrationDto registration) {
        LocalDateTime start = registration.getSlot().getStartTime();
        LocalDateTime end = registration.getSlot().getEndTime();
        String time = convertTimeToString(start, end);
        String lesson = registration.getLesson().getName();
        String childName = registration.getChild().getName();
        String childBirthday = registration.getChild().getBirthday();
        String parent = registration.getUser().getName();
        String phone = registration.getUser().getPhone();

        String regText = registrationPattern
                .replace("{1}", time)
                .replace("{2}", lesson)
                .replace("{3}", childName)
                .replace("{4}", childBirthday)
                .replace("{5}", parent)
                .replace("{6}", phone);

        return Pair.of(start, regText);
    }

    private Pair<LocalDateTime, String> createRegPair(GroupTimeSlotDto timeslot, List<GroupRegistrationDto> groupRegsByTimeSlot) {
        LocalDateTime start = timeslot.getStartTime();
        LocalDateTime end = timeslot.getEndTime();
        String time = convertTimeToString(start, end);
        StringBuilder regText = getStringBuilder(timeslot, time);

        groupRegsByTimeSlot.stream()
                .map(this::generateGroupRegText)
                .forEach(regText::append);

        return Pair.of(start, regText.toString());
    }

    private StringBuilder getStringBuilder(GroupTimeSlotDto timeslot, String time) {
        String lesson = timeslot.getGroupLesson().getName();
        String amount = String.valueOf(timeslot.getRegistrationAmount());
        String capacity = String.valueOf(timeslot.getGroupLesson().getGroupSize());

        String timeSlotText = groupSlotPattern
                .replace("{1}", time)
                .replace("{2}", lesson)
                .replace("{3}", amount)
                .replace("{4}", capacity);

        StringBuilder regText = new StringBuilder();
        regText.append(timeSlotText);
        return regText;
    }

    private String generateGroupRegText(GroupRegistrationDto groupReg) {
        String childName = groupReg.getChild().getName();
        String childBirthday = groupReg.getChild().getBirthday();
        String parent = groupReg.getUser().getName();
        String phone = groupReg.getUser().getPhone();

        return groupRegistrationPattern
                .replace("{0}", childName)
                .replace("{1}", childBirthday)
                .replace("{2}", parent)
                .replace("{3}", phone);
    }

    private String convertTimeToString(LocalDateTime startDate, LocalDateTime endDate) {
        String start = startDate.format(DateTimeFormatter.ofPattern("HH:mm"));
        String end = endDate.format(DateTimeFormatter.ofPattern("HH:mm"));
        return start + " - " + end;
    }

}
