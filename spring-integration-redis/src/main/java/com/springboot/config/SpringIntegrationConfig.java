package com.springboot.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@ImportResource("classpath:WEB-INF/application-outbound.xml")
@AutoConfigureAfter(RedisConfig.class)
@Configuration
public class SpringIntegrationConfig {
}