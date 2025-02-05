package ru.peregruzochka.tg_bot_admin.cache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Getter
@Setter
public class TeacherSaver {
    private UUID teacherId;
}
