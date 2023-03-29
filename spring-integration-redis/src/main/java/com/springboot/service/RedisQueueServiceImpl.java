package com.springboot.service;

import static com.iaspec.ecph.constant.EcphMessageEventTypeConstant.PAYMENT_PROCESSOR_IW_PAYMENT_RECEIVED;
import static com.iaspec.ecph.constant.EcphMessageEventTypeConstant.PAYMENT_PROCESSOR_STATUS_REPORT_CREATED;
import static com.iaspec.ecph.constant.EcphMessageEventTypeConstant.*;
import static com.iaspec.ecph.constant.EcphMessageEventTypeConstant.SUBMIT_PAYMENT_STATUS_REPORT;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.iaspec.ecfps.util.JsonHelper;
import com.iaspec.ecph.dto.Validatable;
import com.iaspec.ecph.dto.message.InternalMessage;
import com.iaspec.ecph.investigation.dto.MTInvestigationDTO;
import com.iaspec.ecph.payment.dto.FIToFIPaymentStatusReportDTO;
import com.iaspec.ecph.payment.dto.PaymentDTO;
import com.iaspec.ecph.payment.dto.PaymentReturnDTO;
import com.springboot.integration.gateway.RedisChannelGateway;

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
        System.out.printf("push internal message of event type %1$s in redis %n", eventType);
        String paymentStr = getResourceFile(eventType);

        try {
            PaymentReturnDTO paymentReturn = JsonHelper.fromJson(paymentStr, PaymentReturnDTO.class);

            PaymentDTO payment = new PaymentDTO();
            BeanUtils.copyProperties(paymentReturn, payment);
            payment.setSettlementMethod(paymentReturn.getSettlementMethod());

            String reqStr = JsonHelper.toJsonIgnoreNulls(payment);
            System.out.println(reqStr);

            InternalMessage reqMsg = new InternalMessage();
            reqMsg.setBusinessService(payment.getBusinessService());
            reqMsg.setEventType(PAYMENT_PROCESSOR_IW_PAYMENT_RECEIVED);
            reqMsg.setData(reqStr);

            channelGateway.enqueueString(reqMsg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // pacs.008 msg
    @Override
    public void pushPayment(String eventType) {
        String jsonString = getResourceFile(eventType);

        try {
            PaymentDTO payment = JsonHelper.fromJson(jsonString, PaymentDTO.class);

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

    @Override
    public void pushMTInvestigation(String eventType) {
        System.out.printf("push internal message of event type %1$s in redis %n", eventType);
        String paymentStr = getResourceFile(eventType);

        try {
            MTInvestigationDTO investigationDTO = JsonHelper.fromJson(paymentStr, MTInvestigationDTO.class);

            String reqStr = JsonHelper.toJsonIgnoreNulls(investigationDTO);
            System.out.println(reqStr);

            InternalMessage reqMsg = new InternalMessage();
            reqMsg.setBusinessService("swift.cbprplus.02");
            reqMsg.setEventType(eventType);
            reqMsg.setData(reqStr);

            channelGateway.enqueueString(reqMsg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getResourceFile(String eventType) {
        String resourceFile = null;
        switch (eventType) {
            case PAYMENT_PROCESSOR_IW_PAYMENT_RECEIVED:
            case PAYMENT_PROCESSOR_OW_PAYMENT_CREATED:
            case PAYMENT_PROCESSOR_OW_PAYMENT_SUBMIT_TO_SWIFT:
            case OUTWARD_PAYMENT_DELETED:
                resourceFile = "incomingpayment.json";
                break;

            case PAYMENT_PROCESSOR_IW_PAYMENT_RETURN_RECEIVED:
            case PAYMENT_PROCESSOR_OW_RETURN_CREATED:
            case PAYMENT_PROCESSOR_OW_RETURN_SUBMIT_TO_SWIFT:
                resourceFile = "paymentReturn3.json";
                break;

            case SUBMIT_OUTWARD_SWIFT_MT_MESSAGE:
                resourceFile = "MTn95.json";
                break;

            case PAYMENT_PROCESSOR_STATUS_REPORT_CREATED:
            case SUBMIT_PAYMENT_STATUS_REPORT:
                resourceFile = "FIToFIPaymentStatusReportDTO.json";
                break;
        }

        String jsonString = null;
        try {
            jsonString = FileUtils.readFileToString(new ClassPathResource("messagejson/" + resourceFile).getFile(), StandardCharsets.UTF_8);

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

        return jsonString;
    }
}
