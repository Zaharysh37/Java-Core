package com.innowise.minispring;

import com.innowise.minispring.test.UserRepository;
import com.innowise.minispring.test.UserService;
import java.util.List;

public class ClassScanner {
    public static List<Class<?>> getClasses() {
        return List.of(UserService.class, UserRepository.class);
    }
}
