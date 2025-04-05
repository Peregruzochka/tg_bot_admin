package ru.peregruzochka.tg_bot_admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupTimeSlotDto {
    private UUID id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private GroupLessonDto groupLesson;
    private TeacherDto teacher;
    private int registrationAmount;
}

