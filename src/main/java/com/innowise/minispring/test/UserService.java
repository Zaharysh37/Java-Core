package com.innowise.minispring.test;

import com.innowise.minispring.Autowired;
import com.innowise.minispring.Component;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void getPrint() {
        userRepository.getVoice();
    }
}
