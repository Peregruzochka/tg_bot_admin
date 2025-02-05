package ru.peregruzochka.tg_bot_admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegistrationDto {
    private UUID id;
    private Long telegramId;
    private UserDto user;
    private ChildDto child;
    private LessonDto lesson;
    private TeacherDto teacher;
    private RegistrationType type;
    private TimeSlotDto slot;

    public enum RegistrationType {
        NEW_USER,
        REGULAR_USER,
        RE_REGISTRATION,
        CANCEL
    }
}

