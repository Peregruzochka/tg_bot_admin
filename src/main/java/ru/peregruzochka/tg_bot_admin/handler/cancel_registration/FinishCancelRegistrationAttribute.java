package ru.peregruzochka.tg_bot_admin.handler.cancel_registration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.peregruzochka.tg_bot_admin.handler.BaseAttribute;

@Component
@ConfigurationProperties(prefix = "attr.finish-cancel-registration")
public class FinishCancelRegistrationAttribute extends BaseAttribute {
}
