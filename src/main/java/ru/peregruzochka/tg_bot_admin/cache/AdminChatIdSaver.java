package ru.peregruzochka.tg_bot_admin.cache;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.peregruzochka.tg_bot_admin.tools.AuthUserChecker;

@Component
@Getter
@Setter
@RequiredArgsConstructor
public class AdminChatIdSaver {

    private final AuthUserChecker authUserChecker;

    @PostConstruct
    void init() {
        chatId = authUserChecker.getUserIds().get(0);
    }

    private Long chatId;
}
