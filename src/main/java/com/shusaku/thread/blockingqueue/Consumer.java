package com.shusaku.thread.blockingqueue;

import java.util.concurrent.BlockingQueue;

/**
 * @author liuzi
 */
public class Consumer implements Runnable {

    BlockingQueue<String> queue;
    public Consumer(BlockingQueue<String> queue){
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            String consumer = Thread.currentThread().getName();
            System.out.println(consumer);
            //取走BlockingQueue里排在首位的对象，若BlockingQueue为空，阻断进入等待状态直到Blocking有新对象加入为止
            String temp = queue.take();
            System.out.println(consumer + " get a product : " + temp);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
