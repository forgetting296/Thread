package com.shusaku.thread.fallback;

/**
 * @program: Java8Test
 * @description:
 * @author: Shusaku
 * @create: 2019-12-24 09:47
 */
public class JoinFunction {

    private static final Long SLEEP_TIME = 500L;


    static class HotWater implements Runnable{

        @Override
        public void run() {

            try {
                System.out.println("开始烧水");
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                System.out.println("没有水了");
            }
            System.out.println("水烧好了");
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
            }
            System.out.println("杯子洗好了");
        }
    }

    public static void main(String[] args) {
        Thread hThread = new Thread(new HotWater(),"烧水");
        Thread wThread = new Thread(new WashCup(),"清洗");
        hThread.start();
        wThread.start();

        try {
            hThread.join();
            wThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("喝茶");
    }
}

