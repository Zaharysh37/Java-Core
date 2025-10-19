package com.innowise.minispring.annotation;

public interface InitializingBean {
    void afterPropertiesSet() throws Exception;
}
