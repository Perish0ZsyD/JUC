package com.siyuan.threadlearn;

/**
 * @author Siyuan
 * @date 2024/10/17/12:08
 * main方法为程序的入口 底层由main线程负责执行  由JVM自动调用执行
 *  java.lang.Thread 线程类
 *
 *  currentThread() 获取当前线程对象 是一个静态方法
 *  getName() 获取线程名称
 *  setName(String name) 设置线程名称
 */
public class TestThreadMain {
    public static void main(String[] args) {
        // 获取当前线程对象
        Thread thread = Thread.currentThread();

        // 打印线程名称
        System.out.println("线程的名称：" + thread.getName());

        thread.setName("主线程"); // 设置线程名城

        // 打印线程名称
        System.out.println("线程的名称：" + thread.getName());

    }
}
