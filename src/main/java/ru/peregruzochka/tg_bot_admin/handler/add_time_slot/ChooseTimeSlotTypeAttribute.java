package ru.peregruzochka.tg_bot_admin.handler.add_time_slot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.peregruzochka.tg_bot_admin.handler.BaseAttribute;

@Component
@ConfigurationProperties(prefix = "attr.choose-timeslot-type")
public class ChooseTimeSlotTypeAttribute extends BaseAttribute {

}
