package com.shusaku.thread.pool.custom;

public interface Service {

    //关闭线程池
    void shutdown();

    //查看线程池是否已经关闭
    boolean isShutdown();

    //提交任务到线程池
    void execute(Runnable task) throws InterruptedException;
}
