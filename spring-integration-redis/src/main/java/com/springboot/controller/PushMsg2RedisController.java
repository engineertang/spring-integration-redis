package com.springboot.controller;

import static com.springboot.model.RedisConstant.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.integration.redis.inbound.RedisInboundChannelAdapter;
import org.springframework.integration.redis.inbound.RedisQueueMessageDrivenEndpoint;
import org.springframework.integration.redis.outbound.RedisQueueOutboundChannelAdapter;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.service.RedisQueueService;

@RestController
@RequestMapping("/api")
public class PushMsg2RedisController {

    @Autowired
    RedisQueueService queueService;
    @Autowired
    RedisTemplate publishRedisTemplate;
    @Autowired
    RedisMessageListenerContainer listenerContainer;
    @Autowired
    MessageListener myListener;
    @Autowired
    RedisQueueMessageDrivenEndpoint myQueueAdapter;


    @PostMapping(path = "/pushmessage" )
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
    public void subscribe() throws InterruptedException {
        System.out.println("subscribe topic");
        System.out.println( myQueueAdapter.isListening());
        myQueueAdapter.start();
        Thread.sleep(5_000);
        System.out.println( myQueueAdapter.isListening());

/*        listenerContainer.addMessageListener(myListener, new PatternTopic(topic));
        System.out.println(Boolean.toString(listenerContainer.isListening()));*/
    }

    @DeleteMapping
    public void unSubscribe() throws Exception {
        System.out.println("unsubscribe topic");
        System.out.println( myQueueAdapter.isListening());
        myQueueAdapter.stop();
        System.out.println( myQueueAdapter.isListening());
        //listenerContainer.stop();
        //System.out.println(Boolean.toString(listenerContainer.isListening()));
    }

    @PutMapping
    public void publish() {
        System.out.println("send message to topic");
        publishRedisTemplate.convertAndSend(topic, "{\"clientSysId\":\"SWIFT\",\"clientReqId\":\"MSG20220725114322066\",\"clientUsrId\":\"SWIFT\",\"eventType\":\"PAYMENT_RECEIVE_NEW_MESSAGE\",\"msgType\":\"pacs.008.001.08\",\"businessService\":\"swift.cbprplus.02\",\"data\":\"\"");
    }

}
