package com.shusaku.thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author liuzi
 */
public class ThreadPoolWithRunnable {

    public static void main(String[] args){
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for(int i = 1;i < 5; i++){
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("thread name : " + Thread.currentThread().getName());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        cachedThreadPool.shutdown();
    }

}
