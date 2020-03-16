package com.shusaku.thread;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liuzi
 *
 * tryLock()方法是有返回值的，它表示用来尝试获取锁，如果获取成功，则返回true，如果获取失败，则返回false
 * 这个方法无论如何都会立即返回。在拿不到锁时，不会等待。
 * tryLock(long time, TimeUnit unit)方法和tryLock()方法是类似的，只不过区别在于这个方法在拿不到锁时会等待一定的时间，在时间期限之内如果还拿不到锁，就返回false。
 * 如果如果一开始拿到锁或者在等待期间内拿到了锁，则返回true。
 */
public class MyTryLock {

    private static ArrayList<Integer> list = new ArrayList<>();
    static Lock lock = new ReentrantLock();

    public static void main(String[] args){
        new Thread(){
            @Override
            public void run(){
                Thread thread = Thread.currentThread();
                boolean flag = lock.tryLock();
                System.out.println(thread.getName() + flag);
                if(flag){
                    try {
                        System.out.println(thread.getName() + "得到了锁");
                        Thread.sleep(1000);
                        for(int i = 0;i < 5;i ++){
                            list.add(i);
                        }
                    }catch (Exception e){
                        System.out.println(e.getClass().getName() + "--" + e.getMessage());
                    }finally {
                        System.out.println(thread.getName() + "释放了锁");
                        lock.unlock();
                    }
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run(){
                Thread thread = Thread.currentThread();
                boolean flag = lock.tryLock();
                System.out.println(thread.getName() + flag);
                if(flag){
                    try {
                        System.out.println(thread.getName() + "得到了锁");
                        for(int i = 0;i < 5;i ++){
                            list.add(i);
                        }
                    }catch (Exception e){
                        System.out.println(e.getClass().getName() + "--" + e.getMessage());
                    }finally {
                        System.out.println(thread.getName() + "释放了锁");
                        lock.unlock();
                    }
                }
            }
        }.start();
        System.out.println(list);
    }
}
