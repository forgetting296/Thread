package com.shusaku.thread;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author liuzi
 */
public class ThreadTest {

    public void testForThread(){
        //尝试在for循环中开启线程  并行操作
        List<Integer> list1 = new ArrayList<Integer>();
        List<Integer> list2 = new ArrayList<Integer>();
        long l1 = System.currentTimeMillis();
        for(int i = 0;i < 100000; i++){
            list1.add(i);
        }
        long l2 = System.currentTimeMillis();
        System.out.println("普通执行  耗时："+ (l2 - l1));


        Thread t1 = new Thread(() -> {
            for(int i = 0;i < 33333; i++){
                list2.add(i);
            }
        });Thread t2 = new Thread(() -> {
            for(int i = 33333;i < 66666; i++){
                list2.add(i);
            }
        });Thread t3 = new Thread(() -> {
            for(int i = 66666;i < 100000; i++){
                list2.add(i);
            }
        });
        long l3 = System.currentTimeMillis();
        t1.start();
        t2.start();
        t3.start();
        long l4 = System.currentTimeMillis();
        System.out.println("多线程  耗时："+ (l4 - l3));
    }

    public void testSub(){

        List<Integer> list1 = new ArrayList<Integer>();

        for(int i = 0;i < 100; i++){
            list1.add(i);
        }
        long l1 = System.currentTimeMillis();
        for(int i = 0;i < list1.size();i++){
            System.out.print(list1.get(i));
        }
        long l2 = System.currentTimeMillis();
        System.out.println();

        long l5 = System.currentTimeMillis();
        for(int i = 0;i < list1.size();i++){
            System.out.print(list1.get(i));
        }
        long l6 = System.currentTimeMillis();
        System.out.println();

        //List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9);
        /*long l3 = System.currentTimeMillis();
        insertPG(list1);
        long l4 = System.currentTimeMillis();*/

        //System.out.println((l2 - l1) +"-------"+(l4 - l3) +"-------"+(l6 - l5));
        System.out.println((l2 - l1) +"-------"+"-------"+(l6 - l5));
    }

    private void insertPG(List<Integer> intList){

        Map<Integer, List<Integer>> groups =
                intList.stream().collect(Collectors.groupingBy(s -> (s - 1) / (intList.size()/4)));
        List<List<Integer>> subSets = new ArrayList<List<Integer>>(groups.values());

        subSets.stream()
                .forEach(list -> {
                    new Thread(() -> {
                        list.forEach(System.out::print);
                        System.out.println(System.currentTimeMillis());
                    }).start();
                });
    }

    public final void givenList_whenParitioningIntoNSublistsUsingGroupingBy_thenCorrect() {
       // List<Integer> intList = newArrayList(1, 2, 3, 4, 5, 6, 7, 8);
        List<Integer> intList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);

        Map<Integer, List<Integer>> groups =
                intList.stream().collect(Collectors.groupingBy(s -> (s - 1) / 4));
        List<List<Integer>> subSets = new ArrayList<List<Integer>>(groups.values());

        subSets.stream()
                .forEach(System.out::println);
//        List<Integer> lastPartition = subSets.get(2);
//        List<Integer> expectedLastPartition = Lists.<Integer> newArrayList(7, 8);
//        assertThat(subSets.size(), equalTo(3));
//        assertThat(lastPartition, equalTo(expectedLastPartition));
    }



    public void testLambdaForEachMap2(){

        Map<String, List<Integer>>map = new HashMap<>();

        map.put("a", Arrays.asList(1,2,3,4,5,6));
        map.put("b", Arrays.asList(1,2,3,4,5,6));
        map.put("c", Arrays.asList(1,2,3,4,5,6));
        map.put("d", Arrays.asList(1,2,3,4,5,6));
        map.put("e", Arrays.asList(1,2,3,4,5,6));

        map.forEach((k,v)-> {
            System.out.println(k);
            new Thread(() -> {
                v.forEach(i -> {
                    System.out.print(i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }).start();
        });
    }

    public static  int clientTotal = 10000;
    public static int threadTotal = 200;
    public static int count = 0;
    public static AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * 测试线程安全变量
     */
    public void testAtomic(){

        try {
            ExecutorService pool = Executors.newCachedThreadPool();
            final Semaphore semaphore = new Semaphore(threadTotal);
            final CountDownLatch latch = new CountDownLatch(clientTotal);
            for(int i = 0;i < clientTotal;i++){
                pool.execute(() -> {
                    try {
                        semaphore.acquire();
                        count++;
                        atomicInteger.incrementAndGet();
                        semaphore.release();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    latch.countDown();
                });
            }
            latch.await();
            pool.shutdown();
            System.out.println("count--"+count);
            System.out.println("atomicCount--"+atomicInteger);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void testLatch(){
        final CountDownLatch latch = new CountDownLatch(5);
        for(int i = 0;i < 6;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("子线程执行");
                    latch.countDown();
                }
            }).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("主线程执行");
    }

    public void testList() throws InterruptedException, ExecutionException {
        // 开始时间
        long start = System.currentTimeMillis();
        // 模拟数据List
        List<String> list = new ArrayList<String>();
        for (int i = 1; i <= 3000; i++) {
            list.add(i + "");
        }
        // 每500条数据开启一条线程
        int threadSize = 500;
        // 总数据条数
        int dataSize = list.size();
        // 线程数
        int threadNum = dataSize / threadSize + 1;
        // 定义标记,过滤threadNum为整数
        boolean special = dataSize % threadSize == 0;
        // 创建一个线程池
        ExecutorService exec = Executors.newFixedThreadPool(threadNum);
        // 定义一个任务集合
        List<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
        Callable<Integer> task = null;
        List<String> cutList = null;
        // 确定每条线程的数据
        for (int i = 0; i < threadNum; i++) {
            if (i == threadNum - 1) {
                if (special) {
                    break;
                }
                cutList = list.subList(threadSize * i, dataSize);
            } else {
                cutList = list.subList(threadSize * i, threadSize * (i + 1));
            }
            // System.out.println("第" + (i + 1) + "组：" + cutList.toString());
            final List<String> listStr = cutList;
            task = new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    System.out.println(Thread.currentThread().getName() + "线程：" + listStr);
                    return 1;
                }
            };
            // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
            tasks.add(task);
        }
        List<Future<Integer>> results = exec.invokeAll(tasks);
        for (Future<Integer> future : results) {
            System.out.println(future.get());
        }
        // 关闭线程池
        exec.shutdown();
        System.out.println("线程任务执行结束");
        System.err.println("执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");
    }
}

class TestSynchronized{
    static final Object object = new Object();

    public static void main(String[] args){

        new Thread(() -> {
            synchronized (object){
                for(int i = 1;i < 10;i += 2){
                    System.out.println(Thread.currentThread().getName() + "     " + i);
                    object.notify();
                    //尽量避免在循环中try{}catch(){}
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(() -> {
            synchronized (object){
                for(int i = 2;i < 10;i += 2){
                    System.out.println(Thread.currentThread().getName() + "     " + i);
                    object.notify();
                    //尽量避免在循环中try{}catch(){}
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


}

