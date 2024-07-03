import java.util.Random;

public class PlaneGenerator extends Thread {
    private AirTrafficControl atc;
    private Random random;

    public PlaneGenerator(AirTrafficControl atc) {
        this.atc = atc;
        this.random = new Random();
    }

    @Override
    public void run() {
        int totalPlanes = 0;
        try {
            int choice = random.nextInt(2);

            if (choice == 0) {
                System.out.println("----- Normal airplane simulation -----");
                generateNormalScenario(totalPlanes);
            } else {
                System.out.println("----- Congestion scenario with emergency -----");
                generateCongestionScenario(totalPlanes);
            }

        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    private void generateNormalScenario(int totalPlanes) throws InterruptedException {
        // Generate 6 random planes
        for (int i = 0; i < 6; i++) {
            int passengers = random.nextInt((50 - 20) + 1) + 20;
            totalPlanes++;
            Flight flight = new Flight(totalPlanes, false, atc, passengers);
            Thread thread = new Thread(flight);
            thread.start();
            Thread.sleep(1000);
        }
    }

    private void generateCongestionScenario(int totalPlanes) throws InterruptedException {
        // Generate the first 2 planes to occupy gates
        for (int i = 0; i < 2; i++) {
            int passengers = random.nextInt((50 - 20) + 1) + 20;
            totalPlanes++;
            Flight flight = new Flight(totalPlanes, false, atc, passengers);
            Thread thread = new Thread(flight);
            thread.start();
            Thread.sleep(1000);
        }

        // Generate the next 2 planes to wait in queue
        for (int i = 0; i < 2; i++) {
            int passengers = random.nextInt((50 - 20) + 1) + 20;
            totalPlanes++;
            Flight flight = new Flight(totalPlanes, false, atc, passengers);
            Thread thread = new Thread(flight);
            thread.start();
            Thread.sleep(1000);
        }

        // Generate the 3rd plane with an emergency landing requirement
        int passengers = random.nextInt((50 - 20) + 1) + 20;
        totalPlanes++;
        Flight emergencyFlight = new Flight(totalPlanes, true, atc, passengers); // Mark as urgent
        Thread emergencyThread = new Thread(emergencyFlight);
        emergencyThread.start();

        // Generate one more plane to wait in queue
        passengers = random.nextInt((50 - 20) + 1) + 20;
        totalPlanes++;
        Flight additionalFlight = new Flight(totalPlanes, false, atc, passengers);
        Thread additionalThread = new Thread(additionalFlight);
        additionalThread.start();
    }
}
