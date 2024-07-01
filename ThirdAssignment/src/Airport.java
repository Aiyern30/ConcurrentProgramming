import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Airport {
    private final Lock runwayLock = new ReentrantLock(true); // Fair lock to ensure FIFO for runway
    private final Semaphore gateSemaphore = new Semaphore(3, true); // Semaphore to manage 3 gates
    private final Lock refuellingLock = new ReentrantLock(true); // Lock for exclusive refuelling
    private final ExecutorService executorService = Executors.newFixedThreadPool(6); // Thread pool for ground
                                                                                     // operations
    private final AtomicInteger airplanesCompleted = new AtomicInteger(0); // Counter for completed airplanes
    private final AtomicBoolean isShuttingDown = new AtomicBoolean(false); // Flag to indicate shutdown

    private final ATCTower atcTower;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    // Statistics fields
    private final List<Long> waitingTimes = new ArrayList<>();
    private final AtomicInteger airplanesServed = new AtomicInteger(0);
    private int totalPassengers = 0;

    public Airport() {
        this.atcTower = new ATCTower(gateSemaphore, refuellingLock);
    }

    public void startAirport() {
        scheduler.scheduleAtFixedRate(this::generateAirplane, 0, 1, TimeUnit.SECONDS);
    }

    public void generateAirplane() {
        if (isShuttingDown.get()) {
            return;
        }
        String airplaneName = "Airplane " + (Airplane.getPlaneCount() + 1);
        boolean emergency = ThreadLocalRandom.current().nextBoolean(); // Randomly decide if emergency
        Airplane airplane = new Airplane(airplaneName, this, emergency);
        airplane.start();

        int delay = ThreadLocalRandom.current().nextInt(0, 3); // Random delay between 0, 1, or 2 seconds
        scheduler.schedule(this::generateAirplane, delay, TimeUnit.SECONDS);
    }

    public void requestLanding(Airplane airplane) throws InterruptedException {
        runwayLock.lock();
        try {
            if (isShuttingDown.get()) {
                return;
            }
            long startTime = System.currentTimeMillis();
            atcTower.requestLanding(airplane);
            long endTime = System.currentTimeMillis();
            waitingTimes.add(endTime - startTime);
            System.out.println(Thread.currentThread().getName() + ": " + airplane.getAirplaneName() + " is landing.");
            Thread.sleep(1000); // Simulate landing time
            planeLanded(airplane);
        } finally {
            runwayLock.unlock();
        }
    }

    private void planeLanded(Airplane airplane) throws InterruptedException {
        int gateNumber = getGateNumber();
        System.out.println(Thread.currentThread().getName() + ": " + airplane.getAirplaneName()
                + " has landed and is taxiing to Gate " + gateNumber + ".");
        airplane.setGateNumber(gateNumber);
        airplane.setAtGate(true);
        System.out.println(Thread.currentThread().getName() + ": " + airplane.getAirplaneName() + " is docking at Gate "
                + gateNumber + ".");
        Thread.sleep(1000); // Simulate docking time

        executorService.execute(() -> handleGroundOperations(airplane, gateNumber));
    }

    private int getGateNumber() {
        return ThreadLocalRandom.current().nextInt(1, 4); // Random gate assignment between 1 and 3
    }

    private void handleGroundOperations(Airplane airplane, int gateNumber) {
        try {
            if (isShuttingDown.get()) {
                return;
            }
            System.out.println(Thread.currentThread().getName() + ": " + airplane.getAirplaneName()
                    + " is disembarking passengers at Gate " + gateNumber + ".");
            for (Passenger passenger : airplane.getPassengers()) {
                passenger.start();
                passenger.join(); // Wait for passenger to complete disembarking
                System.out.println(Thread.currentThread().getName() + ": " + airplane.getAirplaneName()
                        + " disembarked passenger " + passenger.getPassengerName() + ".");
            }
            System.out.println(Thread.currentThread().getName() + ": " + airplane.getAirplaneName()
                    + " disembarked total " + airplane.getPassengers().size() + " passengers.");

            totalPassengers += airplane.getPassengers().size();
            System.out.println(Thread.currentThread().getName() + ": " + airplane.getAirplaneName()
                    + " is refilling supplies and cleaning.");
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 2000)); // Simulate refilling and cleaning time
            atcTower.refuel(airplane); // Use refuelling method from ATCTower
            System.out.println(Thread.currentThread().getName() + ": " + airplane.getAirplaneName()
                    + " is boarding new passengers at Gate " + gateNumber + ".");
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 2000)); // Simulate boarding time
            System.out.println(Thread.currentThread().getName() + ": " + airplane.getAirplaneName()
                    + " is undocking from Gate " + gateNumber + ".");
            Thread.sleep(1000); // Simulate undocking time
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + ": " + airplane.getAirplaneName()
                    + " was interrupted during ground operations.");
            Thread.currentThread().interrupt();
        } finally {
            airplane.setAtGate(false);
            gateSemaphore.release(); // Release semaphore to allow other airplanes to use the gate
            try {
                requestTakeoff(airplane); // Proceed to takeoff
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + ": " + airplane.getAirplaneName()
                        + " was interrupted while requesting takeoff.");
                Thread.currentThread().interrupt();
            }
        }
    }

    public void requestTakeoff(Airplane airplane) throws InterruptedException {
        runwayLock.lock();
        try {
            if (isShuttingDown.get()) {
                return;
            }
            System.out.println(Thread.currentThread().getName() + ": " + airplane.getAirplaneName()
                    + " is taxiing to the runway.");
            Thread.sleep(1000); // Simulate taxiing time
            System.out
                    .println(Thread.currentThread().getName() + ": " + airplane.getAirplaneName() + " is taking off.");
            Thread.sleep(2000); // Simulate takeoff time
            System.out
                    .println(Thread.currentThread().getName() + ": " + airplane.getAirplaneName() + " has taken off.");
        } finally {
            runwayLock.unlock();
            airplanesServed.incrementAndGet();
            airplanesCompleted.incrementAndGet();
            if (airplanesCompleted.get() == Airplane.getPlaneCount()) {
                shutdown(); // Trigger shutdown when all airplanes have completed their journeys
            }
        }
    }

    public void shutdown() {
        if (isShuttingDown.getAndSet(true)) {
            return; // Shutdown already in progress
        }

        executorService.shutdown();
        scheduler.shutdown();

        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
            scheduler.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            System.out.println("Airport shutdown interrupted.");
            Thread.currentThread().interrupt();
        }

        // Print statistics
        System.out.println("Sanity checks and statistics:");
        checkEmptyGates();
        printStatistics();
        System.out.println("Airport shutdown complete.");
    }

    private void checkEmptyGates() {
        if (gateSemaphore.availablePermits() == 3) {
            System.out.println("All gates are empty.");
        } else {
            System.out.println("Error: Not all gates are empty.");
        }
    }

    private void printStatistics() {
        if (!waitingTimes.isEmpty()) {
            long maxWaitingTime = waitingTimes.stream().max(Long::compareTo).orElse(0L);
            long minWaitingTime = waitingTimes.stream().min(Long::compareTo).orElse(0L);
            double averageWaitingTime = waitingTimes.stream().mapToLong(Long::valueOf).average().orElse(0.0);

            System.out.println("Statistics:");
            System.out.println("Maximum waiting time: " + maxWaitingTime + " ms");
            System.out.println("Minimum waiting time: " + minWaitingTime + " ms");
            System.out.println("Average waiting time: " + averageWaitingTime + " ms");
        }

        System.out.println("Number of planes served: " + airplanesServed.get());
        System.out.println("Total passengers boarded: " + totalPassengers);
    }
}