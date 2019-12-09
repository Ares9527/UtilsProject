package com.selfimpr.event;

import org.springframework.context.ApplicationEvent;

public class RefreshEvent extends ApplicationEvent {

    private String message;

    public RefreshEvent(String source) {
        super(source);
        this.message = source;
    }

    public String getSkuId() {
        return message;
    }

    public void setSkuId(String skuId) {
        this.message = skuId;
    }
}