package com.shusaku.thread.fallback;

import com.google.common.util.concurrent.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: Java8Test
 * @description:
 * @author: Shusaku
 * @create: 2019-12-24 10:27
 */
public class GuavaFunction {

    private static final Long SLEEP_TIME = 500L;

    static class HotWater implements Runnable{

        @Override
        public void run() {

            try {
                System.out.println("开始烧水");
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                System.out.println("没有水了");
                return;
            }
            //System.out.println("水烧好了");
        }
    }

    static class WashCup implements Runnable {

        @Override
        public void run() {
            try {
                System.out.println("开始清洗杯子");
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                System.out.println("杯子碎了");
                return;
            }
            //System.out.println("杯子洗好了");
        }
    }

    public static void main(String[] args) {
        HotWater hotWater = new HotWater();
        WashCup washCup = new WashCup();
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(fixedThreadPool);
        ListenableFuture<?> hotWaterSubmit = listeningExecutorService.submit(hotWater);
        ListenableFuture<?> washCupSubmit = listeningExecutorService.submit(washCup);
        Futures.addCallback(hotWaterSubmit, new FutureCallback<Object>() {
            @Override
            public void onSuccess(Object aBoolean) {
                System.out.println("水烧好了~");
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("没有水了~");
            }
        },listeningExecutorService);

        Futures.addCallback(washCupSubmit, new FutureCallback<Object>() {
            @Override
            public void onSuccess(Object aBoolean) {
                System.out.println("杯子洗好了~");
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("杯子碎了~");
            }
        },listeningExecutorService);

        //这里不能手动关闭线程池  因为guava这种方式是异步非阻塞的  这个方法一旦执行  异步任务就会抛出异常
        //listeningExecutorService.shutdownNow();
    }

}
