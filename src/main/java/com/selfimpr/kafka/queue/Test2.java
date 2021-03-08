package com.selfimpr.kafka.queue;

import org.apache.kafka.common.TopicPartition;

import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * 原型-优先级阻塞队列
 */
public class Test2 {

    public static void main(String[] args) throws InterruptedException {
        ConcurrentMap<TopicPartition, PriorityBlockingQueue<Integer>> tempQueue = new ConcurrentHashMap<>();
        // TODO 需要控制priorityBlockingQueue最大容量限制
        PriorityBlockingQueue<Integer> priorityBlockingQueue = new PriorityBlockingQueue<>(10, Comparator.comparingInt(o -> o));
        tempQueue.putIfAbsent(new TopicPartition("topicName", 0), priorityBlockingQueue);

        // 模拟线程池的线程执行完毕一些command
        tempQueue.get(new TopicPartition("topicName", 0)).add(4);
        tempQueue.get(new TopicPartition("topicName", 0)).add(2);
        tempQueue.get(new TopicPartition("topicName", 0)).add(1);
        tempQueue.get(new TopicPartition("topicName", 0)).add(8);

        System.out.println(tempQueue);
        System.out.println();


        PriorityBlockingQueue<Integer> priorityBlockingQueue1 = tempQueue.get(new TopicPartition("topicName", 0));
//        priorityBlockingQueue1.remove(1);
        boolean contains = priorityBlockingQueue1.contains(1);
        System.out.println(contains);

        System.out.println(tempQueue);

        System.out.println(Runtime.getRuntime().availableProcessors());

    }

}
