import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Airport {
    private final Lock runwayLock = new ReentrantLock(true); // Fair lock to ensure FIFO
    private final Lock gateLock = new ReentrantLock(true);
    private final int maxPlanesOnGround = 3;
    private int planesOnGround = 0;

    public void requestLanding(Airplane airplane) throws InterruptedException {
        runwayLock.lock();
        try {
            System.out.println(airplane.getAirplaneName() + " is landing.");
            Thread.sleep(1000); // Simulate landing time
            planeLanded(airplane);
        } finally {
            runwayLock.unlock();
        }
    }

    public synchronized void planeLanded(Airplane airplane) throws InterruptedException {
        while (planesOnGround >= maxPlanesOnGround) {
            System.out.println(airplane.getAirplaneName() + " is waiting for a gate.");
            wait();
        }
        planesOnGround++;
        System.out.println(airplane.getAirplaneName() + " has landed and is taxiing to the gate.");
        airplane.setAtGate(true);
        gateLock.lock();
        try {
            System.out.println(airplane.getAirplaneName() + " is at the gate.");
            Thread.sleep(1000); // Simulate gate operations time
        } finally {
            gateLock.unlock();
        }
    }

    public synchronized void requestTakeoff(Airplane airplane) throws InterruptedException {
        runwayLock.lock();
        try {
            System.out.println(airplane.getAirplaneName() + " is taking off.");
            Thread.sleep(1000); // Simulate takeoff time
            planeTookOff(airplane);
        } finally {
            runwayLock.unlock();
        }
    }

    public synchronized void planeTookOff(Airplane airplane) {
        planesOnGround--;
        airplane.setAtGate(false);
        System.out.println(airplane.getAirplaneName() + " has taken off.");
        notifyAll();
    }
}
