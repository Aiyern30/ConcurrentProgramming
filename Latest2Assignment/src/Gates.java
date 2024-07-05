import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Gates implements Runnable {
    private final int ID;
    private boolean isGateOccupied;
    private Plane plane;
    private final Passenger[] newPassengers;

    private ExecutorService disembarkExecutor;
    private ExecutorService boardExecutor;

    private Thread resupplyThread;
    private Thread cleaningThread;

    public Gates(int ID) {
        this.ID = ID;
        this.isGateOccupied = false;
        this.newPassengers = Simulation.generatePassengers(null, false);
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

            disembarkAllPassengers();
            Thread.sleep(1000);
            boardAllPassengers();

            resupplyThread.join();
            cleaningThread.join();
            synchronized (plane) {
                plane.setGateOperationsCompleted(true);
                plane.notifyAll();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    public void startOperations() throws InterruptedException {
        Thread gateThread = new Thread(this);
        gateThread.start();
        try {
            gateThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    private void disembarkAllPassengers() throws InterruptedException {
        System.out.println("Gate " + ID + " disembarking passengers from " + plane.getID());
        for (Passenger passenger : plane.getPassengers()) {
            passenger.setPlane(plane);
            disembarkExecutor.execute(passenger);
        }

        disembarkExecutor.shutdown();
        disembarkExecutor.awaitTermination(15, TimeUnit.SECONDS);
        System.out.println("Flight " + plane.getID() + " finished disembarking passengers in Gate " + ID);
    }

    private void boardAllPassengers() throws InterruptedException {
        System.out.println("Gate " + ID + " finished boarding passengers onto flight " + plane.getID());
        for (Passenger passenger : newPassengers) {
            passenger.setPlane(plane);
            boardExecutor.execute(passenger);
        }

        plane.setPassengers(newPassengers);
        boardExecutor.shutdown();
        boardExecutor.awaitTermination(15, TimeUnit.SECONDS);
        System.out.println("Gate " + ID + " finished boarding passengers onto " + plane.getID());
    }

    private void cleanPlane() throws InterruptedException {
        System.out.println("Gate " + ID + " cleaning flight " + plane.getID());
        Thread.sleep(2000);
        System.out.println("Gate " + ID + " finished cleaning flight " + plane.getID());
    }

    private void resupplyPlane() throws InterruptedException {
        System.out.println("Gate " + ID + " resupplying flight " + plane.getID());
        Thread.sleep(3000);
        System.out.println("Gate " + ID + " finished resupplying flight " + plane.getID());
    }

    public synchronized boolean isOccupied() {
        return isGateOccupied;
    }

    public synchronized void occupy(Plane plane) {
        isGateOccupied = true;
        this.plane = plane;
    }

    public synchronized void release() {
        isGateOccupied = false;
        this.plane = null;
    }

    public int getID() {
        return ID;
    }
}
