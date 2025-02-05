package ru.peregruzochka.tg_bot_admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ChildDto {
    private UUID id;
    private String name;
    private String birthday;
}
