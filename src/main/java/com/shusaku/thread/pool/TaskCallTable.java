package com.shusaku.thread.pool;

import java.util.Random;
import java.util.concurrent.Callable;

/**
 * @author liuzi
 */
public class TaskCallTable implements Callable<String> {

    private int s;
    Random random = new Random();
    public TaskCallTable(int s){
        this.s = s;
    }

    @Override
    public String call() throws Exception {
        String name = Thread.currentThread().getName();
        long currentTimeMillis = System.currentTimeMillis();
        System.out.println(name + "启动时间" + currentTimeMillis / 1000);
        int rInt = random.nextInt(3);
        Thread.sleep(rInt * 1000);
        System.out.println(name + "is working..." + s);
        return s + "";
    }
}
