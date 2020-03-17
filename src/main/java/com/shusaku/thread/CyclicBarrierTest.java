package com.shusaku.thread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * @program: Thread-study
 * @description: CyclicBarrier　循环栅栏　　　　一组线程都到达临界点之后再恢复运行　先到达的会阻塞直到所有都到达临界点
 *                                          初始化的时候指定一个栅栏拦截的线程数量，指定数量的线程都　wait　
 *                                      之后才进行放行　构造方法中还可以指定一个Runnable对象　　在栅栏放行之后执行run方法　　
 *                                      随机挑选一个线程执行放行之后的run方法　　　计数可以被重置
 * @author: Shusaku
 * @create: 2020-03-17 09:47
 */
public class CyclicBarrierTest {

    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(4,() -> {
            System.out.println("当前线程为: " + Thread.currentThread().getName());
        });
        CyclicBarrierDemo cbd = new CyclicBarrierDemo(cyclicBarrier);
        for(int i = 0;i < 4;i ++) {
            new Thread(cbd,"写线程-" + i).start();
            //cyclicBarrier.reset();
        }
    }
}
class CyclicBarrierDemo implements Runnable {

    private CyclicBarrier cyclicBarrier;


    public CyclicBarrierDemo(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {

        System.out.println(Thread.currentThread().getName() + " 正在写数据......");

        try {
            TimeUnit.SECONDS.sleep(2);
            System.out.println(Thread.currentThread().getName() + " 写数据完毕,等待其他线程......");
            cyclicBarrier.await();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        //System.out.println("所有线程写入完毕.....");
    }
}
