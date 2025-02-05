package ru.peregruzochka.tg_bot_admin.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.peregruzochka.tg_bot_admin.dto.RegistrationDto;
import ru.peregruzochka.tg_bot_admin.dto.TeacherDto;
import ru.peregruzochka.tg_bot_admin.dto.TimeSlotDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@FeignClient(name = "bot-backend", url = "${bot-backend.host}:${bot-backend.port}")
public interface BotBackendClient {

    @PostMapping("/timeslots")
    TimeSlotDto addTimeSlot(@RequestBody TimeSlotDto timeSlotDto);

    @GetMapping("/timeslots/by-date")
    List<TimeSlotDto> getTeacherTimeSlotsByDate(@RequestParam("teacher-id") UUID teacherId, @RequestParam LocalDate date);

    @GetMapping("/timeslots/next-month-search")
    List<TimeSlotDto> getTeacherTimeSlotsInNextMonth(@RequestParam("teacher-id") UUID teacherId);

    @DeleteMapping("timeslots/{timeslot-id}")
    void deleteTimeSlot(@PathVariable("timeslot-id") UUID timeslotId);

    @GetMapping("/teachers")
    List<TeacherDto> getAllTeachers();

    @GetMapping("/registrations/search-today")
    List<RegistrationDto> getAllRegistrationsByToday();
}
