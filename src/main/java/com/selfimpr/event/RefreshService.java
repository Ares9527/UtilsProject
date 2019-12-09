package com.selfimpr.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class RefreshService {

    @Autowired
    private ApplicationContext applicationContext;

    public void test() {
        applicationContext.publishEvent(new RefreshEvent("123"));
    }
}
