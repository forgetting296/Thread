package com.shusaku.thread;

/**
 * @author liuzi
 * Thread-0正在进行读操作
 * Thread-0正在进行读操作
 * Thread-0正在进行读操作
 * Thread-0正在进行写操作
 * Thread-0正在进行读操作
 * Thread-0正在进行读操作
 * Thread-0正在进行读操作
 * Thread-0正在进行写操作
 * Thread-0正在进行读操作
 * Thread-0正在进行读操作
 * Thread-0正在进行读操作
 * Thread-0正在进行写操作
 * Thread-0正在进行读操作
 * Thread-0正在进行读操作
 * Thread-0正在进行读操作
 * Thread-0正在进行写操作
 * Thread-0读写操作完毕
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行写操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行写操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行写操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行写操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1正在进行读操作
 * Thread-1读写操作完毕
 *
 * 读写操作  一个线程无论读写，都没完成  其他线程无法进行读写操作
 */
public class MySynchronizedReadWrite {

    public static void main(String[] args){
        final MySynchronizedReadWrite test = new MySynchronizedReadWrite();
        new Thread(){
            @Override
            public void run(){
                test.get(Thread.currentThread());
            }
        }.start();

        new Thread(){
            @Override
            public void run(){
                test.get(Thread.currentThread());
            }
        }.start();
    }

    public synchronized void get(Thread thread){
        long startTime = System.currentTimeMillis();
        int i = 0;
        while(System.currentTimeMillis() - startTime <= 1){
            i ++;
            if(i % 4 == 0){
                System.out.println(thread.getName() + "正在进行写操作");
            }else{
                System.out.println(thread.getName() + "正在进行读操作");
            }
        }
        System.out.println(thread.getName() + "读写操作完毕");
    }
}
