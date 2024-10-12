import java.util.concurrent.locks.StampedLock;

/**
 * 乐观锁的意思就是乐观地估计读的过程中大概率不会有写入，因此被称为乐观锁。
 * 反过来，悲观锁则是读的过程中拒绝有写入，也就是写入必须等待。显然乐观锁的并发效率更高，但一旦有小概率的写入导致读取的数据不一致，需要能检测出来，再读一遍就行。
 *
 * @author Siyuan
 * @Date 2024/10/12 17:16
 */
public class OptimisticLockLearning {
    public static void main(String[] args) throws InterruptedException {
        Point point = new Point(100, 200);
        System.out.println("primary point: " + point + " distance: " + point.distanceFromOrigin());
        Thread t = new Thread() {
            public void run() {
                point.move(200, 200);
            }
        };
        t.start();
        System.out.println("secondary point: " + point + " distance: " + point.distanceFromOrigin());
//        t.join();

    }
}

class Point {
    private final StampedLock stampedLock = new StampedLock();

    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void move(double deltaX, double deltaY) {
        long stamp = stampedLock.writeLock(); // 获取写锁
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            stampedLock.unlockWrite(stamp); // 释放写锁
        }
    }

    /*
     * 到首先我们通过tryOptimisticRead()获取一个乐观读锁，并返回版本号。
     * 接着进行读取，读取完成后，我们通过validate()去验证版本号，
     * 如果在读取过程中没有写入，版本号不变，验证成功，我们就可以放心地继续后续操作。
     * 如果在读取过程中有写入，版本号会发生变化，验证将失败。
     * 在失败的时候，我们再通过获取悲观读锁再次读取。
     * 由于写入的概率不高，程序在绝大部分情况下可以通过乐观读锁获取数据，
     * 极少数情况下使用悲观读锁获取数据*/

    public double distanceFromOrigin() {
        long stamp = stampedLock.tryOptimisticRead(); // 获得一个乐观读锁
        // 注意 下面两行代码不是原子操作
        // 假设x, y = (100, 200)
        double currentX = x;
        // 此时读取x = 100 但是x，y可能被写线程修改为(300, 400)
        double currentY = y;
        // 此处已读取到y，如果没有写入，读取正确应该是(100, 200)
        // 如果写入，读取时错误的(100, 400)
        if (!stampedLock.validate(stamp)) { // 检查乐观读锁后是否有其他写锁发生
            stamp = stampedLock.readLock(); // 获得一个悲观读锁
            try {
                currentX = x;
                currentY = y;
            } finally {
                stampedLock.unlockRead(stamp); // 释放悲观读锁
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }

    @Override
    public String toString() {
        return "x: " + x + ", y: " + y;
    }
}
