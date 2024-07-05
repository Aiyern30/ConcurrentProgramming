import java.util.concurrent.Semaphore;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.LinkedList;
import java.util.List;

public class AirTrafficControl implements Runnable {

    private final List<Gates> gates;
    private final BlockingQueue<Request> requestQueue = new LinkedBlockingQueue<>();
    private final LinkedList<Plane> landingQueue = new LinkedList<>();
    public final Semaphore runwaySemaphore = new Semaphore(1);

    private int processedPlanesCount = 0;
    private int availableGatesCount;

    public AirTrafficControl(List<Gates> gates) {
        this.gates = gates;
        availableGatesCount = gates.size() - 1;
        System.out.println("Number of gates : " + availableGatesCount);
    }

    @Override
    public void run() {
        try {
            while (!requestQueue.isEmpty() || !landingQueue.isEmpty() || processedPlanesCount < Simulation.NUM_PLANES) {
                if (!requestQueue.isEmpty()) {
                    handleRequest(requestQueue.take());
                }

                if (!landingQueue.isEmpty() && availableGatesCount > 0) {
                    availableGatesCount--;
                    approveToLand(landingQueue.peek());
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
            requestQueue.put(request);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    private void handleRequest(Request request) throws InterruptedException {
        switch (request.getType()) {
            case LANDING:
                System.out.println("ATC: Flight " + request.getPlane().getID() + ", is landing on runway");
                landingQueue.addLast(request.getPlane());
                break;
            case EMERGENCY_LANDING:
                landingQueue.addFirst(request.getPlane());
                break;
            case TAKEOFF:
                approveToTakeOff(request.getPlane());
                break;
            case REQUEST_GATE:
                assignGate(request.getPlane());
                break;
            case RELEASE_GATE:
                releaseGate(request.getPlane());
                break;
            case REFUEL:
                // implement fuel truck call here
                break;
            default:
                throw new AssertionError();
        }
    }

    private void approveToLand(Plane plane) throws InterruptedException {
        runwaySemaphore.acquire();
        System.out.println("ATC: Flight " + plane.getID() + " is granted permission to land on the runway");
        plane.setLandingClearance(true);
        landingQueue.remove();
    }

    private void approveToTakeOff(Plane plane) throws InterruptedException {
        runwaySemaphore.acquire();
        System.out.println("ATC: Flight " + plane.getID() + " is granted permission to take off on the runway");
        plane.setTakeoffClearance(true);
        availableGatesCount++;
        processedPlanesCount++;
        System.out.println("Available Gates : " + availableGatesCount);
    }

    private void assignGate(Plane plane) throws InterruptedException {
        for (Gates gate : gates) {
            if (!gate.isOccupied()) {
                System.out.println("ATC: Flight " + plane.getID() + " dock at gate " + gate.getID());
                gate.assignPlane(plane);
                plane.setGate(gate);
                break;
            }
        }
        System.out.println("Available Gates : " + availableGatesCount);
    }

    private void releaseGate(Plane plane) throws InterruptedException {
        System.out.println("ATC: Flight " + plane.getID() + " release from gate " + plane.getGate().getID());
        plane.getGate().releasePlane();
        plane.setGate(null);
    }
}
