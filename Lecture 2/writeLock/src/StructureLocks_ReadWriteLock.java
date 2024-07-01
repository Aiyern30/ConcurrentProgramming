import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Lock;

public class StructureLocks_ReadWriteLock {

    public static final List<Integer> sharedList = new ArrayList<>();
    public static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    public static final Lock readLock = lock.readLock();
    public static final Lock writeLock = lock.writeLock();

    public static void main(String[] args) throws Exception {

        sharedList.add(5);
        sharedList.add(3);
        sharedList.add(8);

        Writing_Process P1 = new Writing_Process(writeLock, sharedList);
        Reading_Process P2 = new Reading_Process(readLock, sharedList);

        Thread T1 = new Thread(P1);
        Thread T2 = new Thread(P2);

        T1.start();
        T2.start();

        // Uncomment these if needed
        // Thread T3 = new Thread(P2);
        // Thread T4 = new Thread(P1);
        // T3.start();
        // T4.start();
    }
}
