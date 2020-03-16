package com.shusaku.thread;

import com.shusaku.thread.utils.LocalDateUtils;

import java.util.Date;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newCachedThreadPool;

/**
 * @author liuzi
 */
public class ThreadPoolWithRunable {

    public static void main(String[] args){

        ExecutorService pool = newCachedThreadPool();
        for(int i = 0;i < 5;i ++){
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("thread name :");
                }
            });
        }
        pool.shutdown();
    }

    public void test(){
        String dateStr = "2019-04-29 00:00:00";
        Date date = LocalDateUtils.parseStringToDateTime(dateStr, "yyyy-MM-dd HH:mm:ss");
        System.out.println(date.compareTo(new Date()));
    }


}
