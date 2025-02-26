package ru.peregruzochka.tg_bot_admin.cache;

import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Setter
public abstract class Flag {
    private boolean flag;

    public boolean isTrue() {
        return flag;
    }
}
