package com.shusaku.thread;

import java.util.concurrent.Exchanger;

/**
 * @program: Thread-study
 * @description: 　　　Exchanger用于两个线程的直线数据交换　当Exchanger只有一个线程的时候,该线程会阻塞　　
 *              　直到有别的线程调用exchange进入缓冲区,　当前线程与新线程交换数据之后同时恢复执行
 * @author: Shusaku
 * @create: 2020-03-18 09:53
 */
public class ExchangerTest {

    public static void main(String[] args) {
        Exchanger exchanger = new Exchanger();

        Thread thread1 = new Thread(() -> {
            try {
                //这个方法类似于将参数中的字符串发送到另一个线程　　并接收另一个线程发送过来的数据　　如果只有一个线程将会阻塞　直到收到另一个线程发送的内容
                String content = (String) exchanger.exchange("震寰宇，领玉旨，奉天降杀");
                System.out.println("君奉天: " + content);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            try {
                String content = (String) exchanger.exchange("撼八荒，降神谕，任吾逍遥");
                System.out.println("玉逍遥: " + content);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread2.start();
    }

}
