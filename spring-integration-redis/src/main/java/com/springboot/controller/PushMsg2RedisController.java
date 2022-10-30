package com.springboot.controller;

import static com.springboot.model.RedisConstant.topic;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.service.RedisQueueService;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@RestController
@RequestMapping("/api/pushmessage")
public class PushMsg2RedisController  {

    @Autowired
    RedisQueueService queueService;
    @Autowired
    RedisConnectionFactory redisConnectionFactory;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RedisMessageListenerContainer openRegister;
    @Autowired
    MessageListener switchListener;

    @PostMapping
    public void sendStudentInformation(@RequestParam String eventType) {
        System.out.printf("push internal message of event type %1$s in redis %n", eventType);

        switch (eventType) {
            // pacs.008
            case "PAYMENT_PROCESSOR_IW_PAYMENT_RECEIVED":
            case "PAYMENT_PROCESSOR_OW_PAYMENT_CREATED":
            case "PAYMENT_PROCESSOR_OW_PAYMENT_SUBMIT_TO_SWIFT":
            case "OUTWARD_PAYMENT_DELETED":
                queueService.pushPayment(eventType);
                break;

            // pacs.004
            case "PAYMENT_CREATE_OW_RETURN":
            case "PAYMENT_PROCESSOR_OW_RETURN_CONFIRMED":
            case "PAYMENT_PROCESSOR_OW_RETURN_SUBMIT_TO_SWIFT":
            case "PAYMENT_PROCESSOR_PAYMENT_STATUS_REPORT_CONFIRMED":
                queueService.pushPaymentReturn(eventType);
                break;

            // payment status report pacs.002
			/*	queueService.pushPaymentStatusReport(eventType);
				break;*/

            // instructing agent send cancellation msg camt.056
            case "PAYMENT_PROCESSOR_OUTWARD_CANCELLATION_RECEIVED":
                break;
            // 向下游agent foward send cancellation message to instructed agent
            case "PAYMENT_PROCESSOR_OUTWARD_CANCELLATION_APPROVED":
                break;
            // original payment is settled but not yet effected 已经发了状态更新,需要删掉已经提交的msg
            case "PAYMENT_PROCESSOR_OUTWARD_PAYMENT_DELETED":
                break;

            case "PAYMENT_PROCESSOR_OUTWARD_RESOLUTION_APPROVED":
                break;

        }
    }

    @GetMapping
    public void subscribe() {
        System.out.printf("subscribe topic");
        openRegister.addMessageListener(switchListener,  new PatternTopic(topic));
        openRegister.start();
        System.out.printf(Boolean.toString( openRegister.isListening()));
    }

    @DeleteMapping
    public void unSubscribe() throws Exception {
        System.out.printf("unsubscribe topic");
        openRegister.removeMessageListener(switchListener);
        System.out.printf(Boolean.toString( openRegister.isListening()));
    }

    @PutMapping
    public void pubSub(){
        System.out.printf("send topic");
        redisTemplate.convertAndSend(topic, "Hello world");
    }

}
