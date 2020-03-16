package com.shusaku.thread.pool.custom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: Thread-study
 * @description: 新建一个线程池　用来执行提交的任务
 * @author: Shusaku
 * @create: 2020-03-16 15:57
 */
public class PoolThread extends Thread {

    private Logger logger = LoggerFactory.getLogger(PoolThread.class);

    private BolckingQueue taskQueue = null;

    private boolean isStop = false;

    public PoolThread(BolckingQueue blockingQueue) {
        this.taskQueue = blockingQueue;
    }

    @Override
    public void run() {
        while (!isStop && !Thread.currentThread().isInterrupted()) {
            try {

                logger.info("PoolThread try to run");
                //从任务队列取任务并执行
                Runnable runnable = (Runnable) taskQueue.dequeued();
                runnable.run();
            } catch (Exception e) {
                isStop = true;
                break;
            }
        }
    }

    public synchronized void doStop() {
        isStop = true;
    }

    public synchronized boolean isStopped() {
        return isStop;
    }

}
