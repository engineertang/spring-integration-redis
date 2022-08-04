package com.iaspec.ecph.scbhostsimulator.web;

import com.iaspec.ecph.scbhostsimulator.integrationRedis.RedisQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pushmessage")
public class PushMsg2RedisController {

	@Autowired
	RedisQueueService queueService;

	@PostMapping
	public void sendStudentInformation(@RequestParam String eventType) {
		System.out.printf("push internal message of event type %1$s in redis %n", eventType);

		switch (eventType){
			// pacs.008
			case "PAYMENT_PROCESSOR_IW_PAYMENT_RECEIVED" :
			case "PAYMENT_PROCESSOR_OW_PAYMENT_CREATED" :
			case "PAYMENT_PROCESSOR_OW_PAYMENT_SUBMIT_TO_SWIFT":
				queueService.pushPayment(eventType);
				break;
			// pacs.004
			case "PAYMENT_CREATE_OW_RETURN":
			case "PAYMENT_PROCESSOR_OW_RETURN_CONFIRMED":
			case "PAYMENT_PROCESSOR_OW_RETURN_SUBMIT_TO_SWIFT":
				queueService.pushPaymentReturn(eventType);
				break;
			// payment status report pacs.002
			case "PAYMENT_PROCESSOR_PAYMENT_STATUS_REPORT_CONFIRMED":
				queueService.pushPaymentStatusReport(eventType);
				break;

			// instructing agent send cancellation msg camt.056
			case "PAYMENT_PROCESSOR_OUTWARD_CANCELLATION_RECEIVED" :
				break;
			// 向下游agent foward send cancellation message to instructed agent
			case "PAYMENT_PROCESSOR_OUTWARD_CANCELLATION_APPROVED" :
				break;




		}
	}
}