package ru.peregruzochka.tg_bot_admin.cache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Getter
@Setter
public class LocalDateSaver {
    private LocalDate date;
}
