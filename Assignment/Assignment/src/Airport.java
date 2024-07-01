import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class Airport {
    private final List<Runway> runways;
    private final List<Gate> gates;
    private final Semaphore landingApprovalSemaphore; // Semaphore for landing approval

    private int totalPassengersBoarded = 0;
    private int totalWaitingTime = 0;
    private int maxWaitingTime = Integer.MIN_VALUE;
    private int minWaitingTime = Integer.MAX_VALUE;
    private int airplaneCount = 0;
    private int emergencyPassengerCount = 0;

    // Constants
    private static final int MAX_AIRPLANES_ON_GROUNDS = 3;

    public Airport(int numRunways, int numGates) {
        runways = new ArrayList<>(numRunways);
        gates = new ArrayList<>(numGates);
        landingApprovalSemaphore = new Semaphore(1, true); // Only one permit for landing, fair ordering

        for (int i = 0; i < numRunways; i++) {
            runways.add(new Runway());
        }
        for (int i = 0; i < numGates; i++) {
            gates.add(new Gate(i + 1)); // Gate numbers start from 1
        }
    }

    public void requestLanding(Airplane airplane) throws InterruptedException {
        System.out.println(
                Thread.currentThread().getName() + ": " + airplane.getAirplaneName() + ": Requesting landing.");
        landingApprovalSemaphore.acquire(); // Acquire a permit for landing
        System.out
                .println(Thread.currentThread().getName() + ": " + airplane.getAirplaneName() + ": Landing approved.");
    }

    public void land(Airplane airplane) throws InterruptedException {
        Runway runway = getAvailableRunway();
        if (runway != null) {
            runway.land(airplane);
        }
    }

    public Gate dock(Airplane airplane) {
        synchronized (gates) {
            Gate availableGate = getAvailableGate();
            if (availableGate != null) {
                availableGate.setAvailable(false);
                System.out.println(Thread.currentThread().getName() + ": " + airplane.getAirplaneName()
                        + ": Docking at Gate " + availableGate.getGateNumber());
            } else {
                System.out.println(Thread.currentThread().getName() + ": " + airplane.getAirplaneName()
                        + ": No gate available, unable to dock.");
            }
            return availableGate;
        }
    }

    public void undock(Airplane airplane, Gate gate) {
        synchronized (gates) {
            gate.setAvailable(true);
            System.out.println(Thread.currentThread().getName() + ": " + airplane.getAirplaneName() + ": Leaving Gate "
                    + gate.getGateNumber());
        }
    }

    public void takeOff(Airplane airplane) throws InterruptedException {
        Runway runway = getAvailableRunway();
        if (runway != null) {
            runway.takeOff(airplane);
        }
    }

    public void refuel(Airplane airplane) throws InterruptedException {
        synchronized (this) {
            System.out.println(
                    Thread.currentThread().getName() + ": " + airplane.getAirplaneName() + ": Refueling started.");
            Thread.sleep(getRandomTime(1000, 3000)); // Simulate refueling time
            System.out.println(
                    Thread.currentThread().getName() + ": " + airplane.getAirplaneName() + ": Refueling completed.");
        }
    }

    public void incrementPassengersBoarded(int count) {
        totalPassengersBoarded += count;
    }

    public void incrementEmergencyPassengers(int count) {
        emergencyPassengerCount += count;
    }

    public void updateStatistics(int waitingTime) {
        totalWaitingTime += waitingTime;
        maxWaitingTime = Math.max(maxWaitingTime, waitingTime);
        minWaitingTime = Math.min(minWaitingTime, waitingTime);
        airplaneCount++;
    }

    public void printStatistics() {
        System.out.println("=== Airport Statistics ===");
        System.out.println("Total airplanes processed: " + airplaneCount);
        System.out.println("Total passengers boarded: " + totalPassengersBoarded);
        System.out.println(
                "Average waiting time: " + (airplaneCount == 0 ? 0 : totalWaitingTime / airplaneCount) + " ms");
        System.out.println("Maximum waiting time: " + maxWaitingTime + " ms");
        System.out.println("Minimum waiting time: " + minWaitingTime + " ms");
        System.out.println("Emergency passengers processed: " + emergencyPassengerCount);
    }

    public void printSanityCheck() {
        System.out.println("=== Sanity Check ===");
        boolean gatesAvailable = gates.stream().allMatch(Gate::isAvailable);
        System.out.println("All gates empty: " + gatesAvailable);
    }

    private Runway getAvailableRunway() throws InterruptedException {
        Runway availableRunway = null;
        for (Runway runway : runways) {
            if (runway.isAvailable()) {
                availableRunway = runway;
                break;
            }
        }
        return availableRunway;
    }

    private Gate getAvailableGate() {
        return gates.stream().filter(Gate::isAvailable).findFirst().orElse(null);
    }

    private static int getRandomTime(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
