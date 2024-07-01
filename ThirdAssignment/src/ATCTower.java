import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ATCTower {
    private final Lock atcLock = new ReentrantLock(true);
    private final Semaphore gateSemaphore;
    private final Lock refuellingLock;
    private final PriorityQueue<Airplane> landingQueue = new PriorityQueue<>(
            Comparator.comparingInt(Airplane::getPriority));

    public ATCTower(Semaphore gateSemaphore, Lock refuellingLock) {
        this.gateSemaphore = gateSemaphore;
        this.refuellingLock = refuellingLock;
    }

    public void requestLanding(Airplane airplane) throws InterruptedException {
        atcLock.lock();
        try {
            if (airplane.isEmergency()) {
                landingQueue.add(airplane);
                System.out.println(Thread.currentThread().getName() + ": ATC: " + airplane.getAirplaneName()
                        + " is requesting emergency landing permission.");
            } else {
                landingQueue.add(airplane);
                System.out.println(Thread.currentThread().getName() + ": ATC: " + airplane.getAirplaneName()
                        + " is requesting landing permission.");
            }

            // Check if an emergency landing is required
            Airplane nextAirplane;
            while ((nextAirplane = landingQueue.poll()) != null) {
                if (nextAirplane.isEmergency()) {
                    gateSemaphore.acquire(); // Ensure a gate is available before granting landing permission
                    System.out.println(Thread.currentThread().getName() + ": ATC: " + nextAirplane.getAirplaneName()
                            + " is approved for emergency landing.");
                    return;
                } else if (gateSemaphore.tryAcquire()) {
                    System.out.println(Thread.currentThread().getName() + ": ATC: " + nextAirplane.getAirplaneName()
                            + " is approved for landing.");
                    return;
                } else {
                    landingQueue.add(nextAirplane); // Re-add to queue if no gates are available
                }
            }
        } finally {
            atcLock.unlock();
        }
    }

    public void requestTakeoff(Airplane airplane) throws InterruptedException {
        atcLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + ": ATC: " + airplane.getAirplaneName()
                    + " is requesting takeoff permission.");
            Thread.sleep(500); // Simulate ATC processing time
            System.out.println(Thread.currentThread().getName() + ": ATC: " + airplane.getAirplaneName()
                    + " is approved for takeoff.");
        } finally {
            atcLock.unlock();
        }
    }

    public void refuel(Airplane airplane) throws InterruptedException {
        System.out.println(
                Thread.currentThread().getName() + ": " + airplane.getAirplaneName() + " is waiting to refuel.");
        refuellingLock.lock();
        try {
            System.out
                    .println(Thread.currentThread().getName() + ": " + airplane.getAirplaneName() + " is refuelling.");
            Thread.sleep(2000); // Simulate refuelling time
            System.out.println(Thread.currentThread().getName() + ": " + airplane.getAirplaneName()
                    + " has completed refuelling.");
        } finally {
            refuellingLock.unlock();
        }
    }
}