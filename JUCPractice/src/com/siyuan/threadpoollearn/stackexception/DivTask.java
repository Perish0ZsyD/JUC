package com.siyuan.threadpoollearn.stackexception;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Siyuan
 * @date 2024/10/29/21:50
 */
public class DivTask implements Runnable{
    int a, b;
    public DivTask(int a, int b) {
        this.a = a;
        this.b = b;
    }
    @Override
    public void run() {
        double re = a / b;
        System.out.println(re);
    }
    public static void main(String[] args) {
        ThreadPoolExecutor pools = new TraceThreadPoolExecutor(0, Integer.MAX_VALUE,
                0L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
        /**
         * 错误堆栈中可以看到是在哪里提交的任务
         */
        for (int i = 0; i < 5; i++) {
            pools.execute(new DivTask(100, i));
        }
    }
}
