package com.springboot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostPublishedEvent {

    private String postUrl;
    private String postTitle;
    private List<String> emails;

}