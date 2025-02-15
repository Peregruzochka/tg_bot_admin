package ru.peregruzochka.tg_bot_admin.sender;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.AdminChatIdSaver;
import ru.peregruzochka.tg_bot_admin.dto.CancelEvent;
import ru.peregruzochka.tg_bot_admin.dto.RegistrationEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class CancelEventSender {
    private final TelegramBot telegramBot;
    private final AdminChatIdSaver adminChatIdSaver;

    @Value("${attr.cancel-event.text}")
    private String text;

    public void send(CancelEvent cancelEvent) {
        telegramBot.send(
                createMessageText(cancelEvent.getRegistrationEvent(), cancelEvent.getCaseDescription()),
                adminChatIdSaver.getChatId()
        );
    }

    private String createMessageText(RegistrationEvent registrationEvent, String caseDescription) {
        return text
                .replace("{0}", registrationEvent.getStartTime().toLocalDate().toString())
                .replace("{1}", convertTimeToString(registrationEvent.getStartTime(), registrationEvent.getEndTime()))
                .replace("{2}", registrationEvent.getTeacherName())
                .replace("{3}", registrationEvent.getLessonName())
                .replace("{4}", registrationEvent.getUserName())
                .replace("{5}", registrationEvent.getRegistrationType())
                .replace("{6}", registrationEvent.getChildName())
                .replace("{7}", registrationEvent.getChildrenBirthday())
                .replace("{8}", caseDescription);
    }

    private String convertTimeToString(LocalDateTime startTime, LocalDateTime endTime) {
        String start = startTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        String end = endTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        return start + " - " + end;
    }
}

