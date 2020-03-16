package com.shusaku.thread;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * @author liuzi
 * 两条线程交替打印奇偶数
 */
public class AlternatePrinting {

    static int i = 0;
    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphoreOdd = new Semaphore(1);
        Semaphore semaphoreEven = new Semaphore(1);
        //启动奇数线程
        semaphoreOdd.acquire();
        PrintEven printEven = new PrintEven(semaphoreOdd,semaphoreEven);
        Thread thread1 = new Thread(printEven);
        thread1.start();
        PrintOdd printOdd = new PrintOdd(semaphoreOdd,semaphoreEven);
        Thread thread2 = new Thread(printOdd);
        thread2.start();
    }

    /**
     * 使用信号量实现
     */
    static class PrintOdd implements Runnable{

        private Semaphore semaphoreOdd;
        private Semaphore semaphoreEvent;

        public PrintOdd(Semaphore semaphoreOdd, Semaphore semaphoreEvent){
            this.semaphoreEvent = semaphoreEvent;
            this.semaphoreOdd = semaphoreOdd;
        }

        @Override
        public void run() {
            try {
                //获取信号量 semaphoreOdd在初始化的时候被获取了信号量  所以这里会被阻塞，会先执行下面的奇数线程
                semaphoreOdd.acquire();
                while(true){
                    i ++;
                    if(i % 2 == 0){
                        System.out.println("偶数线程：" + i);
                        //释放偶数信号量，让奇数线程那边的阻塞解除
                        semaphoreEvent.release();
                        //再次申请获取偶数信号量，因为之前已经获取过，如果没有奇数线程去释放，那么就会一直阻塞在这，等待奇数线程释放
                        semaphoreOdd.acquire();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    static class PrintEven implements Runnable{

        private Semaphore semaphoreOdd;
        private Semaphore semaphoreEven;

        public PrintEven(Semaphore semaphoreOdd, Semaphore semaphoreEven){
            this.semaphoreOdd = semaphoreOdd;
            this.semaphoreEven = semaphoreEven;
        }

        @Override
        public void run() {
            try {
                semaphoreEven.acquire();
                while(true){
                    i++;
                    if(i % 2 == 1){
                        System.out.println("奇数线程：" + i);
                        //释放奇数信号量  让偶数线程那边线程阻塞解除
                        semaphoreOdd.release();
                        //这里阻塞，等待偶数线程释放信号量  申请获取奇数信号量，等待偶数线程执行完然后释放信号量，不然阻塞
                        semaphoreEven.acquire();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static AtomicInteger atomicInteger = new AtomicInteger(1);
    /*public static void main(String[] args) {
        Thread t1 = new Thread(new ThreadA());
        Thread t2 = new Thread(new ThreadB());
        t1.start();
        t2.start();
    }*/
    /**
     * 同步锁实现方式
     * 让两个线程使用同一把锁。交替执行。
     */
    static class ThreadA implements Runnable{

        @Override
        public void run() {
            while(true){
                synchronized (atomicInteger){
                    if(atomicInteger.intValue() % 2 == 1){
                        System.out.println("奇数线程：" + atomicInteger.intValue());
                        atomicInteger.getAndIncrement();
                        //奇数线程释放资源锁
                        //atomicInteger.notify();
                        try {
                            atomicInteger.wait();//wait会自动释放之前占用的锁  不需要再之前进行notify()
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else{
                        //是偶数  奇数线程等待
                        try {
                            atomicInteger.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    static class ThreadB implements Runnable{

        @Override
        public void run() {
            while(true){
                synchronized (atomicInteger){
                    if(atomicInteger.intValue() % 2 == 0){
                        System.out.println("偶数线程：" + atomicInteger.intValue());
                        atomicInteger.getAndIncrement();
                        //atomicInteger.notify();
                        try {
                            atomicInteger.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else{
                        try {
                            atomicInteger.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

}

class PrintDigitThread extends Thread{

    public static void main(String[] args) {
        PrintDigitThread p1 = new PrintDigitThread((i) -> i % 2 == 1,"thread1");
        PrintDigitThread p2 = new PrintDigitThread((i) -> i % 2 == 0,"thread2");
        p1.start();
        p2.start();
    }

    private Predicate<Integer> predicate;

    public PrintDigitThread(Predicate<Integer> predicate, String name){
        this.predicate = predicate;
        this.setName(name);
    }

    @Override
    public void run() {
        int v = ShareData.atomicInt.get();
        while(v < 100){
            synchronized (ShareData.atomicInt){
                v = ShareData.atomicInt.get();
                if(predicate.test(v)){
                    System.out.println(Thread.currentThread().getName() + ":" + v);
                    ShareData.atomicInt.incrementAndGet();
                    try {
                        ShareData.atomicInt.notify();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    try {
                        ShareData.atomicInt.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}


class ShareData{
    public static final AtomicInteger atomicInt = new AtomicInteger(0);
}
