import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 线程池内部维护了若干个线程，没有任务的时候，这些线程都处于等待状态。如果有新任务，就分配一个空闲线程执行。如果所有线程都处于忙碌状态，新任务要么放入队列等待，要么增加一个新线程进行处理。
 *
 * @author Siyuan
 * @Date 2024/10/13 10:26
 */
public class ThreadPoolLearn {
    public static void main(String[] args) {
//        // 创建一个固定大小的线程池
//        ExecutorService es = Executors.newFixedThreadPool(4);
//        for (int i = 0; i < 6; i++) {
//            es.execute(new Task("" + i));
//        }
//        // 关闭线程池,等待正在执行的任务先完成，然后再关闭；shutdownNow会立即停止正在执行的任务
//        // awaitTermination则会等待指定的时间让线程池关闭
//        es.shutdown();

        // ScheduledThreadPool定期反复执行
        /*Java标准库还提供了一个java.util.Timer类，这个类也可以定期执行任务，
        但是，一个Timer会对应一个Thread，所以，一个Timer只能定期执行一个任务，多个定时任务必须启动多个Timer，
        而一个ScheduledThreadPool就可以调度多个定时任务，
        所以，我们完全可以用ScheduledThreadPool取代旧的Timer*/
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(4);
        // 1s后执行一次性任务
        ses.schedule(new Task("one-time"), 1, TimeUnit.SECONDS);

        // 2s后开始执行定时任务，每3s执行,任务总是以固定时间间隔触发，不管任务执行多长时间
        ses.scheduleAtFixedRate(new Task("fixed-rate"), 2, 3, TimeUnit.SECONDS);

        // 3s为间隔执行;上一个任务完成后，等待固定时间间隔执行下一次任务
        ses.scheduleWithFixedDelay(new Task("fixed-delay"), 2, 3, TimeUnit.SECONDS);
    }
}

class Task implements Runnable {
    private final String name;
    public Task(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("start " + name);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }
        System.out.println("end " + name);
    }
}
