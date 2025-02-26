package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.peregruzochka.tg_bot_admin.handler.BaseAttribute;

@Component
@ConfigurationProperties(prefix = "attr.add-reg-send-to-backend")
public class AddRegSendToBackendAttribute extends BaseAttribute {
}
