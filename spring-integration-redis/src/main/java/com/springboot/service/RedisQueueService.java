package com.springboot.service;

public interface RedisQueueService {

   void pushPayment(String eventType);

   void pushPaymentReturn(String eventType);

   void pushPaymentStatusReport(String eventType);

   void pushPaymentCancellation(String eventType);
}
