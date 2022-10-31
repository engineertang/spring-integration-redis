package com.springboot.listener;

import org.springframework.stereotype.Service;

@Service
public class MyListenerImpl implements MyListener {

    @Override
    public void onListen(String object) {
        System.out.println("My listener is listening a new message");
        System.out.println(object);
    }
}
