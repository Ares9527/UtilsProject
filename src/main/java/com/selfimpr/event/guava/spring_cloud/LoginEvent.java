package com.selfimpr.event.guava.spring_cloud;

public class LoginEvent implements Event<LoginMsg> {

    private LoginMsg loginMsg;

    public LoginEvent(LoginMsg loginMsg) {
        this.loginMsg = loginMsg;
    }

    @Override
    public LoginMsg getContent() {
        return this.loginMsg;
    }

}
