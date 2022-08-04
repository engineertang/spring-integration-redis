package com.springboot.controller;

import com.iaspec.ecfps.util.JsonHelper;
import com.iaspec.ecph.dto.message.InternalMessage;
import com.iaspec.ecph.payment.dto.PaymentDTO;
import com.iaspec.ecph.payment.dto.PaymentReturnDTO;
import com.springboot.intfc.RedisChannelGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Service
public class RedisQueueService implements QueueService {
    private RedisChannelGateway channelGateway;

    @Autowired
    public RedisQueueService(RedisChannelGateway channelGateway) {
        this.channelGateway = channelGateway;
    }

    @Override
    public void savePaymentReturn(String eventType) {
        String paymentStr = getResourceFile(eventType);

        try {
            PaymentReturnDTO payment = JsonHelper.fromJson(paymentStr, PaymentReturnDTO.class);

            String reqStr = JsonHelper.toJsonIgnoreNulls(payment);
            InternalMessage reqMsg = new InternalMessage();

            reqMsg.setBusinessService(payment.getBusinessService());
            reqMsg.setEventType(eventType);
            reqMsg.setData(reqStr);
            //reqMsg.setOptions(options);

            channelGateway.enqueueString(reqMsg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void savePayment(String eventType) {
        String paymentStr = getResourceFile(eventType);

        try {
            PaymentDTO payment = JsonHelper.fromJson(paymentStr, PaymentDTO.class);

            String reqStr = JsonHelper.toJsonIgnoreNulls(payment);
            InternalMessage reqMsg = new InternalMessage();
            reqMsg.setClientReqId(payment.getClientReqId());
            reqMsg.setClientSysId(payment.getClientSysId());
            reqMsg.setClientUsrId(payment.getClientUsrId());
            reqMsg.setMsgType(payment.getMessageType());
            reqMsg.setBusinessService(payment.getBusinessService());
            reqMsg.setEventType(eventType);
            reqMsg.setData(reqStr);
            //reqMsg.setOptions(options);

            channelGateway.enqueueString(reqMsg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getResourceFile(String eventType) {
        String resourceFile = null;
        switch (eventType){
            case "PAYMENT_PROCESSOR_IW_PAYMENT_RECEIVED" :
                resourceFile = "incomingpayment";
                break;
            case "PAYMENT_PROCESSOR_OW_PAYMENT_CREATED" :
                resourceFile = "incomingpayment";
                break;
            case "PAYMENT_PROCESSOR_OW_PAYMENT_SUBMIT_TO_SWIFT" :
                resourceFile = "incomingpayment";
                break;
            case "PAYMENT_CREATE_OW_RETURN" :
                resourceFile = "paymentReturn";
                break;
        }

        String paymentStr = null;
        try {
            resourceFile = getResourceFile(eventType);
            Resource resource = new ClassPathResource(resourceFile);
            FileReader fileReader = new FileReader(resource.getFile());
            BufferedReader in = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String str;
            while (( str = in.readLine()) != null) {
                stringBuilder.append(str);
            }
            paymentStr = stringBuilder.toString();
        } catch (IOException e){
            System.out.println(e);
        }

        return paymentStr;
    }
}
