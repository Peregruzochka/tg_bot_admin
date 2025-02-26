package ru.peregruzochka.tg_bot_admin.cache;

import org.springframework.stereotype.Component;
import ru.peregruzochka.tg_bot_admin.dto.LessonDto;

import java.util.UUID;

@Component
public class LessonDtoCache extends Cache<UUID, LessonDto>{
}
