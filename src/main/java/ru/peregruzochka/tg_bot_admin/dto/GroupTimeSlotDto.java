package ru.peregruzochka.tg_bot_admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
public class GroupTimeSlotDto {
    private UUID id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @EqualsAndHashCode.Exclude
    private GroupLessonDto groupLesson;
    @EqualsAndHashCode.Exclude
    private TeacherDto teacher;
    @EqualsAndHashCode.Exclude
    private int registrationAmount;
}

