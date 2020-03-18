package com.selfimpr.event.guava.spring_cloud;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Configuration
public class EventBusConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    @ConditionalOnMissingBean(AsyncEventBus.class)
    AsyncEventBus createEventBus() {
        // EventBus可以定义线程池，比SpringEvent更适合高并发场景
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(5,
                new BasicThreadFactory.Builder().namingPattern("jvm-cache-pool-%d").daemon(true).build());
        AsyncEventBus asyncEventBus = new AsyncEventBus(executorService);
        Reflections reflections = new Reflections("com.selfimpr.event.guava.spring_cloud", new MethodAnnotationsScanner());
        Set<Method> methodSet = reflections.getMethodsAnnotatedWith(Subscribe.class);
        if (methodSet != null) {
            for (Method method : methodSet) {
                try {
                    asyncEventBus.register(applicationContext.getBean(method.getDeclaringClass()));
                } catch (Exception e) {
                    // register subscribe class error
                    e.printStackTrace();
                }
            }
        }
        return asyncEventBus;
    }
}
