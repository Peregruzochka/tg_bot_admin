package ru.peregruzochka.tg_bot_admin.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.peregruzochka.tg_bot_admin.dto.CancelDto;
import ru.peregruzochka.tg_bot_admin.dto.GroupLessonDto;
import ru.peregruzochka.tg_bot_admin.dto.GroupRegistrationDto;
import ru.peregruzochka.tg_bot_admin.dto.GroupTimeSlotDto;
import ru.peregruzochka.tg_bot_admin.dto.LessonDto;
import ru.peregruzochka.tg_bot_admin.dto.RegistrationDto;
import ru.peregruzochka.tg_bot_admin.dto.TeacherDto;
import ru.peregruzochka.tg_bot_admin.dto.TimeSlotDto;
import ru.peregruzochka.tg_bot_admin.dto.UserDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@FeignClient(name = "bot-backend", url = "${bot-backend.host}:${bot-backend.port}")
public interface BotBackendClient {

    @PostMapping("/timeslots")
    TimeSlotDto addTimeSlot(@RequestBody TimeSlotDto timeSlotDto);

    @GetMapping("/timeslots/by-date")
    List<TimeSlotDto> getTeacherTimeSlotsByDate(@RequestParam("teacher-id") UUID teacherId, @RequestParam LocalDate date);

    @GetMapping("/timeslots/available-by-date")
    List<TimeSlotDto> getTeacherAvailableTimeSlotsByDate(@RequestParam("teacher-id") UUID teacherId, @RequestParam LocalDate date);

    @GetMapping("/timeslots/available-next-month-search")
    List<TimeSlotDto> getTeacherAvailableTimeSlotsInNextMonth(@RequestParam("teacher-id") UUID teacherId);

    @DeleteMapping("/timeslots/{timeslot-id}")
    void deleteTimeSlot(@PathVariable("timeslot-id") UUID timeslotId);

    @GetMapping("/timeslots/{timeslot-id}")
    TimeSlotDto getTimeSlot(@PathVariable("timeslot-id") UUID timeslotId);

    @PostMapping("/group-timeslots")
    GroupTimeSlotDto addGroupTimeSlot(@RequestParam("teacher-id") UUID teacherId,
                                      @RequestParam("group-lesson-id") UUID groupLessonId,
                                      @RequestParam("start-time") LocalDateTime start);

    @GetMapping("/group-timeslots/next-month-search")
    List<GroupTimeSlotDto> getTeacherGroupTimeSlotInNextMonth(@RequestParam("teacher-id") UUID teacherId);

    @GetMapping("/group-timeslots/available-by-date")
    List<GroupTimeSlotDto> getAvailableGroupTimeSlotsByDate(@RequestParam("teacher-id") UUID teacherId,
                                                            @RequestParam("date") LocalDate date);

    @GetMapping("/group-timeslots/{group-timeslot-id}")
    GroupTimeSlotDto getGroupTimeSlot(@PathVariable("group-timeslot-id") UUID groupTimeslotId);

    @DeleteMapping("/group-timeslots/{group-timeslot-id}")
    void deleteGroupTimeSlot(@PathVariable("group-timeslot-id") UUID groupTimeslotId);

    @GetMapping("/group-timeslots/by-date")
    List<GroupTimeSlotDto> getTeacherGroupTimeSlotsByDate(@RequestParam("teacher-id") UUID teacherId, @RequestParam("date") LocalDate date);

    @GetMapping("/teachers")
    List<TeacherDto> getAllTeachers();

    @GetMapping("/teachers/group-teachers")
    List<TeacherDto> getGroupTeacher();

    @GetMapping("/registrations/search-today")
    List<RegistrationDto> getAllRegistrationsByToday();

    @GetMapping("/registrations/search-by-date")
    List<RegistrationDto> getAllRegistrationsByDate(@RequestParam("date") LocalDate date);

    @PostMapping("/registrations")
    RegistrationDto addRegistration(@RequestBody RegistrationDto registrationDto);

    @PostMapping("/group-registrations")
    GroupRegistrationDto addGroupRegistration(@RequestBody GroupRegistrationDto groupRegistrationDto);

    @GetMapping("/lessons/all")
    List<LessonDto> getAllLessons();

    @GetMapping("/group-lessons/by-teacher")
    List<GroupLessonDto> getGroupLessonsByTeacher(@RequestParam(name = "teacher-id") UUID teacherId);

    @GetMapping("/users/phone")
    UserDto getUserByPhone(@RequestParam(name = "phone") String phone);

    @PostMapping("/cancellations")
    CancelDto addCancel(@RequestBody CancelDto cancelDto);
}
