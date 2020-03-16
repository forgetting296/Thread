package com.shusaku.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Condition 和 ReentrantLock的关系就类似于synchronized 和 Object.wait() 和 signal()
 * 可以用ReentrantLock对象的newCondition()方法获取一个Condition对象 获取锁之后，condition.await()会使线程等待，同时释放当前锁
 * 当其他线程获取锁，并且执行condition.singal()或者singalAll()时，线程会重新获得锁并继续执行。或者线程中断时，也能跳出等待。
 * awaitUninterruptibly()方法与await()基本相同，但是不会在等待过程中响应中断。
 *
 */
public class ConditionTest implements Runnable {

    public static ReentrantLock lock = new ReentrantLock();
    public static Condition condition = lock.newCondition();

    @Override
    public void run() {
        try {
            lock.lockInterruptibly();
            System.out.println(Thread.currentThread().getName() + " 获取锁");
            System.out.println(Thread.currentThread().getName() + " 释放锁");
            condition.await();
            System.out.println(Thread.currentThread().getName() + " 当前线程锁已被唤醒");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(lock.isHeldByCurrentThread()){
                lock.unlock();
                System.out.println(Thread.currentThread().getName() + " 释放锁");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ConditionTest test = new ConditionTest();
        Thread t1 = new Thread(test);
        t1.start();
        Thread.sleep(2000);

        /**
         * 同一个锁对象，获取锁之后，唤醒之前的线程，释放锁之后，之前的线程拿到锁 ，继续执行 ，然后释放锁
         */

        lock.lock();
        System.out.println(Thread.currentThread().getName() + " 获取锁");
        condition.signal();//唤醒一个等待节点，但并不一定唤醒哪个   singalAll()可以唤醒所有等待节点，再去竞争锁
        System.out.println(Thread.currentThread().getName() + " 唤醒锁");
        lock.unlock();
        System.out.println(Thread.currentThread().getName() + " 锁已被释放");
    }

}
