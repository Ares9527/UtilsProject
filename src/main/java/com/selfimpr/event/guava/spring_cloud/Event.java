package com.selfimpr.event.guava.spring_cloud;

public interface Event<T> {

    T getContent();
}
