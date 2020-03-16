package com.shusaku.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class CountDownLatchTest implements Runnable{

    public static void main(String[] args) throws InterruptedException {

        /**
         * countDownLatch.await();会将之前countDownLatch.countDown()的线程一并执行  countDown()之后线程阻断 效果类似await()
         */

        CountDownLatchTest test = new CountDownLatchTest();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for(int i = 0;i < 15;i++){
            executorService.submit(test);
        }
        //放行所有等待的countDown
        countDownLatch.await();
        System.out.println("end");
        executorService.shutdown();
    }

    static final CountDownLatch countDownLatch = new CountDownLatch(10);
    static final AtomicInteger ato = new AtomicInteger(0);

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
            System.out.println("complete" + ato.incrementAndGet());
            //等待计数达到count  当前线程会等待
            countDownLatch.countDown();
            //System.out.println("....");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
