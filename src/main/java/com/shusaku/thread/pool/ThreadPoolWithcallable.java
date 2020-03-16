package com.shusaku.thread.pool;

import java.util.concurrent.*;

/**
 * @author liuzi
 */
public class ThreadPoolWithcallable {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(4);
        for(int i = 0;i < 10;i ++){
            Future<String> submit = fixedThreadPool.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    Thread.sleep(5000);
                    return "b--" + Thread.currentThread().getName();
                }
            });
            //这个方法是被阻塞的，一直要等到线程任务返回结果
            System.out.println(submit.get());
        }
        fixedThreadPool.shutdown();
    }

    public void testThreadPool() throws InterruptedException {
        //Thread thread = Thread.currentThread();
        //thread.sleep(1000);
    }

}
