package ru.peregruzochka.tg_bot_admin.cache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.peregruzochka.tg_bot_admin.dto.RegistrationDto;

import java.util.List;

@Component
@Getter
@Setter
public class CancelRegistrationPool {
    private List<RegistrationDto> registrations;
}
