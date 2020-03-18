package com.selfimpr.event.guava.spring_cloud;

import com.google.common.eventbus.AsyncEventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocalEventBusImpl implements LocalEventBus {

    @Autowired
    private AsyncEventBus eventBus;

    @Override
    public void post(Event event) {
        if (event != null) {
            eventBus.post(event);
        }
    }

}
