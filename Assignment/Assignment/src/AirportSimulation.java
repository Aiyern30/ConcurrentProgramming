import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class AirportSimulation {
    public static void main(String[] args) {
        final int NUM_RUNWAYS = 1;
        final int NUM_GATES = 3;
        final int NUM_AIRPLANES = 6;

        Airport airport = new Airport(NUM_RUNWAYS, NUM_GATES);
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(NUM_AIRPLANES + 1);

        // Schedule regular airplanes
        for (int i = 0; i < NUM_AIRPLANES; i++) {
            Airplane airplane = new Airplane(i, "Airplane-" + i, airport);
            executorService.schedule(airplane, ThreadLocalRandom.current().nextInt(0, 3001), TimeUnit.MILLISECONDS);
        }

        // Schedule emergency airplane
        Airplane emergencyAirplane = new Airplane(NUM_AIRPLANES, "Airplane-Emergency", airport);
        EmergencyLandingThread emergencyLandingThread = new EmergencyLandingThread(emergencyAirplane, airport);
        executorService.execute(emergencyLandingThread);

        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        airport.printStatistics();
        airport.printSanityCheck();
    }
}
