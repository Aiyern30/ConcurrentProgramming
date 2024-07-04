import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class AirTrafficControl extends Thread {

    Semaphore runwaySemaphore = new Semaphore(1);
    Semaphore gateSemaphore = new Semaphore(3);

    PriorityBlockingQueue<Flight> waitingFlights;
    FuelService fuelService = new FuelService();

    AtomicInteger currentCapacity = new AtomicInteger(0);

    List<Gate> gates;

    public AirTrafficControl() {
        waitingFlights = new PriorityBlockingQueue<>();
        gates = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Gate gate = new Gate(i);
            gates.add(gate);
        }
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ": Air Traffic Control is online and operational.");
    }

    public void requestToLand() throws InterruptedException {
        Flight flight;
        synchronized (waitingFlights) {
            while (gateSemaphore.availablePermits() == 0) {
                System.out.println("ATC: All gates occupied. Flight waiting in queue.");
                waitingFlights.wait();
            }
            flight = waitingFlights.poll();
            System.out.println("ATC: Flight " + flight.getId() + " removed from queue.");
        }

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

    public void addFlight(Flight flight) {
        synchronized (waitingFlights) {
            waitingFlights.offer(flight);
            waitingFlights.notify();
        }

        flight.setStatus("Waiting");
        flight.setArrivalTime(System.currentTimeMillis());
        if (gateSemaphore.availablePermits() > 0 && runwaySemaphore.availablePermits() > 0) {
            flight.setWaitingTime(0);
        }
    }

    public void deployFuelService(Flight flight) {
        fuelService.refuelAircraft(flight);
    }

    public synchronized Gate assignGate() {
        for (Gate gate : gates) {
            if (!gate.isOccupied()) {
                gate.setOccupied(true);
                return gate;
            }
        }
        return null; // No available gate
    }

    public synchronized void releaseGate(Gate gate) {
        gate.setOccupied(false);
        synchronized (gate) {
            gate.notify(); // Notify the gate thread that it's available
        }
    }
}
