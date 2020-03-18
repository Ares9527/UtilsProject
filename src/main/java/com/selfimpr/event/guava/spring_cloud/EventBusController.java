package com.selfimpr.event.guava.spring_cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 在Spring cloud环境下，使用google公司开源的guava工具类EventBus
 * 一、引入guava的jar包
 * 二、在config下新建一个类EventBusConfig.java
 * 三、利用接口封装事件发送
 *      1、定义接口LocalEventBus.java
 *      2、定义实现类LocalEventBusImpl.java
 *      3、接口Event.class
 * 四、在业务工程里使用，需要定义事件、消息体、订阅者、发送者：
 * 1、定义login事件：LoginEvent
 * 2、定义消息体：LoginMsg
 * 3、定义订阅者：LoginSubscriber
 * 4、定义发送者：把消息发送到EventBus
 */
@RestController
@RequestMapping("/event/bus")
public class EventBusController {

    @Autowired
    private LocalEventBus localEventBus;

    @GetMapping(value = "/test")
    public void eventBusTest() {
        LoginMsg msg = new LoginMsg();
        msg.setUid(9527L);
        localEventBus.post(new LoginEvent(msg));
    }
}
