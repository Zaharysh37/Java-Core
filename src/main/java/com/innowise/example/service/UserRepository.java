package com.innowise.example.service;

import com.innowise.minispring.annotation.Component;
import com.innowise.minispring.annotation.InitializingBean;
import com.innowise.minispring.annotation.Scope;

@Component
@Scope("prototype")
public class UserRepository implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Initializing success!");
    }

    public void getStatus() {
        System.out.println("Full Success!");
    }
}
