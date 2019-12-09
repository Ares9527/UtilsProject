package com.selfimpr.event.guava;

import com.google.common.eventbus.Subscribe;
import org.springframework.stereotype.Component;

@Component
public class GuavaEventListener {

    public int lastMessage = 0;

    @Subscribe
    public void listen(GuavaEvent event) {

        lastMessage = event.getMessage();
        System.out.println("guava--------Message:" + lastMessage);
    }

    public int getLastMessage() {
        return lastMessage;
    }

}
