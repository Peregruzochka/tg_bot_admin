package ru.peregruzochka.tg_bot_admin.handler.add_time_slot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.peregruzochka.tg_bot_admin.handler.BaseAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "attr.choose-minute")
public class ChooseMinuteAttribute extends BaseAttribute {
    private int startMinute;
    private int endMinute;
    private String chooseMinuteCallback;

    public String generateText(String teacherName, String localDate) {
        return super.getText().replace("{0}", teacherName).replace("{1}", localDate);
    }

    public InlineKeyboardMarkup generateChooseMinuteMarkup(int hour) {
        return super.generateMarkup(generateMinuteButtons(hour));
    }

    private List<List<InlineKeyboardButton>> generateMinuteButtons(int hour) {
        return IntStream.range(startMinute, endMinute)
                .filter(i -> i % 5 == 0)
                .mapToObj(min -> super.createButton(
                        createChooseMinuteButtonText(hour, min),
                        createChooseMinuteCallback(min)))
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> {
                            List<List<InlineKeyboardButton>> result = new ArrayList<>();
                            for (int i = 0; i < list.size(); i += 2) {
                                result.add(list.subList(i, Math.min(i + 2, list.size())));
                            }
                            return result;
                        }
                ));
    }

    private String createChooseMinuteButtonText(int hour, int minute) {
        return String.format("%d:%02d", hour, minute);
    }

    private String createChooseMinuteCallback(int minute) {
        return chooseMinuteCallback + minute;
    }
}
