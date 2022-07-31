package com.springboot.listener;

import com.springboot.model.PostPublishedEvent;
import com.springboot.model.Student;

public interface EventProcessingService {
   //void process(PostPublishedEvent event);

   void processStudent(Student student);
}

