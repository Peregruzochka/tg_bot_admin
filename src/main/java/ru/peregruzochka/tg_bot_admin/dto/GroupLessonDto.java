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
public class GroupLessonDto {
    private UUID id;
    private String name;
    private String description;
    private int groupSize;
    private List<TeacherDto> teachers;
}
