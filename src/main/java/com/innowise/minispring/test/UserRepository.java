package com.innowise.minispring.test;

import com.innowise.minispring.Component;
import com.innowise.minispring.InitializingBean;
import com.innowise.minispring.Scope;

@Component
//@Scope("prototype")
public class UserRepository implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Initializing success!");
    }

    public void getVoice() {
        System.out.println("Full Success!");
    }
}
