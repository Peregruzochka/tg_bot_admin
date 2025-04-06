package ru.peregruzochka.tg_bot_admin.sender;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.AdminChatIdSaver;
import ru.peregruzochka.tg_bot_admin.dto.GroupRegistrationEvent;

import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class NewGroupRegistrationEventSender {

    private final TelegramBot telegramBot;
    private final AdminChatIdSaver adminChatIdSaver;
    @Value("${attr.new-group-registration-event.text}")
    private String text;

    public void send(GroupRegistrationEvent event) {
        telegramBot.send(generateText(event), adminChatIdSaver.getChatId());
    }

    private String generateText(GroupRegistrationEvent event) {
        return text
                .replace("{0}", event.getStartTime().toLocalDate().toString())
                .replace("{1}", generateTime(event))
                .replace("{2}", event.getTeacherName())
                .replace("{3}", event.getLessonName())
                .replace("{4}", event.getUsername())
                .replace("{5}", event.getUserStatus())
                .replace("{6}", event.getChildName())
                .replace("{7}", event.getChildBirthday())
                .replace("{8}", String.valueOf(event.getAmount()))
                .replace("{9}", String.valueOf(event.getCapacity()));
    }


    private String generateTime(GroupRegistrationEvent event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String startTime = event.getStartTime().format(formatter);
        String endTime = event.getEndTime().format(formatter);
        return startTime + " - " + endTime;
    }
}
