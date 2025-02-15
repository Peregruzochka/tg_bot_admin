package ru.peregruzochka.tg_bot_admin.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import ru.peregruzochka.tg_bot_admin.dto.CancelEvent;
import ru.peregruzochka.tg_bot_admin.sender.CancelEventSender;


@Slf4j
@Component
@RequiredArgsConstructor
public class CancelEventListener implements MessageListener {

    private final CancelEventSender cancelEventSender;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String messageBody = new String(message.getBody());
        log.info("Cancel event received: {}", messageBody);
        try {
            CancelEvent cancelEvent = objectMapper.readValue(messageBody, CancelEvent.class);
            cancelEventSender.send(cancelEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
