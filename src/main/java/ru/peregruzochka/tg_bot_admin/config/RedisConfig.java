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
import ru.peregruzochka.tg_bot_admin.redis.ConfirmGroupRegistrationEventListener;
import ru.peregruzochka.tg_bot_admin.redis.ConfirmRegistrationEventListener;
import ru.peregruzochka.tg_bot_admin.redis.GroupCancelEventListener;
import ru.peregruzochka.tg_bot_admin.redis.NewGroupRegistrationEventListener;
import ru.peregruzochka.tg_bot_admin.redis.NewRegistrationEventListener;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final NewRegistrationEventListener newRegistrationEventListener;
    private final ConfirmRegistrationEventListener confirmRegistrationEventListener;
    private final ConfirmGroupRegistrationEventListener confirmGroupRegistrationEventListener;
    private final CancelEventListener cancelEventListener;
    private final NewGroupRegistrationEventListener newGroupRegistrationEventListener;
    private final GroupCancelEventListener groupCancelEventListener;

    @Value("${spring.data.redis-channel.new-registration}")
    private String newRegistrationChannel;

    @Value("${spring.data.redis-channel.confirmed}")
    private String confirmRegistrationChannel;

    @Value("${spring.data.redis-channel.group-confirmed}")
    private String confirmGroupRegistrationChannel;

    @Value("${spring.data.redis-channel.cancel}")
    private String cancelChannel;

    @Value("${spring.data.redis-channel.new-group-registration}")
    private String newGroupRegistrationChannel;

    @Value("${spring.data.redis-channel.group-cancel}")
    private String groupCancelChannel;

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
                new MessageListenerAdapter(confirmGroupRegistrationEventListener),
                new ChannelTopic(confirmGroupRegistrationChannel)
        );

        container.addMessageListener(
                new MessageListenerAdapter(cancelEventListener),
                new ChannelTopic(cancelChannel)
        );

        container.addMessageListener(
                new MessageListenerAdapter(newGroupRegistrationEventListener),
                new ChannelTopic(newGroupRegistrationChannel)
        );

        container.addMessageListener(
                new MessageListenerAdapter(groupCancelEventListener),
                new ChannelTopic(groupCancelChannel)
        );

        return container;
    }
}
