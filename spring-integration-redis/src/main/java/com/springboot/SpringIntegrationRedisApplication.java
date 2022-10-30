package com.springboot;

import static com.springboot.model.RedisConstant.topic;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;


@SpringBootApplication
public class SpringIntegrationRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringIntegrationRedisApplication.class, args);
    }

    @Bean(name = "myListener")
    MessageListener listener() {
        return (message, pattern) -> System.out.println(" receive message" + new String(message.getBody()));
    }

    @Bean(name = "redisTemplate")
    RedisTemplate publishRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate rt = new RedisTemplate();
        rt.setConnectionFactory(redisConnectionFactory);
        return rt;
    }




    @Bean(name = "listenerContainer")
    RedisMessageListenerContainer listenerContainer(RedisConnectionFactory subscribeRedisConnectionFactory, MessageListener myListener) {
        System.out.printf("subscribe topic");
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.addMessageListener(myListener, new PatternTopic(topic));
        container.setConnectionFactory(subscribeRedisConnectionFactory);

        return container;
    }

}
