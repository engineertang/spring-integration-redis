package com.springboot.controller;

import static com.iaspec.ecph.constant.EcphMessageEventTypeConstant.PAYMENT_PROCESSOR_STATUS_REPORT_CREATED;
import static com.iaspec.ecph.constant.EcphMessageEventTypeConstant.SUBMIT_PAYMENT_STATUS_REPORT;

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
        queueService.pushPaymentStatusReport(SUBMIT_PAYMENT_STATUS_REPORT);

        System.out.println("Send message successfully");
        // 等待5秒，去redis里取值
        // 测试可以根据redis list里新增一条数据paymentDTO.settlementAmount = original - 270
        // redisTemplate
    }

    @Test
    void pushPayment002() {
        queueService.pushPayment(SUBMIT_PAYMENT_STATUS_REPORT);

        System.out.println("Send message successfully");
        // 等待5秒，去redis里取值
        // 测试可以根据redis list里新增一条数据paymentDTO.settlementAmount = original - 270
        // redisTemplate
    }

}