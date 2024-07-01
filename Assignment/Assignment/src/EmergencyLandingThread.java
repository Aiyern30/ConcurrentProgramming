import java.util.concurrent.ThreadLocalRandom;

public class EmergencyLandingThread extends Thread {
    private final Airplane airplane;
    private final Airport airport;

    public EmergencyLandingThread(Airplane airplane, Airport airport) {
        this.airplane = airplane;
        this.airport = airport;
    }

    @Override
    public void run() {
        try {
            airplane.setLandingRequested(true);

            airport.land(airplane);
            System.out.println(
                    Thread.currentThread().getName() + ": " + airplane.getAirplaneName() + ": Landing on runway.");

            Gate gate = null; // Initialize gate outside the loop
            boolean docked = false;
            while (!docked) {
                gate = airport.dock(airplane);
                if (gate != null) {
                    docked = true;
                } else {
                    System.out.println(
                            Thread.currentThread().getName() + ": " + airplane.getAirplaneName()
                                    + ": No gate available, waiting...");
                    Thread.sleep(500); // Retry after some time if no gate is available
                }
            }

            final Gate finalGate = gate; // finalGate is effectively final for lambda use

            Thread disembarkThread = new Thread(() -> finalGate.disembarkPassengers(),
                    "Thread-Disembark-" + airplane.getAirplaneId());
            Thread suppliesThread = new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + ": " + airplane.getAirplaneName()
                        + ": Refilling supplies and cleaning started.");
                try {
                    Thread.sleep(Airplane.getRandomTime(200, 500)); // Corrected access to getRandomTime
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + ": " + airplane.getAirplaneName()
                        + ": Refilling supplies and cleaning completed.");
            }, "Thread-Supplies-" + airplane.getAirplaneId());
            Thread embarkThread = new Thread(() -> {
                finalGate.embarkPassengers();
                airport.incrementPassengersBoarded(airplane.getMaxPassengers()); // Assuming max passengers for
                                                                                 // emergency
            }, "Thread-Embark-" + airplane.getAirplaneId());

            disembarkThread.start();
            suppliesThread.start();
            embarkThread.start();

            disembarkThread.join();
            suppliesThread.join();
            embarkThread.join();

            airport.refuel(airplane);

            airport.undock(airplane, finalGate);
            System.out.println(Thread.currentThread().getName() + ": " + airplane.getAirplaneName() + ": Taking off.");
            airport.takeOff(airplane);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
