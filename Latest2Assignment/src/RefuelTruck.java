import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RefuelTruck implements Runnable {
    private final Lock refuelLock = new ReentrantLock();
    private final AirTrafficControl atc;

    public RefuelTruck(AirTrafficControl atc) {
        this.atc = atc;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Wait for refueling requests
                Request request = atc.getRefuelRequest();
                if (request != null) {
                    refuelPlane(request.getPlane());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    public void refuelPlane(Plane plane) throws InterruptedException {
        refuelLock.lock();
        try {
            // Wait for the plane to be docked at a gate
            while (plane.getGate() == null) {
                Thread.sleep(100); // Adjust sleep time as needed
            }

            System.out.println("Refuel truck is refueling Flight " + plane.getID());
            Thread.sleep(3000); // Simulate refueling time
            plane.setRefueled(true);
            System.out.println("Flight " + plane.getID() + " has been refueled");
        } finally {
            refuelLock.unlock();
        }
    }

}
