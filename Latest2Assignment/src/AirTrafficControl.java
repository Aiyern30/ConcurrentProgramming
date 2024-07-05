import java.util.concurrent.Semaphore;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.LinkedList;
import java.util.List;

public class AirTrafficControl implements Runnable {

    private final List<Gates> gates;
    private final BlockingQueue<Request> requests = new LinkedBlockingQueue<>();
    private final LinkedList<Plane> landingQueue = new LinkedList<>();
    private final RefuelTruck refuelTruck;

    public final Semaphore runwayLock = new Semaphore(1);

    private int planesServed = 0;
    private int availableGates;

    public AirTrafficControl(List<Gates> gates) {
        this.gates = gates;
        this.refuelTruck = new RefuelTruck(this);
        Thread refuelThread = new Thread(refuelTruck);
        refuelThread.start();
        this.availableGates = gates.size();
        System.out.println("Number of gates : " + availableGates);
    }

    @Override
    public void run() {
        try {
            while (!requests.isEmpty() || !landingQueue.isEmpty() || planesServed < Simulation.NUM_PLANES) {
                if (!requests.isEmpty()) {
                    processRequest(requests.take());
                }

                synchronized (landingQueue) {
                    if (!landingQueue.isEmpty() && availableGates > 0) {
                        availableGates--;
                        approveLanding(landingQueue.poll());
                    }
                }

                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    public void addRequest(Request request) {
        try {
            requests.put(request);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    public Request getRefuelRequest() throws InterruptedException {
        return requests.take();
    }

    private void processRequest(Request request) throws InterruptedException {
        switch (request.getType()) {
            case LANDING:
                System.out.println("ATC: Flight " + request.getPlane().getID() + " is requesting landing!");
                synchronized (landingQueue) {
                    landingQueue.addLast(request.getPlane());
                }
                break;
            case EMERGENCY_LANDING:
                synchronized (landingQueue) {
                    landingQueue.addFirst(request.getPlane());
                }
                break;
            case TAKEOFF:
                approveTakeOff(request.getPlane());
                break;
            case ASSIGN_GATE:
                assignGate(request.getPlane());
                break;
            case RELEASE_GATE:
                releaseGate(request.getPlane());
                break;
            case REFUEL:
                refuelPlane(request.getPlane());
                break;

            default:
                throw new AssertionError();
        }
    }

    private void refuelPlane(Plane plane) throws InterruptedException {
        synchronized (plane) {
            while (plane.getGate() == null) {
                plane.wait(); // Wait until plane is assigned a gate
            }
        }

        // Perform refueling
        System.out.println("ATC: Initiating refueling for Flight " + plane.getID());
        Thread.sleep(3000); // Simulate refueling time
        plane.setRefueled(true);
        System.out.println("ATC: Flight " + plane.getID() + " has been refueled");
    }

    private void approveLanding(Plane plane) throws InterruptedException {
        runwayLock.acquire();
        System.out.println("ATC: Flight " + plane.getID() + " is approved to land on the runway");
        plane.setLandingClearance(true);
    }

    private void approveTakeOff(Plane plane) throws InterruptedException {
        runwayLock.acquire();
        try {
            System.out.println("ATC: Flight " + plane.getID() + " is approved to takeoff on the runway");
            plane.setTakeoffClearance(true);
            availableGates++;
            planesServed++;
            System.out.println("Available Gates: " + availableGates);
        } finally {
            runwayLock.release();
        }
    }

    private synchronized void assignGate(Plane plane) throws InterruptedException {
        for (Gates gate : gates) {
            if (!gate.isOccupied()) {
                System.out.println("ATC: Flight " + plane.getID() + " docking at gate " + gate.getID());
                gate.occupy(plane);
                plane.setGate(gate);
                break;
            }
        }
        System.out.println("Available Gates: " + availableGates);
    }

    private synchronized void releaseGate(Plane plane) throws InterruptedException {
        System.out.println("ATC: Flight " + plane.getID() + " releasing from gate " + plane.getGate().getID());
        plane.getGate().release();
        plane.setGate(null);
    }
}
