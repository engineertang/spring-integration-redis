package com.springboot.listener;

import com.springboot.model.PostPublishedEvent;
import com.springboot.model.Student;
import org.springframework.stereotype.Service;

//@Service("RedisEventProcessingService")
public class RedisEventProcessingService implements EventProcessingService {

/*    @Override
    public void process(PostPublishedEvent event) {
        // TODO: Send emails here, retry strategy, etc :)

        System.out.println("received spring integration message");
    }*/

    @Override
    public void processStudent(Student student) {
        // TODO: Send emails here, retry strategy, etc :)

        System.out.println("received spring integration message" + student);
    }
}
