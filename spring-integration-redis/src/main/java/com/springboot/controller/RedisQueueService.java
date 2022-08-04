package com.springboot.controller;

public interface RedisQueueService {

   void pushPayment(String eventType);

   void pushPaymentReturn(String eventType);

   void pushPaymentStatusReport(String eventType);
}
