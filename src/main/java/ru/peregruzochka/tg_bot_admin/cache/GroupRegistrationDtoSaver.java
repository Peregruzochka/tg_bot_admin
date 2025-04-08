package ru.peregruzochka.tg_bot_admin.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.peregruzochka.tg_bot_admin.dto.GroupRegistrationDto;

@Component
@Getter
@Setter
@RequiredArgsConstructor
public class GroupRegistrationDtoSaver {
    private GroupRegistrationDto registration;
}
