package com.shusaku.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class MyTryLock2 implements Runnable {

    ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args){
        MyTryLock2 lock = new MyTryLock2();
        Thread t1 = new Thread(lock);
        Thread t2 = new Thread(lock);
        t1.start();
        t2.start();
    }


    @Override
    public void run() {
        try {
            if(lock.tryLock(5, TimeUnit.SECONDS)){
                System.out.println(Thread.currentThread().getName() + "获取锁成功！");
                Thread.sleep(6000);
            }else{
                System.out.println(Thread.currentThread().getName() + "获取锁失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
    }
}
