package com.shusaku.thread.pool;

import java.util.concurrent.*;

/**
 * @author liuzi
 */
public class ExecutorDemo {

    public static void main(String[] args){
        /**
         * 只有一个线程的线程池  所有提交的任务都是顺序执行的
         */
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        /**
         * 线程中有很多线程需要同时执行，老的可用线程将被新的任务触发重新执行
         * 如果线程超过60秒内没有执行，那么将被终止并从池中删除
         */
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        //获取cpu数量
        int cpuNums = Runtime.getRuntime().availableProcessors();
        /**
         * 拥有固定数量的线程的线程池  如果没有任务执行 线程会一直等待
         */
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(cpuNums);
        /**
         * 调度线程池  用来调度即将执行的任务的线程池
         * 调用schedule提交任务的时候，则可按延迟，按间隔时长来调度线程的运行
         */
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(cpuNums);
        /**
         * 只有一个线程，用来调度执行将来的任务  好像可以指定时间
         */
        ScheduledExecutorService threadScheduledExecutor = Executors.newSingleThreadScheduledExecutor();

        TimeUnit unit;
        BlockingQueue workQueue;
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
        //poolExecutor.set
    }

}
