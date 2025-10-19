package com.innowise.example.service;

import com.innowise.minispring.annotation.Autowired;
import com.innowise.minispring.annotation.Component;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void getInfo() {
        userRepository.getStatus();
    }
}
