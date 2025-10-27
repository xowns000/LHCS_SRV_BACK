package kr.co.hkcloud.palette3.config.redis;

import kr.co.hkcloud.palette3.core.chat.redis.app.TalkRedisChatSubscriberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ChatRedisConfig {
    /**
     * 단일 Topic 사용을 위한 Bean 설정
     * @desc 기본 채널 생성 (서버 기동 시 디폴트로 paletteChatMessageQueue 채널에 생성)
     */
    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic("paletteChatMessageQueue");
    }

    /**
     * redis에 발행(publish)된 메시지 처리를 위한 리스너 설정
     * @desc 메시지 리스너, 토픽 등록 컨테이너
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory,
        MessageListenerAdapter listenerAdapter, ChannelTopic channelTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, channelTopic);
        return container;
    }

    /**
     * Redis에서 발행한 메시지를 구독 처리하는 subscriber 설정 추가
     * @desc RedisMessageSubscriber 메세지 리스너로 등록
     */
    @Bean
    public MessageListenerAdapter listenerAdapter(TalkRedisChatSubscriberServiceImpl subscriber) {
        return new MessageListenerAdapter(subscriber, "onSubMessage");
    }

    /**
     * 어플리케이션에서 사용할 redisTemplate 설정
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        return redisTemplate;
    }
}
