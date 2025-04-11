package ru.peregruzochka.tg_bot_admin.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TeacherDto {
    private UUID id;
    private String name;
    @EqualsAndHashCode.Exclude
    private UUID imageID;
    @EqualsAndHashCode.Exclude
    private boolean hidden;
}
