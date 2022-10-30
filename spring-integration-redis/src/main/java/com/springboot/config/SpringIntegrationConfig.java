package com.springboot.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@ImportResource("classpath:application-redis-int.xml")
@AutoConfigureAfter(RedisConfig.class)
@Configuration
public class SpringIntegrationConfig {


}


