package com.shusaku.thread;

/**
 * @author liuzi
 */
public class TestVolatile {
    //被volatile修饰的变量  线程不安全
    private static volatile int num = 0;

    public static void main(String[] args) throws InterruptedException {

        for(int i = 0;i < 100;i ++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int i = 0;i < 1000;i ++){
                        {
                            num ++;
                        }
                    }
                }
            }).start();
        }
        Thread.sleep(2000);
        System.out.println(num);//结果不确定 但是肯定小于100000
    }

}
