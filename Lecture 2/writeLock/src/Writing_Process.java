import java.util.List;
import java.util.concurrent.locks.Lock;

public class Writing_Process implements Runnable {

    private final Lock writeLock;
    private final List<Integer> sharedList;

    public Writing_Process(Lock writeLock, List<Integer> sharedList) {
        this.writeLock = writeLock;
        this.sharedList = sharedList;
    }

    @Override
    public void run() {
        this.Add_Item();
    }

    public void Add_Item() {
        writeLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " Executed!");
            sharedList.add(100);
            System.out.println(Thread.currentThread().getName() + " One value");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                sharedList.set(0, 20);
                System.out.println(Thread.currentThread().getName() + " First Value");
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Handle interruption if needed
            }
        } finally {
            writeLock.unlock();
        }
    }
}
