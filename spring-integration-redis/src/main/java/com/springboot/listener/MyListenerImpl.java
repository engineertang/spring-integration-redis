package com.springboot.listener;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class MyListenerImpl implements MyListener {

    @Override
    public void onListen() {
        System.out.println("My listener is listening a new message");
    }
}
