package com.innowise.minispring.test;

import com.innowise.minispring.MiniApplicationContext;

public class Main {
    public static void main(String[] args)
        throws Exception {
        MiniApplicationContext container = new MiniApplicationContext();
        UserService userService = container.getBean(UserService.class);
        userService.getPrint();
        UserRepository userRepository = container.getBean(UserRepository.class);
    }
}
