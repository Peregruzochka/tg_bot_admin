package ru.peregruzochka.tg_bot_admin.handler.add_registration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.peregruzochka.tg_bot_admin.handler.BaseAttribute;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "attr.add-reg-input-user-name")
public class AddRegInputUserNameAttribute extends BaseAttribute {

}
