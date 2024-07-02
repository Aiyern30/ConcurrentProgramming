import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class AirTrafficControl extends Thread {

    Semaphore runwaySemaphore = new Semaphore(1);
    Semaphore gateSemaphore = new Semaphore(3);

    Deque<Flight> waitingFlights;
    FuelService fuelService = new FuelService();

    AtomicInteger currentCapacity = new AtomicInteger(0);

    List<Gate> gates;

    public AirTrafficControl() {
        waitingFlights = new LinkedList<>();
        gates = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            gates.add(new Gate(i));
        }
    }

    @Override
    public void run() {
        System.out.println("Air Traffic Control: Online and operational.");
    }

    public void requestToLand() throws InterruptedException {
        synchronized (waitingFlights) {
            if (gateSemaphore.availablePermits() == 0) {
                System.out.println("ATC: All gates occupied. Flight waiting in queue.");
                waitingFlights.wait();
            }
        }

        Flight flight = waitingFlights.poll();
        System.out.println("ATC: Flight " + flight.getId() + " removed from queue.");

        long currentTime = System.currentTimeMillis();
        int waitingTime = (int) (currentTime - flight.getArrivalTime());
        Statistics.recordWaitingTime(waitingTime); // Record waiting time here

        System.out.println("ATC: Flight " + flight.getId() + " requesting runway.");
        runwaySemaphore.acquire();
        Thread.sleep(2000);
        System.out.println("ATC: Flight " + flight.getId() + " has runway access.");

        flight.setStatus("On Runway");

        currentCapacity.getAndIncrement();
    }

    public void requestToTakeoff(Flight flight) throws InterruptedException {
        runwaySemaphore.acquire();
        System.out.println("Flight " + flight.getId() + " has runway access.");

        System.out.println("Flight " + flight.getId() + " is undocking.");
        Thread.sleep(2000);

        System.out.println("Flight " + flight.getId() + " is taking off.");
        Thread.sleep(2000);

        System.out.println("Flight " + flight.getId() + " has departed.");
        runwaySemaphore.release();
        gateSemaphore.release();

        currentCapacity.getAndDecrement();

        synchronized (waitingFlights) {
            waitingFlights.notify();
        }
    }

    public synchronized void addFlight(Flight flight) throws InterruptedException {
        if (flight.isUrgent()) {
            System.out.println("\n --------- EMERGENCY --------- \nATC: Flight "
                    + flight.getId() + " REQUESTS EMERGENCY LANDING!!!\n----------------------------");
            waitingFlights.offerFirst(flight);
        } else {
            waitingFlights.offer(flight);
        }

        flight.setStatus("Waiting");

        flight.setArrivalTime(System.currentTimeMillis());
        if (gateSemaphore.availablePermits() > 0 && runwaySemaphore.availablePermits() > 0) {
            // If there's no waiting, set waiting time to 0
            flight.setWaitingTime(0);
        }
    }

    public void deployFuelService(Flight flight) {
        fuelService.refuelAircraft(flight);
    }

    public Gate assignGate() {
        for (Gate gate : gates) {
            if (!gate.isOccupied()) {
                gate.setOccupied(true);
                return gate;
            }
        }
        return null; // This should not happen since we check for availability before calling
    }

    public void releaseGate(Gate gate) {
        gate.setOccupied(false);
    }
}
