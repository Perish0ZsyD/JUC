package com.siyuan.threadpoollearn.stackexception;

import java.util.concurrent.*;

/**
 * 线程池可能会覆盖程序抛出的异常，简单方法就是使用excute()代替submit()或者如下使用自定义线程池
 * 扩展线程池，在任务调度之前先保存提交任务线程的堆栈信息
 * @author Siyuan
 * @date 2024/10/29/16:00
 */
public class TraceThreadPoolExecutor extends ThreadPoolExecutor {
    public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
                                   long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    } // 构造函数

    @Override
    public void execute(Runnable task) {
        super.execute(wrap(task, clientTrace(), Thread.currentThread()
                .getName()));
    }

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(wrap(task, clientTrace(), Thread.currentThread()
                .getName()));
    }

    private Exception clientTrace() {
        return new Exception("Client stack trace");
    }

    // lambda优化，相当于new Runnable() { @Override public void run() {...}}
    private Runnable wrap(final Runnable task, final Exception clientStack,
                          String clientThreadName) { // wrap第二个参数为一个异常，里面保存着提交任务的线程的堆栈信息，当任务发生异常，就会被打印
        return () -> {
            try {
                task.run();
            } catch (Exception e) {
                clientStack.printStackTrace();
                throw e;
            }
        };
    }
}
