package ru.peregruzochka.tg_bot_admin.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import ru.peregruzochka.tg_bot_admin.dto.RegistrationEvent;
import ru.peregruzochka.tg_bot_admin.sender.ConfirmRegistrationEventSender;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConfirmRegistrationEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final ConfirmRegistrationEventSender confirmRegistrationEventSender;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String messageBody = new String(message.getBody());
        log.info("New registration event: {}", messageBody);
        try {
            RegistrationEvent registrationEvent = objectMapper.readValue(messageBody, RegistrationEvent.class);
            confirmRegistrationEventSender.send(registrationEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
