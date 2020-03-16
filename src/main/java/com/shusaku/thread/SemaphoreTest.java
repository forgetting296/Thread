package com.shusaku.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreTest implements Runnable {

    final Semaphore semaphore = new Semaphore(3);

    @Override
    public void run() {
        try {
            //获取信号量
            semaphore.acquire();
            //System.out.println("~~~~~");
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getId() + " done");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //释放信号量
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            semaphore.release();
            //System.out.println("-----");
        }
    }

    public static void main(String[] args){
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        SemaphoreTest semaphoreTest = new SemaphoreTest();
        for(int i = 0;i < 4;i ++){
            executorService.submit(semaphoreTest);
        }
        for(int i = 0;i < 2;i ++){
            executorService.submit(semaphoreTest);
        }
        executorService.shutdown();
    }
}
