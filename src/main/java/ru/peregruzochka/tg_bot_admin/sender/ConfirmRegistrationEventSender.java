package ru.peregruzochka.tg_bot_admin.sender;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.AdminChatIdSaver;
import ru.peregruzochka.tg_bot_admin.dto.RegistrationEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class ConfirmRegistrationEventSender {
    private final TelegramBot telegramBot;
    private final AdminChatIdSaver adminChatIdSaver;
    @Value("${attr.confirm-registration-event.text}")
    private String confirmRegistrationEventText;

    public void send(RegistrationEvent registrationEvent) {
        telegramBot.send(createMessageText(registrationEvent), adminChatIdSaver.getChatId());
    }

    private String createMessageText(RegistrationEvent registrationEvent) {
        String text = confirmRegistrationEventText.replace("{0}", registrationEvent.getStartTime().toLocalDate().toString());
        text = text.replace("{1}", convertTimeToString(registrationEvent.getStartTime(), registrationEvent.getEndTime()));
        text = text.replace("{2}", registrationEvent.getTeacherName());
        text = text.replace("{3}", registrationEvent.getLessonName());
        text = text.replace("{4}", registrationEvent.getUserName());
        text = text.replace("{5}", registrationEvent.getRegistrationType());
        text = text.replace("{6}", registrationEvent.getChildName());
        text = text.replace("{7}", registrationEvent.getChildrenBirthday());
        return text;
    }

    private String convertTimeToString(LocalDateTime startTime, LocalDateTime endTime) {
        String start = startTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        String end = endTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        return start + " - " + end;
    }
}
