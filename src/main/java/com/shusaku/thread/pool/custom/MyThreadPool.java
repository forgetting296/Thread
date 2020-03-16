package com.shusaku.thread.pool.custom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * @program: Thread-study
 * @description:
 * @author: Shusaku
 * @create: 2020-03-16 16:09
 */
public class MyThreadPool implements Service {

    private Logger logger = LoggerFactory.getLogger(MyThreadPool.class);

    /**
     * 任务队列　　用来存储提交的任务
     */
    private BolckingQueue<Runnable> taskQueue = null;

    /**
     * 线程池中存储线程的容器
     */
    private Queue<PoolThread> threads = new ArrayDeque<>();

    private boolean isShutdown = false;

    public MyThreadPool(int initSize, int maxNoOfTasks) {

        logger.info("init ThreadPool initSize: {}, maxNoOfTasks: {}", initSize, maxNoOfTasks);

        taskQueue = new BolckingQueue<>(maxNoOfTasks);

        //初始化线程
        for(int i = 0;i < initSize;i ++) {
            threads.add(new PoolThread(taskQueue));
        }

        logger.info("threads add finished, taskQueue init finished, strating...");

        //启动线程池线程
        threads.forEach(poolThread -> {poolThread.start();});
    }

    @Override
    public void shutdown() {
        isShutdown = true;
        threads.forEach(PoolThread::doStop);
    }

    @Override
    public boolean isShutdown() {
        return isShutdown;
    }

    @Override
    public synchronized void execute(Runnable task) throws InterruptedException {

        if(this.isShutdown) {
            throw new IllegalArgumentException("ThreadPool is stropped");
        }

        taskQueue.enqueue(task);

    }

    public static void main(String[] args) throws InterruptedException {

        MyThreadPool threadPool = new MyThreadPool(5,20);

        for(int i = 0;i < 20;i ++) {
            threadPool.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(10);
                    System.out.println(Thread.currentThread().getName() + " is running add down");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        while(true) {
            System.out.println("---------------");
            TimeUnit.SECONDS.sleep(5);
        }

    }

}
