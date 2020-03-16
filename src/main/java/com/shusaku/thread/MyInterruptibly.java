package com.shusaku.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liuzi
 *
 * lockInterruptibly()方法比较特殊，当通过这个方法获取锁时，如果线程正在等待获取锁，则这个线程能够响应中断
 * 也就是说，当两个线程同时通过lock.lockInterruptibly()想获取某个锁，假若此线程A获取到了锁，而线程B则在等待，
 * 那么对B线程调用threadB.interrupt()方法能够中断B的等待过程。
 * 注意：当一个线程获取了锁之后，是不会被interrupt()方法中断的。
 *
 *  如果是使用synchronized修饰的话，当一个线程处于等待某个锁的状态，是无法被中断的，只有一直等待下去
 */
public class MyInterruptibly {

    private Lock lock = new ReentrantLock();

    public static void main(String[] args){
        MyInterruptibly test = new MyInterruptibly();
        MyThread thread0 = new MyThread(test);
        MyThread thread1 = new MyThread(test);
        thread0.start();
        thread1.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread0.interrupt();
        thread1.interrupt();
        System.out.println("========================");
    }

    static class MyThread extends Thread{
        private MyInterruptibly test = null;
        public MyThread (MyInterruptibly test){
            this.test = test;
        }
        @Override
        public void run(){
            try {
                test.insert(Thread.currentThread());
            }catch (Exception e){
                System.out.println(Thread.currentThread().getName() + "被中断");
            }
        }
    }

    private void insert(Thread thread) throws InterruptedException {
        lock.lockInterruptibly();   //注意：如果需要正确中断等待锁的线程，必须将获取锁放在外边，然后将InterruptedException抛出  让run()方法捕获异常  打印中断信息
        try {
            System.out.println(thread.getName() + "得到了锁");
            long startTime = System.currentTimeMillis();
            for( ; ;){
                if(System.currentTimeMillis() - startTime >= 10000)
                    break;
                //插入数据
            }
        }finally {
            System.out.println(Thread.currentThread().getName() + "执行finally");
            lock.unlock();
            System.out.println(thread.getName() + "释放了锁");
        }
    }
}
