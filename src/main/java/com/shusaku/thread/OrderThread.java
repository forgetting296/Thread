package com.shusaku.thread;

import com.google.common.util.concurrent.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.*;

/**
 * @program: Thread-study
 * @description:  使用不同的方式实现线程的顺序执行
 * @author: Shusaku
 * @create: 2020-03-17 11:03
 */
public class OrderThread {

    //线程顺序执行　　保证线程之间变量的可见性
    private volatile static int num = 0;

    public static void main(String[] args) {

        System.out.println("num: " + num);
        countdownLatchOrderThread();
    }

    public static void countdownLatchOrderThread() {

        CountDownLatch add10Latch = new CountDownLatch(1);
        CountDownLatch add20Latch = new CountDownLatch(1);
        CountDownLatch multiLatch = new CountDownLatch(1);
        CountDownLatch devide4Latch = new CountDownLatch(1);

        Runnable add10 = new Runnable() {
            @Override
            public void run() {
                num += 10;
                System.out.println("add10: " + num);
                add10Latch.countDown();
            }
        };

        Runnable add20 = new Runnable() {
            @Override
            public void run() {
                num += 20;
                System.out.println("add20: " + num);
                add20Latch.countDown();
            }
        };

        Runnable multi3 = new Runnable() {
            @Override
            public void run() {
                num *= 3;
                System.out.println("multi3: " + num);
                multiLatch.countDown();
            }
        };

        Runnable devide4 = new Runnable() {
            @Override
            public void run() {
                num /= 4;
                System.out.println("devide4: " + num);
                devide4Latch.countDown();
            }
        };

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
        fixedThreadPool.execute(add10);
        try {
            add10Latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fixedThreadPool.execute(add20);
        try {
            add20Latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fixedThreadPool.execute(multi3);
        try {
            multiLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fixedThreadPool.execute(devide4);
        try {
            devide4Latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fixedThreadPool.shutdown();
    }

    /**
     *  通过Semaphore保证线程的顺序执行　　两个方法acquire() 和 release() 初始化Semaphore的时候要指定一个permits　
     *  acquire()的时候会将当前的permits减1 如果permits为0　线程将会阻塞　　release()可以将permits加1　release()之后,
     *  acquire()的时候阻塞的线程可以获得信号量继续执行
     *
     *  下面的这种方式　保证了只有semaphore1.acquire()的线程可以执行,其他的Semaphore对象的permits都是0，都是需要在代码中
     *  通过对象进行release()才能获取信号量进行执行,通过这种方式保证线程的执行顺序
     */
    public static void semaphoreOrderThread() {

        Semaphore semaphore1 = new Semaphore(1, true);
        Semaphore semaphore2 = new Semaphore(0);
        Semaphore semaphore3 = new Semaphore(0);
        Semaphore semaphore4 = new Semaphore(0);

        Runnable add10 = new Runnable() {
            @Override
            public void run() {
                try {
                    semaphore1.acquire();
                    num += 10;
                    System.out.println("add10: " + num);
                    semaphore2.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable add20 = new Runnable() {
            @Override
            public void run() {

                try {
                    semaphore2.acquire();
                    num += 20;
                    System.out.println("add20: " + num);
                    semaphore3.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable multi3 = new Runnable() {
            @Override
            public void run() {
                try {
                    semaphore3.acquire();
                    num *= 3;
                    System.out.println("multi3: " + num);
                    semaphore4.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable devide4 = new Runnable() {
            @Override
            public void run() {
                try {
                    semaphore4.acquire();
                    num /= 4;
                    System.out.println("devide4: " + num);
                    semaphore1.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        //调用顺序不影响执行结果
        new Thread(add20).start();
        new Thread(add10).start();
        new Thread(multi3).start();
        new Thread(devide4).start();
    }

    /**
     * 使用单线程的线程池进行调度　　保证线程的顺序执行
     */
    public static void singleThreadPoolOrderThread() {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        Runnable add10 = new Runnable() {
            @Override
            public void run() {
                num += 10;
                System.out.println("add10: " + num);
            }
        };

        Runnable add20 = new Runnable() {
            @Override
            public void run() {
                num += 20;
                System.out.println("add20: " + num);
            }
        };

        Runnable multi3 = new Runnable() {
            @Override
            public void run() {
                num *= 3;
                System.out.println("multi3: " + num);
            }
        };

        Runnable devide4 = new Runnable() {
            @Override
            public void run() {
                num /= 4;
                System.out.println("devide4: " + num);
            }
        };
        singleThreadExecutor.execute(add10);
        singleThreadExecutor.execute(add20);
        singleThreadExecutor.execute(multi3);
        singleThreadExecutor.execute(devide4);
        singleThreadExecutor.shutdown();
    }

    /**
     * guava中对线程池和Future的封装无法保证线程的有序执行　　因为是异步非阻塞的
     */
    public static void guavaOrderThread() {

        Runnable add10 = new Runnable() {
            @Override
            public void run() {
                num += 10;
            }
        };

        Runnable add20 = new Runnable() {
            @Override
            public void run() {
                num += 20;
            }
        };

        Runnable multi3 = new Runnable() {
            @Override
            public void run() {
                num *= 3;
            }
        };

        Callable devide4 = new Callable() {
            @Override
            public Integer call() throws Exception {
                return num /= 4;
            }
        };

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(fixedThreadPool);
        //这个方法既可以支持传Runnable 也支持传Callable
        //executorService.submit();
        ListenableFuture<?> add10Future = executorService.submit(add10);
        Futures.addCallback(add10Future, new FutureCallback<Object>() {
            @Override
            public void onSuccess(@Nullable Object o) {
                System.out.println("add10 success " + num);
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("add10 fail " + num);
            }
        },executorService);

        ListenableFuture<?> add20Future = executorService.submit(add20);
        Futures.addCallback(add20Future, new FutureCallback<Object>() {
            @Override
            public void onSuccess(@Nullable Object o) {
                System.out.println("add20 success " + num);
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("add20 faile " + num);
            }
        }, executorService);

        ListenableFuture<?> multi3Future = executorService.submit(multi3);
        Futures.addCallback(multi3Future, new FutureCallback<Object>() {
            @Override
            public void onSuccess(@Nullable Object o) {
                System.out.println("multi3 success " + num);
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("multi3 faile " + num);
            }
        }, executorService);

        ListenableFuture<?> devide4Future = executorService.submit(devide4);
        Futures.addCallback(devide4Future, new FutureCallback<Object>() {
            @Override
            public void onSuccess(@Nullable Object o) {
                System.out.println("devide4 success " + num);
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("devide4 faile " + num);
            }
        }, executorService);

    }


    /**
     * 采用Callable对象　使用线程池submit()返回的Future的阻塞方法get()　去保证线程的执行顺序
     */
    public static void callableOrderThread() {

        Callable add10 = new Callable() {
            @Override
            public Integer call() throws Exception {
                return num += 10;
            }
        };

        Callable add20 = new Callable() {
            @Override
            public Integer call() throws Exception {
                return num += 20;
            }
        };

        Callable multi3 = new Callable() {
            @Override
            public Integer call() throws Exception {
                return num *= 3;
            }
        };

        Callable devide4 = new Callable() {
            @Override
            public Integer call() throws Exception {
                return num /= 4;
            }
        };

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
        Future add10Future = fixedThreadPool.submit(add10);
        try {
            Integer add10Num = (Integer) add10Future.get();
            System.out.println("add10: " + add10Num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Future add20Future = fixedThreadPool.submit(add20);
        try {
            Integer add20Num = (Integer) add20Future.get();
            System.out.println("add20: " + add20Num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Future multi3Future = fixedThreadPool.submit(multi3);
        try {
            Integer multi3Num = (Integer) multi3Future.get();
            System.out.println("multi3: " + multi3Num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Future devide4Future = fixedThreadPool.submit(devide4);
        try {
            Integer devide4Num = (Integer) devide4Future.get();
            System.out.println("devide4: " + devide4Num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //Future的get()方法会阻塞线程　　直到返回结果　　这种方式需要手动关闭线程池
        fixedThreadPool.shutdown();
    }

    /**
     * 通过Thread本身的join方法　阻塞当前线程　直到调用join的线程执行完毕
     */
    public static void joinOrderThread() {

        Thread add10 = new Thread(() -> {
            try {
                num += 10;
                System.out.println("add10: " + num);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread add20 = new Thread(() -> {
            try {
                num += 20;
                System.out.println("add20: " + num);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread multi3 = new Thread(() -> {
            try {
                num *= 3;
                System.out.println("multi3: " + num);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread devide4 = new Thread(() -> {
            try {
                num /= 4;
                System.out.println("devide4: " + num);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //此处的线程调用顺序不能改变....
        add10.start();
        add20.start();
        multi3.start();
        devide4.start();

        //此处的线程调用顺序不能改变....
        try {
            add10.join();
            add20.join();
            multi3.join();
            devide4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * CyclicBarrier是一个同步工具类　它允许一组线程互相等待　直到达到某个公共屏障点　因为在释放等待线程之后可以重用　　所以称为循环栅栏
     *
     *  采用CyclicBarrier来保证线程的顺序执行　主要是采用互相等待的特性　设置CyclicBarrier的parties为２　在不同线程中await()不同的CyclicBarrier　来保证线程的执行顺序
     * 要第一个运行的线程要保证只await()一个CyclicBarrier　第二个执行的线程先await()第一个线程await()的CyclicBarrier　执行完自己的逻辑在await()一个CyclicBarrier　以此类推
     */
    public static void cyclicBarrierOrderThread() {
        CyclicBarrier cb1 = new CyclicBarrier(2);
        CyclicBarrier cb2 = new CyclicBarrier(2);
        CyclicBarrier cb3 = new CyclicBarrier(2);

        Thread add10 = new Thread(() -> {
            try {
                cb1.await();
                num += 10;
                System.out.println("add10: " + num);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        });

        Thread add20 = new Thread(() -> {
            try {
                cb1.await();//阻塞当前线程　cb1的await()数量达到parties之后会解除阻塞
                cb2.await();
                num += 20;
                System.out.println("add20: " + num);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        });

        Thread multi3 = new Thread(() -> {
            try {
                cb2.await();
                cb3.await();
                num *= 3;
                System.out.println("multi3: " + num);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        });

        Thread devide4 = new Thread(() -> {
            try {
                cb3.await();
                num /= 4;
                System.out.println("devide4: " + num);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        });

        //此处的线程执行顺序不影响结果的输出
        devide4.start();
        add20.start();
        add10.start();
        multi3.start();
    }

}
