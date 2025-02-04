package ru.peregruzochka.tg_bot_admin.handler.start;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.peregruzochka.tg_bot_admin.handler.BaseAttribute;

@Component
@ConfigurationProperties(prefix = "attr.start")
public class StartAttribute extends BaseAttribute {
}
