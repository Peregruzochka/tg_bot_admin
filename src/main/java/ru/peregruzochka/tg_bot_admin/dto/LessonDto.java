package ru.peregruzochka.tg_bot_admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonDto {
    private UUID id;
    private String name;
    private String description;
    private List<TeacherDto> teachers;
}
