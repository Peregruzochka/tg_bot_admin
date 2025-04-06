package ru.peregruzochka.tg_bot_admin.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import ru.peregruzochka.tg_bot_admin.dto.GroupRegistrationEvent;
import ru.peregruzochka.tg_bot_admin.sender.NewGroupRegistrationEventSender;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewGroupRegistrationEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final NewGroupRegistrationEventSender newGroupRegistrationEventSender;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String messageBody = new String(message.getBody());
        log.info("New group registration event: {}", messageBody);
        try {
            GroupRegistrationEvent registrationEvent = objectMapper.readValue(messageBody, GroupRegistrationEvent.class);
            newGroupRegistrationEventSender.send(registrationEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
