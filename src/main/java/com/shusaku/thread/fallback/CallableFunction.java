package com.shusaku.thread.fallback;

import java.util.concurrent.*;

/**
 * @program: Java8Test
 * @description:
 * @author: Shusaku
 * @create: 2019-12-24 10:03
 */
public class CallableFunction {

    private static final Long SLEEP_TIME = 500L;


    static class HotWater implements Callable<Boolean> {

        @Override
        public Boolean call() throws Exception {
            try {
                System.out.println("开始烧水");
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                System.out.println("没有水了");
                return Boolean.FALSE;
            }
            System.out.println("水烧好了");
            return Boolean.TRUE;
        }
    }

    static class WashCup implements Callable<Boolean> {

        @Override
        public Boolean call() throws Exception {
            try {
                System.out.println("开始清洗杯子");
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                System.out.println("杯子碎了");
                return Boolean.FALSE;
            }
            System.out.println("杯子洗好了");
            return Boolean.TRUE;
        }
    }

    public static void main(String[] args) {
        HotWater hotWater = new HotWater();
        WashCup washCup = new WashCup();
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
        Future<Boolean> waterSubmit = fixedThreadPool.submit(hotWater);
        Future<Boolean> washSubmit = fixedThreadPool.submit(washCup);

        Boolean waterResult = false;
        Boolean washResult = false;
        try {
            waterResult = waterSubmit.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            washResult = washSubmit.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(waterResult && washResult) {
            System.out.println("喝茶了");
        } else {
            System.out.println("没有茶喝");
        }

        //这里是可以手动关闭线程池的   因为上边的操作都是异步阻塞的  执行到这里 肯定是任务已经跑完了  不会抛出异常
        fixedThreadPool.shutdown();
    }

    public static void isReady(Boolean waterResult, Boolean washResult) {

        if(waterResult && washResult) {
            System.out.println("喝茶了");
        }

    }

}
