package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.peregruzochka.tg_bot_admin.dto.GroupRegistrationDto;
import ru.peregruzochka.tg_bot_admin.dto.GroupTimeSlotDto;
import ru.peregruzochka.tg_bot_admin.dto.UserDto;
import ru.peregruzochka.tg_bot_admin.handler.BaseAttribute;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "attr.add-reg-group-finish-attribute")
public class AddRegGroupFinishAttribute extends BaseAttribute {
    private String changeChildButton;
    private String changeChildCallback;

    public String generateText(GroupRegistrationDto registration) {
        String date = registration.getTimeSlot().getStartTime().toLocalDate().toString();
        String time = slotToString(registration.getTimeSlot());
        String teacher = registration.getTimeSlot().getTeacher().getName();
        String lesson = registration.getTimeSlot().getGroupLesson().getName();
        String clientName = registration.getUser().getName();
        String clientPhone = registration.getUser().getPhone();
        String childName = registration.getChild().getName();
        String childBirthday = registration.getChild().getBirthday();
        String amount = String.valueOf(registration.getTimeSlot().getRegistrationAmount());
        String capacity = String.valueOf(registration.getTimeSlot().getGroupLesson().getGroupSize());

        return super.getText()
                .replace("{0}", date)
                .replace("{1}", time)
                .replace("{2}", teacher)
                .replace("{3}", lesson)
                .replace("{4}", clientName)
                .replace("{5}", clientPhone)
                .replace("{6}", childName)
                .replace("{7}", childBirthday)
                .replace("{8}", amount)
                .replace("{9}", capacity);
    }

    public InlineKeyboardMarkup generateFinishMarkup(UserDto user) {
        if (user.getChildren() != null && user.getChildren().size() > 1) {
            InlineKeyboardButton button = createButton(changeChildButton, changeChildCallback);
            List<List<InlineKeyboardButton>> buttons = new ArrayList<>(createMarkup().getKeyboard());

            buttons.add(1, List.of(button));

            return new InlineKeyboardMarkup(buttons);
        } else {
            return createMarkup();
        }
    }


    private String slotToString(GroupTimeSlotDto timeSlotDto) {
        String startTime = timeSlotDto.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        String endTime = timeSlotDto.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        return startTime + " - " + endTime;
    }
}

