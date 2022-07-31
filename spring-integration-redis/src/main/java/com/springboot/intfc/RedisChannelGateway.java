package com.springboot.intfc;

import com.springboot.model.PostPublishedEvent;
import com.springboot.model.Student;

public interface RedisChannelGateway {
   void enqueue(PostPublishedEvent event);

   void enqueue(Student student);
}