package com.shusaku.thread;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liuzi
 *
 * 使用Lock对象的lock()方法，用来获取锁。
 * 如果锁已经被其他线程获取，则进行等待。
 * 注意：如果使用lock()方法获取锁，必须主动去释放，并且在发生异常的时候，不会自动释放锁。
 *      所以，Lock必须在try{}catch(){}中进行，并且将释放锁的操作放在finally块中进行，保证锁一定会被释放，不会发生死锁；
 */
public class MyLockTest {

    private static ArrayList<Integer> list = new ArrayList<>();
    static Lock lock = new ReentrantLock();

    public static void main(String[] args){
        new Thread(){
            @Override
            public void run(){
                Thread thread = Thread.currentThread();
                lock.lock();
                try {
                    System.out.println(thread.getName() + "获取了锁");
                    for(int i = 0;i < 5;i++){
                        list.add(i);
                    }
                }catch (Exception e){

                }finally {
                    System.out.println(thread.getName() + "释放了锁");
                    lock.unlock();
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run(){
                Thread thread = Thread.currentThread();
                lock.lock();
                try {
                    System.out.println(thread.getName() + "获取了锁");
                    for(int i = 0;i < 5;i++){
                        list.add(i);
                    }
                }catch (Exception e){

                }finally {
                    System.out.println(thread.getName() + "释放了锁");
                    lock.unlock();
                }
            }
        }.start();
    }

}
