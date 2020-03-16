package com.shusaku.thread.pool.custom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @program: Thread-study
 * @description:
 * @author: Shusaku
 * @create: 2020-03-16 13:45
 */
public class DefaultThreadPool<Job extends Runnable> implements ThreadPool<Job> {

    //线程最大限制
    private static int MAX_WORKER_NUMBERS = 10;

    //默认线程数量
    private static int DEFAULT_WORKER_NUMBER = 5;

    //最小线程数量
    private static int MIN_WORKER_NUMBER = 5;

    //工作列表
    private final LinkedList<Job> jobs = new LinkedList<>();

    //当前工作者的数量
    private int workNum = DEFAULT_WORKER_NUMBER;

    //工作者列表
    private final List<Worker> workers = Collections.synchronizedList(new ArrayList<>());

    AtomicLong threadNum = new AtomicLong();

    public DefaultThreadPool () {
        initializeWorkers(workNum);
    }

    public DefaultThreadPool (int num) {
        workNum = num > MAX_WORKER_NUMBERS ? MAX_WORKER_NUMBERS : num < MIN_WORKER_NUMBER ? MIN_WORKER_NUMBER : num;
        initializeWorkers(workNum);
    }

    private void initializeWorkers(int workNum) {

        //开启workNum个线程去处理jobs中的任务  刚刚开启时处于wait状态　　等待notify()
        for(int i = 0;i < workNum;i ++) {
            Worker worker = new Worker();
            workers.add(worker);
            Thread thread = new Thread(worker,"ThreadPool-Worker-" + threadNum.incrementAndGet());
            thread.start();
        }

    }

    @Override
    public void execture(Job job) {
        if(null != job) {
            synchronized (jobs) {
                jobs.addLast(job);
                jobs.notify();
            }
        }
    }

    @Override
    public void shutdown() {
        workers.forEach(Worker::shutdown);
    }

    @Override
    public void addWorders(int num) {
        synchronized (jobs) {
            if(num + this.workNum > MAX_WORKER_NUMBERS) {
                num = MAX_WORKER_NUMBERS - this.workNum;
            }
            initializeWorkers(num);
        }
    }

    @Override
    public void removeWorker(int num) {
        synchronized (jobs) {
            if(num > this.workNum) {
                throw new IllegalArgumentException("beyond workNum");
            }
            int count = 0;
            while(count < num) {
                Worker worker = workers.get(count);
                if(workers.remove(worker)) {
                    worker.shutdown();
                    count ++;
                }
            }
            this.workNum -= count;
        }
    }

    @Override
    public int getJobSize() {
        return jobs.size();
    }

    class Worker implements Runnable {

        private volatile boolean running = true;

        @Override
        public void run() {

            while(running) {
                Job job;
                synchronized (jobs) {
                    while(jobs.isEmpty()) {
                        try {
                            jobs.wait();
                        } catch (InterruptedException e) {
                            //感知到外部对WorkerThread的中断操作，直接返回
                            e.printStackTrace();
                            return;
                        }
                    }
                    job = jobs.removeFirst();
                }
                //执行队列中的任务
                if(null != job) {
                    try {
                        job.run();
                    } catch (Exception e) {
                        //忽略job执行中的异常
                    }
                }
            }

        }

        public void shutdown() {
            running = false;
        }
    }
}

