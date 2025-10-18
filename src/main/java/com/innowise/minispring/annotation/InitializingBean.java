package com.innowise.minispring.ann;

public interface InitializingBean {
    void afterPropertiesSet() throws Exception;
}
