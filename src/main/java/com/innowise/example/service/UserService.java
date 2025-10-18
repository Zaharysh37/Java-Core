package com.innowise.example.service;

import com.innowise.minispring.ann.Autowired;
import com.innowise.minispring.ann.Component;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void getInfo() {
        userRepository.getStatus();
    }
}
