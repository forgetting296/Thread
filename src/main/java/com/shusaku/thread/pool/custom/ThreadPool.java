package com.shusaku.thread.pool.custom;

/**
 * 自定义线程池
 * @param <Job>
 */
public interface ThreadPool<Job extends Runnable> {

    void execture(Job job);

    void shutdown();

    //添加工作线程
    void addWorders(int num);

    //减少工作线程
    void removeWorker(int num);

    //得到正在等待执行的任务数量
    int getJobSize();
}
