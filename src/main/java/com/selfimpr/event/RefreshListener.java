package com.selfimpr.event;

import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class RefreshListener implements ApplicationListener<RefreshEvent> {

    @Async
    @Override
    public void onApplicationEvent(RefreshEvent refreshEvent) {
        System.out.println(refreshEvent.getSkuId());
    }
}
