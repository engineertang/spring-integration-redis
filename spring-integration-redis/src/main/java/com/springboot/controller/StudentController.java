package com.springboot.controller;

import com.springboot.model.PostPublishedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.intfc.MessageGateway;
import com.springboot.model.Student;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {
	
	//@Autowired
	private MessageGateway messageGateway;
	@Autowired
	QueueService queueService;

	@PostMapping
	public void sendStudentInformation(@RequestBody Student student) {
		System.out.println("save");
		//queueService.enqueue(new PostPublishedEvent("google.com", "helloworld", (List<String>) Arrays.asList("@gmail.com")));
		queueService.enqueue(student);
		//messageGateway.sendMessage(student);
	}
}
