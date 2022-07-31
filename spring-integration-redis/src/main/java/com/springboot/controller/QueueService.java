package com.springboot.controller;

import com.springboot.model.PostPublishedEvent;
import com.springboot.model.Student;

public interface QueueService {

   void enqueue(PostPublishedEvent event);


   void enqueue(Student student);
}
