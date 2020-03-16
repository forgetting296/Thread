package com.shusaku.thread.blockingqueue;

import java.util.concurrent.BlockingQueue;

/**
 * @author liuzi
 */
public class Producer implements Runnable{

    BlockingQueue<String> queue;
    public Producer(BlockingQueue<String> queue){
        this.queue = queue;
    }

    @Override
    public void run() {
        System.out.println("I hava made a product: " + Thread.currentThread().getName());
        String temp = "A Product,生产线程： " + Thread.currentThread().getName();
        try {
            //把对象加到BlockingQueue中，如果BlockQueue没有空间，则调用此方法的线程阻断直到BlockingQueue里边有空间再继续
            queue.put(temp);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
