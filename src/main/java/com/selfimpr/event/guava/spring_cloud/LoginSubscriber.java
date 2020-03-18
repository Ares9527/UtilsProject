package com.selfimpr.event.guava.spring_cloud;

import com.google.common.eventbus.Subscribe;
import org.springframework.stereotype.Component;

@Component
public class LoginSubscriber {

    @Subscribe
    public void onLogin(LoginEvent event) {
        LoginMsg msg = event.getContent();
        System.out.println(msg);

        Long uid = msg.getUid();
        // 具体业务
    }

}
