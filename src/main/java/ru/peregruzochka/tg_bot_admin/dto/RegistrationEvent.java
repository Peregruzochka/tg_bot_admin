package ru.peregruzochka.tg_bot_admin.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegistrationEvent {
    private UUID registrationId;
    private Long telegramId;
    private String userName;
    private String childName;
    private String childrenBirthday;
    private String teacherName;
    private String registrationType;
    private String lessonName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
