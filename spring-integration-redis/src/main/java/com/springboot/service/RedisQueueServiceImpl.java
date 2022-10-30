package com.springboot.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.iaspec.ecfps.util.JsonHelper;
import com.iaspec.ecph.dto.message.InternalMessage;
import com.iaspec.ecph.payment.dto.FIToFIPaymentStatusReportDTO;
import com.iaspec.ecph.payment.dto.PaymentDTO;
import com.springboot.intfc.RedisChannelGateway;

@Service
public class RedisQueueServiceImpl implements RedisQueueService {
    private final RedisChannelGateway channelGateway;

    @Autowired
    public RedisQueueServiceImpl(RedisChannelGateway channelGateway) {
        this.channelGateway = channelGateway;
    }

    // pacs.004 message
    @Override
    public void pushPaymentReturn(String eventType) {

        String paymentStr = getResourceFile(eventType);

        try {
            PaymentDTO payment = JsonHelper.fromJson(paymentStr, PaymentDTO.class);

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

    // pacs.008 msg
    @Override
    public void pushPayment(String eventType) {
        String paymentStr = getResourceFile(eventType);

        try {
            PaymentDTO payment = JsonHelper.fromJson(paymentStr, PaymentDTO.class);
            // set different test case
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

    @Override
    public void pushPaymentStatusReport(String eventType) {
        String strData = getResourceFile(eventType);
        FIToFIPaymentStatusReportDTO reportDTO = JsonHelper.fromJson(strData, FIToFIPaymentStatusReportDTO.class);

        String reqStr = JsonHelper.toJsonIgnoreNulls(reportDTO);
        InternalMessage reqMsg = new InternalMessage();
        reqMsg.setBusinessService(reportDTO.getBusinessService());
        reqMsg.setEventType(eventType);
        reqMsg.setData(reqStr);

        channelGateway.enqueueString(reqMsg);
    }

    @Override
    public void pushPaymentCancellation(String eventType) {
    }

    private String getResourceFile(String eventType) {
        String resourceFile = null;
        switch (eventType) {
            case "PAYMENT_PROCESSOR_IW_PAYMENT_RECEIVED":
            case "PAYMENT_PROCESSOR_OW_PAYMENT_CREATED":
            case "PAYMENT_PROCESSOR_OW_PAYMENT_SUBMIT_TO_SWIFT":
            case "PAYMENT_PROCESSOR_PAYMENT_STATUS_REPORT_CONFIRMED":
            case "OUTWARD_PAYMENT_DELETED":
                resourceFile = "incomingpayment.json";
                break;

            case "PAYMENT_CREATE_OW_RETURN":
            case "PAYMENT_PROCESSOR_OW_RETURN_CONFIRMED":
            case "PAYMENT_PROCESSOR_OW_RETURN_SUBMIT_TO_SWIFT":
                resourceFile = "paymentReturn2.json";
                break;
        }

        String paymentStr = null;
        try {
            paymentStr = FileUtils.readFileToString(new ClassPathResource("messagejson/" + resourceFile).getFile(), StandardCharsets.UTF_8);

            /*Resource resource = new ClassPathResource(resourceFile);
            FileReader fileReader = new FileReader(resource.getFile());
            BufferedReader in = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String str;
            while (( str = in.readLine()) != null) {
                stringBuilder.append(str);
            }
            paymentStr = stringBuilder.toString();*/
        } catch (IOException e) {
            System.out.println(e);
        }

        return paymentStr;
    }
}
