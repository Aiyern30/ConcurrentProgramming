import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

public class StructuredLocks_ReentrantLock {

    public static final Lock lock = new ReentrantLock();
    public static int counter = 0;

    public static void main(String[] args) throws Exception {
        Worker.lock = lock; // Initialize the static lock in Worker class

        for (int i = 0; i < 5; i++) { // Corrected loop condition
            Thread thread = new Thread(new Worker());
            thread.start();
        }
    }
}
