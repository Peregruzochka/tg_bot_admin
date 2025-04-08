package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.peregruzochka.tg_bot_admin.dto.ChildDto;
import ru.peregruzochka.tg_bot_admin.handler.BaseAttribute;

import java.util.List;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "attr.add-reg-group-change-child")
public class AddRegGroupChangeChildAttribute extends BaseAttribute {
    private String childCallback;

    public InlineKeyboardMarkup generateChildMarkup(List<ChildDto> children) {
        List<List<InlineKeyboardButton>> childButtons = children.stream()
                .map(child -> createButton(child.getName(), childCallback + child.getId()))
                .map(List::of)
                .toList();

        return generateMarkup(childButtons);
    }
}
