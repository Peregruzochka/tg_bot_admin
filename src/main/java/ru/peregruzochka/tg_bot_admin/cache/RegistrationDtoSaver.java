package ru.peregruzochka.tg_bot_admin.cache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.peregruzochka.tg_bot_admin.dto.RegistrationDto;

@Getter
@Setter
@Component
public class RegistrationDtoSaver {
    private RegistrationDto registrationDto;
}
