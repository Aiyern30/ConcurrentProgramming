import java.util.concurrent.locks.Lock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Worker implements Runnable {

    public static Lock lock;

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " Created");
        lock.lock();
        System.out.println(Thread.currentThread().getName() + " Locked");
        try {
            for (int i = 0; i < 5; i++) {
                incrementCounter();
                System.out.println(
                        Thread.currentThread().getName() + " Counter: " + StructuredLocks_ReentrantLock.counter);
                Thread.sleep(1000);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            System.out.println(Thread.currentThread().getName() + " Released!");
            lock.unlock();
        }
    }

    private void incrementCounter() {
        StructuredLocks_ReentrantLock.counter++;
    }
}
