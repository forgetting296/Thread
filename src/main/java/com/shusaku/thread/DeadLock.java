package com.shusaku.thread;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.locks.ReentrantLock;

public class DeadLock implements Runnable {

    public static ReentrantLock lock1 = new ReentrantLock();
    public static ReentrantLock lock2 = new ReentrantLock();

    int lock;

    public DeadLock(int lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            if(lock == 1){
                lock1.lockInterruptibly();
                Thread.sleep(500);
                lock2.lockInterruptibly();
            }else{
                lock2.lockInterruptibly();
                Thread.sleep(500);
                lock1.lockInterruptibly();
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(lock1.isHeldByCurrentThread()){
                lock1.unlock();
            }
            if(lock2.isHeldByCurrentThread()){
                lock2.unlock();
            }
            System.out.println(Thread.currentThread().getName() + ": 线程退出！");
        }
    }

    public static void main(String[] args){
        DeadLock deadLock1 = new DeadLock(1);
        DeadLock deadLock2 = new DeadLock(2);
        Thread t1 = new Thread(deadLock1);
        Thread t2 = new Thread(deadLock2);
        t1.start();
        t2.start();
        //DeadLockChecker.check();
    }

    static class DeadLockChecker{

        private final static ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
        final static Runnable deadLockCheck = new Runnable() {
            @Override
            public void run() {
                while(true){
                    long[] deadLockedThreadIds = mbean.findDeadlockedThreads();
                    if(deadLockedThreadIds != null){
                        ThreadInfo[] threadInfo = mbean.getThreadInfo(deadLockedThreadIds);
                        for (Thread t:
                             Thread.getAllStackTraces().keySet()) {
                            for(int i = 0;i < threadInfo.length;i ++){
                                if(t.getId() == threadInfo[i].getThreadId()){
                                    t.interrupt();
                                }
                            }
                        }
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        //作为守护线程  监测死锁  一旦死锁  interrupt 抛异常
        public static void check(){
            Thread t = new Thread(deadLockCheck);
            t.setDaemon(true);
            t.start();
        }
    }
}
