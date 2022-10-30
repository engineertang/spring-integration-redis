package com.springboot.controller;

import com.springboot.SpringIntegrationRedisApplicationTests;
import com.springboot.service.RedisQueueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RedisQueueServiceImplTest extends SpringIntegrationRedisApplicationTests {

    @Autowired
    RedisQueueService queueService;

    @Test
    void pushPaymentReturn() {
        queueService.pushPaymentReturn("PAYMENT_CREATE_OW_RETURN");
    }

    @Test
    void pushPaymentStatusReport() {
        queueService.pushPaymentStatusReport("PAYMENT_CREATE_OW_RETURN");

        // 等待5秒，去redis里取值
        // 测试可以根据redis list里新增一条数据paymentDTO.settlementAmount = original - 270
        // redisTemplate
    }
}