package ru.peregruzochka.tg_bot_admin.cache;

import org.springframework.stereotype.Component;
import ru.peregruzochka.tg_bot_admin.dto.TimeSlotDto;

import java.util.List;
import java.util.UUID;

@Component
public class TeacherTimeSlotPool extends Cache<UUID, List<TimeSlotDto>> {
}
