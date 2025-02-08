package ru.peregruzochka.tg_bot_admin.cache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class AdminChatIdSaver {
    private Long chatId;
}
