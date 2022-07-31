package com.springboot.controller;

import com.springboot.intfc.RedisChannelGateway;
import com.springboot.model.PostPublishedEvent;
import com.springboot.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisQueueService implements QueueService {
    private RedisChannelGateway channelGateway;

    @Autowired
    public RedisQueueService(RedisChannelGateway channelGateway) {
        this.channelGateway = channelGateway;
    }


    @Override
    public void enqueue(PostPublishedEvent event) {
        channelGateway.enqueue(event);
    }

    @Override
    public void enqueue(Student student) {
        channelGateway.enqueue(student);

    }
}
