import java.time.LocalTime;

/**
 * @author Siyuan
 * @date 2024/10/11/14:49
 */
public class ThreadLearning {
    // 线程基本操作
//    public static void main(String[] args) throws InterruptedException{
//        Thread t = new MyThread();
//        t.start();
//        Thread.sleep(1); // 暂定1ms
//        t.interrupt(); // 中断t线程 输出 1 hello!，注意此处是向线程t发出中断请求，能否立即响应需要看t线程代码，比如while循环不断检测isInterrupted
//        t.join(); // 等待t线程结束
//        System.out.println("main end");
//    }
    // join和interrupted测试
//    public static void main(String[] args) throws InterruptedException{
//        Thread t = new MyThread();
//        t.start();
//        Thread.sleep(1000); // 暂定1000ms
//        t.interrupt(); // 中断t线程 输出 1 hello!，注意此处是向线程t发出中断请求(置isInterrupted为0)，能否立即响应需要看t线程代码，比如while循环不断检测isInterrupted
//        t.join(); // 等待t线程结束
//        System.out.println("main end");
//    }
    // 守护线程test
    public static void main(String[] args) throws InterruptedException {
        Thread t = new TimerThread();
        t.setDaemon(true);
        t.start();
        t.join();
        System.out.println("main end...");
    }
}

/*
 * main线程通过调用t.interrupt()从而通知t线程中断，而此时t线程正位于hello.join()的等待中，此方法会立刻结束等待并抛出InterruptedException。由于我们在t线程中捕获了InterruptedException，
 * 因此，就可以准备结束该线程。在t线程结束前，对hello线程也进行了interrupt()调用通知其中断。如果去掉这一行代码，可以发现hello线程仍然会继续运行，且JVM不会退出。
*/
class MyThread extends Thread {
    public void run() {
        Thread hello = new HelloThread();
        hello.start();
        try {
            hello.join();
        } catch (InterruptedException e) {
            System.out.println("Interrupted!");
        }
        hello.interrupt(); // 在t线程结束前，对hello线程也进行了interrupt()调用通知其中断，这样如果t线程结束，hello线程也会结束
    }
}

class HelloThread extends Thread {
    public void run() {
        int n = 0;
        while (!isInterrupted()) { // MyThread -> hello.interrupted()
            n ++;
            System.out.println(n + "hello!");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}

/*
* JAVA程序入口是JVM启动的main线程，main线程启动其他线程，当所有线程都退出，JVM退出，进程结束
* 但有的线程目的是无限循环，如一个定时触发任务
* 守护线程 负责，非守护线程结束后JVM退出，注意setDaemon(true)   把该线程标记为守护线程
* 守护线程不能持有任何需要关闭的资源，例如打开文件等，因为虚拟机退出时，守护线程没有任何机会来关闭文件，这会导致数据丢失。
* */

class TimerThread extends Thread {
    @Override
    public void run() {
        while (true) {
            System.out.println(LocalTime.now());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}