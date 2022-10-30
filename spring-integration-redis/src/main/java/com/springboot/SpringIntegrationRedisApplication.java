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

	@Bean(name = "switchListener")
	MessageListener listener(){
		return (message, pattern) -> System.out.printf("receive message" + new String(message.getBody()));
	}

	@Bean
	RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate rt = new RedisTemplate();
		rt.setConnectionFactory(redisConnectionFactory);
		return rt;
	}

	@Bean
	ApplicationRunner pubSub(RedisTemplate<String, String> rt){
		ApplicationRunner titledRunner = ( args -> rt.convertAndSend(topic, "Hello world"));
		return titledRunner;
	}

	@Bean(name = "openRegister")
	RedisMessageListenerContainer listenerContainer(RedisConnectionFactory redisConnectionFactory, MessageListener switchListener) {
		System.out.printf("subscribe topic");

		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(redisConnectionFactory);
		container.addMessageListener(switchListener, new PatternTopic(topic));
		return container;
	}

/*	@Bean(name = "closeRegister")
	RedisMessageListenerContainer closeContainer(RedisConnectionFactory redisConnectionFactory) {
		System.out.printf("unsubscribe topic");

		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(redisConnectionFactory);
		container.removeMessageListener(listener, new PatternTopic(topic));
		return container;
	}*/

}
