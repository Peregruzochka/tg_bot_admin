package ru.peregruzochka.tg_bot_admin.tools;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "auth-user-checker")
public class AuthUserChecker {
    private List<Long> userIds;

    public boolean check(Long chatId) {
        return userIds.contains(chatId);
    }
}
