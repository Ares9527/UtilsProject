package com.selfimpr.event.guava;

import com.google.common.eventbus.EventBus;

public class Test {

    public static void main(String[] args) {
        //guava test
        EventBus eventBus = new EventBus();
        GuavaEventListener listener = new GuavaEventListener();
        eventBus.register(listener);

        eventBus.post(new GuavaEvent(200));

        System.out.println("LastMessage:" + listener.getLastMessage());
    }
}
