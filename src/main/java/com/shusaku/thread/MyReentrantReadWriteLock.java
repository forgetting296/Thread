package com.shusaku.thread;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author liuzi
 * Thread-0正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1读操作进行完成
 * Thread-0读操作进行完成
 * Thread-1正在进行写操作
 * Thread-1正在进行写操作
 * Thread-1正在进行写操作
 * Thread-1正在进行写操作
 * Thread-1正在进行写操作
 * Thread-1正在进行写操作
 * Thread-1正在进行写操作
 * Thread-1正在进行写操作
 * Thread-1正在进行写操作
 * Thread-1正在进行写操作
 * Thread-1正在进行写操作
 * Thread-1正在进行写操作
 * Thread-1正在进行写操作
 * Thread-1正在进行写操作
 * Thread-1正在进行写操作
 * Thread-1正在进行写操作
 * Thread-1写操作进行完成
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0正在进行写操作
 * Thread-0写操作进行完成
 *
 * 读写操作  多个线程可以同时进行读操作  不能同时进行写操作
 * 注意：如果一个线程已经占用了读锁，则此时其他线程如果要申请写锁，则申请写锁的线程会一直等待读锁的释放，但是可以申请读锁
 *      如果一个线程已经占用了写锁，则此时其他线程如果申请写锁或者读锁，则申请的线程会一直等待释放写锁
 *
 *  Lock和synchronized的选择
 *      1、Lock是一个接口，而synchronized是java中的关键字，synchronized是内置的语言实现
 *      2、synchronized在发生异常时，会自动释放线程占有的锁，因此不会导致死锁现象的发生；而Lock在发生异常的时候，如果没有主动通过unLock(0去释放锁，很可能导致死锁现象的发生，需要在finally块中释放
 *      3、Lock可以让等待锁的线程中断响应。而synchronized不行，使用synchronized时，等待的线程会一直等待下去，不能中断响应
 *      4、通过Lock可以知道有没有成功获取锁，sybchronized无法做到
 *      5、Lock可以提高多个线程进行读操作的效率
 *      从性能上来说，如果资源竞争不激烈，两者性能是差不多的，而当资源竞争非常激烈的时候，此时Lock的性能远远优于synchronized。
 *
 */
public class MyReentrantReadWriteLock {

    private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    public static void main(String[] args){
        final MyReentrantReadWriteLock test = new MyReentrantReadWriteLock();
        new Thread(){
            @Override
            public void run(){
                test.get(Thread.currentThread());
                test.write(Thread.currentThread());
            }
        }.start();

        new Thread(){
            @Override
            public void run(){
                test.get(Thread.currentThread());
                test.write(Thread.currentThread());
            }
        }.start();
    }

    /**
     * 读操作  用读锁来锁定
     * @param thread
     */
    public void get(Thread thread){
        //CopyOnWriteArrayList
        rwl.readLock().lock();
        try {
            long startTime = System.currentTimeMillis();
            while(System.currentTimeMillis() - startTime <= 1){
                System.out.println(thread.getName() + "正在进行读操作");
            }
            System.out.println(thread.getName() + "读操作进行完成");
        }finally {
            rwl.readLock().unlock();
        }
    }

    /**
     * 写操作，用写锁来锁定
     * @param thread
     */
    public void write(Thread thread){
        rwl.writeLock().lock();
        try {
            long startTime = System.currentTimeMillis();
            while(System.currentTimeMillis() - startTime <= 1){
                System.out.println(thread.getName() + "正在进行写操作");
            }
            System.out.println(thread.getName() + "写操作进行完成");
        }finally {
            rwl.writeLock().unlock();
        }
    }

}
