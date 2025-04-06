package ru.peregruzochka.tg_bot_admin.sender;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.peregruzochka.tg_bot_admin.bot.TelegramBot;
import ru.peregruzochka.tg_bot_admin.cache.AdminChatIdSaver;
import ru.peregruzochka.tg_bot_admin.dto.GroupCancelEvent;
import ru.peregruzochka.tg_bot_admin.dto.GroupRegistrationEvent;

import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class GroupCancelEventSender {

    private final TelegramBot telegramBot;
    private final AdminChatIdSaver adminChatIdSaver;
    @Value("${attr.group-cancel-event.text}")
    private String text;

    public void send(GroupCancelEvent groupCancelEvent) {
        telegramBot.send(generateText(groupCancelEvent), adminChatIdSaver.getChatId());
    }

    private String generateText(GroupCancelEvent event) {
        String date = event.getGroupRegistrationEvent().getStartTime().toLocalDate().toString();
        String time = generateTime(event.getGroupRegistrationEvent());
        String teacher = event.getGroupRegistrationEvent().getTeacherName();
        String lesson = event.getGroupRegistrationEvent().getLessonName();
        String parent = event.getGroupRegistrationEvent().getUsername();
        String parentStatus = event.getGroupRegistrationEvent().getUserStatus();
        String child = event.getGroupRegistrationEvent().getChildName();
        String childBirthday = event.getGroupRegistrationEvent().getChildBirthday();
        String caseDescription = event.getCaseDescription();
        String amount = String.valueOf(event.getGroupRegistrationEvent().getAmount());
        String capacity = String.valueOf(event.getGroupRegistrationEvent().getCapacity());

        return text
                .replace("{0}", date)
                .replace("{1}", time)
                .replace("{2}", teacher)
                .replace("{3}", lesson)
                .replace("{4}", parent)
                .replace("{5}", parentStatus)
                .replace("{6}", child)
                .replace("{7}", childBirthday)
                .replace("{8}", caseDescription)
                .replace("{9}", amount)
                .replace("{10}", capacity);
    }

    private String generateTime(GroupRegistrationEvent event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String startTime = event.getStartTime().format(formatter);
        String endTime = event.getEndTime().format(formatter);
        return startTime + " - " + endTime;
    }


}
