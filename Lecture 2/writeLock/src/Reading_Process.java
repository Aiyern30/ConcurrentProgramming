import java.util.List;
import java.util.concurrent.locks.Lock;

public class Reading_Process implements Runnable {

    private final Lock readLock;
    private final List<Integer> sharedList;

    public Reading_Process(Lock readLock, List<Integer> sharedList) {
        this.readLock = readLock;
        this.sharedList = sharedList;
    }

    @Override
    public void run() {
        PrintList();
    }

    public void PrintList() {
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " Printing Thread Executed!");

            for (int i = 0; i < sharedList.size(); i++) {
                System.out.println(Thread.currentThread().getName() + " Value: " + sharedList.get(i));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // Handle interruption if needed
                }
            }
        } finally {
            readLock.unlock();
        }
    }
}
