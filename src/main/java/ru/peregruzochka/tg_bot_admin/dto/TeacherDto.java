package ru.peregruzochka.tg_bot_admin.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDto {
    private UUID id;
    private String name;
    private UUID imageID;
    private boolean hidden;
}
