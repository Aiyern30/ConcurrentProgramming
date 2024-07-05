import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Gates implements Runnable {
    private final int ID;
    private boolean isOccupied;
    private Plane plane; // current plane docked at the gate
    private final Passenger[] passengers; // passengers at gate waiting to board

    private ExecutorService disembarkExecutor;
    private ExecutorService boardExecutor;
    private Thread resupplyThread;
    private Thread cleaningThread;

    public Gates(int ID) {
        this.ID = ID;
        this.isOccupied = false;
        this.passengers = new Passenger[Simulation.MAX_PASSENGERS]; // Initialize passenger array
    }

    @Override
    public void run() {
        disembarkExecutor = Executors.newSingleThreadExecutor();
        boardExecutor = Executors.newSingleThreadExecutor();

        resupplyThread = new Thread(() -> {
            try {
                resupplyPlane();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        });

        cleaningThread = new Thread(() -> {
            try {
                cleanPlane();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        });

        try {
            resupplyThread.start();
            cleaningThread.start();

            // Perform operations
            disembarkPassengers();
            Thread.sleep(1000); // Simulate time between disembarking and boarding
            boardPassengers();

            resupplyThread.join();
            cleaningThread.join();
            plane.setGateOperationsCompleted(true);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    public void startOperations() {
        Thread gateThread = new Thread(this);
        gateThread.start();
        try {
            gateThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    private void disembarkPassengers() {
        System.out.println("Gate " + ID + " disembarking passengers from Flight " + plane.getID());
        for (Passenger passenger : plane.getPassengers()) {
            if (passenger != null) {
                passenger.setPlane(plane);
                disembarkExecutor.execute(passenger);
            }
        }

        disembarkExecutor.shutdown();
        try {
            disembarkExecutor.awaitTermination(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        System.out.println("Gate " + ID + " finished disembarking passengers from Flight " + plane.getID());
    }

    private void boardPassengers() {
        System.out.println("Gate " + ID + " boarding passengers onto Flight " + plane.getID());
        for (Passenger passenger : passengers) {
            if (passenger != null) {
                passenger.setPlane(plane);
                boardExecutor.execute(passenger);
            }
        }

        plane.setPassengers(passengers); // Set passengers for the plane
        boardExecutor.shutdown();
        try {
            boardExecutor.awaitTermination(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        System.out.println("Gate " + ID + " finished boarding passengers onto Flight " + plane.getID());
    }

    private void cleanPlane() throws InterruptedException {
        System.out.println("Gate " + ID + " cleaning Flight " + plane.getID());
        Thread.sleep(2000); // Simulate time to clean the plane
        System.out.println("Gate " + ID + " finished cleaning Flight " + plane.getID());
    }

    private void resupplyPlane() throws InterruptedException {
        System.out.println("Gate " + ID + " resupplying Flight " + plane.getID());
        Thread.sleep(3000); // Simulate time to resupply the plane
        System.out.println("Gate " + ID + " finished resupplying Flight " + plane.getID());
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void assignPlane(Plane plane) {
        isOccupied = true;
        this.plane = plane;
    }

    public void releasePlane() {
        isOccupied = false;
        this.plane = null;
    }

    public int getID() {
        return ID;
    }
}
