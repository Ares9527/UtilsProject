package com.selfimpr.event.guava.spring_cloud;

public interface LocalEventBus {

    void post(Event event);

}
