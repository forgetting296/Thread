package com.shusaku.thread.pool.custom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * @program: Thread-study
 * @description: 自定义阻塞队列
 * @author: Shusaku
 * @create: 2020-03-16 15:19
 */
public class BolckingQueue<T> {

    private Logger logger = LoggerFactory.getLogger(BolckingQueue.class);

    /**
     * 使用链表实现一个阻塞队列(数据结构定义数据存数和获取方式　只要满足这两点　阻塞队列就可以使用链表　或者　数组来实现)
     */
    private List<T> queue = new LinkedList<T>();

    /**
     * limit用来限制提交任务的最大数　默认是10
     */
    private int limit = 10;

    public BolckingQueue(int limit) {
        this.limit = limit;
    }

    /**
     * enqueue是一个同步方法　当任务达到上限　便会调用wait方法进行阻塞　否则将任务放入队列中　并唤醒其他dequeue线程
     * @param item
     */
    public synchronized void enqueue(T item) throws InterruptedException {
        while(this.queue.size() == this.limit) {
            this.wait();
        }
        if(queue.size() <= limit) {
            //notifyAll没有问题　　因为其他的方法都加了悲观锁　并发时没有得到锁一样会重新wait
            this.notifyAll();
        }
        this.queue.add(item);
    }

    /**
     * dequeue 也是一个同步方法　当队列中没有任务时便会调用wait方法进入阻塞　当任务达到最大容量的时候唤醒其他dequeue()线程
     * @return
     * @throws InterruptedException
     */
    public synchronized T dequeued() throws InterruptedException {

        logger.info("BlickingQueue dequeued");

        while(this.queue.size() == 0) {
            logger.info(" BlickingQueue wait");
            this.wait();
        }

        if(this.queue.size() == limit) {
            logger.info("BlickingQueue notify");
            this.notifyAll();
        }

        return queue.remove(0);
    }

    public synchronized int size() {
        return queue.size();
    }

}
