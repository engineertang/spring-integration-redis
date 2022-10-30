package com.springboot.intfc;

import com.iaspec.ecph.dto.message.InternalMessage;
import com.iaspec.ecph.payment.dto.PaymentDTO;

public interface RedisChannelGateway {

    void enqueue(PaymentDTO student);

    void enqueueString(InternalMessage jsonString);
}