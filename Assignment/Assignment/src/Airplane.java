import java.util.concurrent.ThreadLocalRandom;

public class Airplane implements Runnable {
    private final int airplaneId;
    private final String airplaneName;
    private final Airport airport;
    private static final int MAX_PASSENGERS = 50;
    private volatile boolean landingRequested = false;
    private Gate gate;

    private static final int MIN_AIRPLANE_RUNTIME_MS = 0;
    private static final int MAX_AIRPLANE_RUNTIME_MS = 2000;

    public Airplane(int airplaneId, String airplaneName, Airport airport) {
        this.airplaneId = airplaneId;
        this.airplaneName = airplaneName;
        this.airport = airport;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        try {
            requestLanding();

            airport.land(this);

            System.out.println(Thread.currentThread().getName() + ": " + airplaneName + ": Landing on runway.");

            while ((gate = airport.dock(this)) == null) {
                System.out.println(
                        Thread.currentThread().getName() + ": " + airplaneName + ": No gate available, waiting...");
                Thread.sleep(500); // Retry after some time if no gate is available
            }

            Thread disembarkThread = new Thread(() -> gate.disembarkPassengers(), "Thread-Disembark-" + airplaneId);
            Thread suppliesThread = new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + ": " + airplaneName
                        + ": Refilling supplies and cleaning started.");
                try {
                    Thread.sleep(Airplane.getRandomTime(200, 500)); // Accessing static method
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + ": " + airplaneName
                        + ": Refilling supplies and cleaning completed.");
            }, "Thread-Supplies-" + airplaneId);
            Thread embarkThread = new Thread(() -> {
                gate.embarkPassengers();
                airport.incrementPassengersBoarded(MAX_PASSENGERS);
            }, "Thread-Embark-" + airplaneId);

            disembarkThread.start();
            suppliesThread.start();
            embarkThread.start();

            disembarkThread.join();
            suppliesThread.join();
            embarkThread.join();

            airport.refuel(this);

            airport.undock(this, gate);
            System.out.println(Thread.currentThread().getName() + ": " + airplaneName + ": Taking off.");
            airport.takeOff(this);

            long endTime = System.currentTimeMillis();
            int waitingTime = (int) (endTime - startTime);
            airport.updateStatistics(waitingTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isLandingRequested() {
        return landingRequested;
    }

    public void setLandingRequested(boolean landingRequested) {
        this.landingRequested = landingRequested;
    }

    public String getAirplaneName() {
        return airplaneName;
    }

    private void requestLanding() {
        synchronized (this) {
            if (!landingRequested) {
                System.out.println(Thread.currentThread().getName() + ": " + airplaneName + ": Requesting landing.");
                try {
                    airport.requestLanding(this);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Preserve interrupt status
                    e.printStackTrace();
                }
                landingRequested = true;
            }
        }
    }

    public static int getRandomTime(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public int getAirplaneId() {
        return airplaneId;
    }

    public int getMaxPassengers() {
        return MAX_PASSENGERS;
    }
}
