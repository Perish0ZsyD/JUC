/**
 * @author Siyuan
 * @date 2024/10/11/14:24
 *///TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        // test for thread & sleep for juc simulation
        System.out.println("main start..."); // Thread.setPriority(int n); 0~10,默认5, 优先映射到操作系统实际优先级上
        Thread t = new Thread() {
            public void run() {
                System.out.println("Thread start...");
                try {
                    Thread.sleep(10); // ms
                } catch (InterruptedException e) {}
                System.out.println("Thread end...");
            }
        };
        t.start();  //  必须调用start()方法才会启动新线程，内部调用private native void start0()方法
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {}
        Thread t1 = new Thread(() -> {
            System.out.println("lambda thread...");
        });
        System.out.println("t1 start...");
        t1.start();
        t1.join(); // main会等待t1线程结束再结束
        System.out.println("main end..");
    }
}