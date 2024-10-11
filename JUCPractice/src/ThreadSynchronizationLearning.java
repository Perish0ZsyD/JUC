import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Siyuan
 * @date 2024/10/11/15:14
 * 多线程同时读写变量，存在数据不一致问题
 * 使用synchronized解决了多线程同步访问共享变量的正确性问题。
 * 它的缺点是带来了性能下降。因为synchronized代码块无法并发执行，加锁和解锁需要消耗一定的时间，所以，synchronized会降低程序的执行效率
 * 如何使用synchronized:
 * 1、找出修改共享变量的线程代码块；
 * 2、选择一个共享实例作为锁；
 * 3、使用synchronized(lockObject) { ... }。
 * 注: 无论是否有异常，都会在synchronized结束处正确释放锁
 * JVM规范定义了几种原子操作：
 * 1、基本类型（long和double除外）赋值，例如：int n = m；
 * 2、引用类型赋值，例如：List<String> list = anotherList。
 * 3、单条原子操作语句不需要，多条需要
 * 注: 读也需要注意同步，可以巧妙改写多条语句为一条原子语句
 * 可重入锁:
 * JVM允许同一个线程重复获取同一个锁，这种能被同一个线程反复获取的锁，就叫做可重入锁 (每获取一次锁，记录+1，每退出synchronized块，记录-1，减到0的时候，才会真正释放锁)
 * 避免死锁: ：线程获取锁的顺序要一致。即严格按照先获取lockA，再获取lockB的顺序
 */
public class ThreadSynchronizationLearning {
    public static void main(String[] args) throws InterruptedException{
        var add = new AddThread();
        var dec = new DecThread();
        add.start();
        dec.start();
        add.join();
        dec.join();
        System.out.println(Couter.count); // 加synchronization锁之前，每次不一样，并发交替运行
    }
}

class Couter {
    public static final Object lock = new Object();
    public static int count = 0;
}

class AddThread extends Thread {
    public void run() {
        for (int i = 0; i < 100_000; i++) {
            synchronized (Couter.lock){ // 获取锁
                System.out.println("add acquire lock");
                Couter.count++;
            } // 释放锁
            System.out.println("add release lock");
        }
    }
}

class DecThread extends Thread {
    public void run() {
        for (int i = 0; i < 100_000; i++) {
            synchronized (Couter.lock){
                System.out.println("dec acquire lock");
                Couter.count--;
            }
            System.out.println("dec release lock");
        }
    }
}

/*
* wait()方法的执行机制非常复杂。首先，它不是一个普通的Java方法，而是定义在Object类的一个native方法，也就是由JVM的C代码实现的。
* 其次，必须在synchronized块中才能调用wait()方法，因为wait()方法调用时，会释放线程获得的锁，wait()方法返回时，线程又会重新试图获得锁。
* wait只能在锁对象上调用
* */
class TaskQueue {
    Queue<String> queue = new LinkedList<>();

    public synchronized void addTask(String s) {
        this.queue.add(s);
        this.notify(); // 唤醒在this锁等待的线程
    }

    public synchronized String getTask() throws InterruptedException {
        while (queue.isEmpty()) {
            this.wait(); // 当一个线程执行到getTask()方法内部的while循环时，它必定已经获取到了this锁，此时，线程执行while条件判断，如果条件成立（队列为空），线程将执行this.wait()，进入等待状态
    }
        return queue.remove();
    }
}