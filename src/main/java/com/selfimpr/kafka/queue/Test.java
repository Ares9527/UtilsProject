package com.selfimpr.kafka.queue;

import org.apache.kafka.common.TopicPartition;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * 原型-递归版
 */
public class Test {

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

        // TODO 定时线程 更新currentOffsets的值（需要设置最大等待时间）
        // 模拟第一次定时检测
        Iterator<Map.Entry<TopicPartition, PriorityBlockingQueue<Integer>>> iterator = tempQueue.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<TopicPartition, PriorityBlockingQueue<Integer>> next = iterator.next();
            TopicPartition key = next.getKey();
            PriorityBlockingQueue<Integer> value = next.getValue();
            System.out.println(key + " : "+ value);
            Integer[] digui = digui(value.take(), value);
            System.out.println(key + " 此次需要提交的位移是: " + digui[0]);
            if (digui[0] != digui[1]) {
                value.add(digui[0]);
            }
            value.add(digui[1]);
            System.out.println(key + " 此时tempQueue是: "+ value);
        }

        System.out.println("---------------------------------------------");
        // 模拟线程池的线程执行完毕一些command
        tempQueue.get(new TopicPartition("topicName", 0)).add(7);
        tempQueue.get(new TopicPartition("topicName", 0)).add(3);
        tempQueue.get(new TopicPartition("topicName", 0)).add(5);
        tempQueue.get(new TopicPartition("topicName", 0)).add(6);

        // 模拟第二次定时检测
        Iterator<Map.Entry<TopicPartition, PriorityBlockingQueue<Integer>>> iterator2 = tempQueue.entrySet().iterator();
        while (iterator2.hasNext()) {
            Map.Entry<TopicPartition, PriorityBlockingQueue<Integer>> next = iterator2.next();
            TopicPartition key = next.getKey();
            PriorityBlockingQueue<Integer> value = next.getValue();
            System.out.println(key + " : "+ value);
            Integer[] digui = digui(value.take(), value);
            System.out.println(key + " 222此次需要提交的位移是: " + digui[0]);
            if (digui[0] != digui[1]) {
                value.add(digui[0]);
            }
            value.add(digui[1]);
            System.out.println(key + " 222此时tempQueue是: "+ value);
        }
    }

    /**
     * @param first
     * @param queue
     * @return Integer[] 0代表需要提交的位移/并且需要还原的值，1表示需要还原的值
     * @throws InterruptedException
     */
    public static Integer[] digui(Integer first, PriorityBlockingQueue<Integer> queue) throws InterruptedException {
        Integer second = queue.take();
        if (first + 1 != second) {
            return new Integer[] {first, second};
        } else {
            if (queue.size() == 0) {
                return new Integer[] {second, second};
            }
            return digui(second, queue);
        }
    }
}
