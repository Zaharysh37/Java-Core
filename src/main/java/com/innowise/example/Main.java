package com.innowise.example;

import com.innowise.example.service.UserRepository;
import com.innowise.example.service.UserService;
import com.innowise.minispring.MiniApplicationContext;

public class Main {
    public static void main(String[] args)
        throws Exception {
        MiniApplicationContext container = new MiniApplicationContext("com.innowise.example.service");
        UserService userService = container.getBean(UserService.class);
        userService.getInfo();
        UserRepository userRepository = container.getBean(UserRepository.class);
    }
}
