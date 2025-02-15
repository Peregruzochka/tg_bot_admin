package ru.peregruzochka.tg_bot_admin.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import ru.peregruzochka.tg_bot_admin.redis.CancelEventListener;
import ru.peregruzochka.tg_bot_admin.redis.ConfirmRegistrationEventListener;
import ru.peregruzochka.tg_bot_admin.redis.NewRegistrationEventListener;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final NewRegistrationEventListener newRegistrationEventListener;
    private final ConfirmRegistrationEventListener confirmRegistrationEventListener;
    private final CancelEventListener cancelEventListener;

    @Value("${spring.data.redis-channel.new-registration}")
    private String newRegistrationChannel;

    @Value("${spring.data.redis-channel.confirmed}")
    private String confirmRegistrationChannel;

    @Value("${spring.data.redis-channel.cancel}")
    private String cancelChannel;

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);

        container.addMessageListener(
                new MessageListenerAdapter(newRegistrationEventListener),
                new ChannelTopic(newRegistrationChannel)
        );

        container.addMessageListener(
                new MessageListenerAdapter(confirmRegistrationEventListener),
                new ChannelTopic(confirmRegistrationChannel)
        );

        container.addMessageListener(
                new MessageListenerAdapter(cancelEventListener),
                new ChannelTopic(cancelChannel)
        );

        return container;
    }
}
