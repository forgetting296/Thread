package com.shusaku.thread.blockingqueue.main;

import com.shusaku.thread.blockingqueue.Consumer;
import com.shusaku.thread.blockingqueue.Producer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author liuzi
 */
public class Test {
    /**
     * I hava made a product: Producer1
     * I hava made a product: Producer2
     * I hava made a product: Producer3
     * Consumer1
     * Consumer1 get a product : A Product,生产线程： Producer1
     * Consumer2
     * Consumer2 get a product : A Product,生产线程： Producer2
     * Consumer3
     * Consumer3 get a product : A Product,生产线程： Producer3
     * Consumer4
     * Consumer5
     * I hava made a product: Producer5
     * Consumer4 get a product : A Product,生产线程： Producer5
     * @param args
     */
    public static void main(String[] args){
        //如果不设置参数capacity  该queue的默认大小为Integer.MAX_VALUE
        BlockingQueue<String> queue = new LinkedBlockingDeque<>(2);
        Consumer consumer = new Consumer(queue);
        Producer producer = new Producer(queue);
        for(int i = 0;i < 3;i ++){
            new Thread(producer,"Producer" + (i + 1)).start();
        }
        for(int i = 0;i < 5;i ++){
            new Thread(consumer,"Consumer" + (i + 1)).start();
        }
        new Thread(producer,"Producer" + (5)).start();
    }

}
