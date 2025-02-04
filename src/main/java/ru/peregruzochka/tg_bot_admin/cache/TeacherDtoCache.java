package ru.peregruzochka.tg_bot_admin.cache;

import org.springframework.stereotype.Component;
import ru.peregruzochka.tg_bot_admin.dto.TeacherDto;

import java.util.UUID;

@Component
public class TeacherDtoCache extends Cache<UUID, TeacherDto> {
}
