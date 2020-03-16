package com.shusaku.thread.pool;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @author liuzi
 */
public class TestPool {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Future<?> submit = null;
        Random random = new Random();
        /**
         * 创建固定数量的线程池
         * 如果没有任务执行，线程会一直等待
         * 在构造函数中，参数4是线程池的大小，可以随意设置，也可以和cpu的数量保持一致
         * int cpuNums = Runtime.getRuntime().availableProcessors();
         */
        //ExecutorService exec = Executors.newFixedThreadPool(4);
        /**
         * 创建调度线程池
         * 用来调度即将执行的任务的线程池
         */
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(4);

        //用来记录各线程的返回结果
        ArrayList<Future<?>> results = new ArrayList<>();
        for(int i = 0;i < 10; i++){
            //fixPool提交线程，runnable无返回值，callable有返回值
            //submit = exec.submit(new TaskRunnable(i));
            //对于schedulerPool来说，调用submit提交任务时，跟普通pool效果一致
            //submit = exec.submit(new TaskCallTable(i));
            //对于schedulerPool来说，调用schedule提交任务的时候，则可按延迟，按间隔时长来调度线程的运行
            submit = exec.schedule(new TaskCallTable(i),random.nextInt(10),TimeUnit.SECONDS);
            //存储线程执行结果
            results.add(submit);
        }

        for(Future f:results){
            boolean done = f.isDone();
            System.out.println(done ? "完成":"未完成");
            System.out.println("线程返回future结果： " + f.get());
        }
    }

}
