import java.util.Random;

public class Flight implements Runnable {

    private int id;
    private boolean urgent;
    private String status;
    private long arrivalTime;
    private AirTrafficControl atc;
    private int waitingTime;

    private int passengers;
    private Gate gate;

    public Flight(int id, boolean urgent, AirTrafficControl atc, int passengers) {
        this.id = id;
        this.urgent = urgent;
        this.atc = atc;
        this.passengers = passengers;
        this.gate = null; // Initialize to null (indicating not docked yet)
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public Gate getGate() {
        return gate;
    }

    public void setGate(Gate gate) {
        this.gate = gate;
    }

    public AirTrafficControl getAtc() {
        return atc;
    }

    public void setAtc(AirTrafficControl atc) {
        this.atc = atc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    @Override
    public void run() {
        try {
            requestLanding();

            while (this.getStatus().equals("On Runway")) {
                dock();
            }

            while (this.getStatus().equals("Docked")) {
                operateAirport();
            }

            while (this.getStatus().equals("Depart")) {
                depart();
            }

            if (atc.currentCapacity.get() == 0 && atc.runwaySemaphore.availablePermits() == 1
                    && atc.gateSemaphore.availablePermits() == 3) {
                System.out.println("\n--------------- Simulation Complete ---------------\n");
                Statistics.printStatistics();
            }

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private void requestLanding() throws InterruptedException {
        System.out.println("Flight " + this.getId() + " requesting landing.");
        Thread.sleep(1000);

        synchronized (atc) {
            atc.addFlight(this);

            if (this.isUrgent()) {
                System.out.println(Thread.currentThread().getName() + ": Emergency landing requested for Flight "
                        + this.getId() + ".");
            } else {
                System.out.println(
                        Thread.currentThread().getName() + ": Flight " + this.getId() + " queued for landing.");
            }

            atc.requestToLand();
            waitingTime = 0; // Assuming waiting time calculation here
            atc.notifyAll(); // Notify ATC that flight is ready for landing
        }
    }

    public void dock() throws InterruptedException {
        Gate gate = atc.assignGate(); // Assign a gate
        if (gate != null) {
            this.setGate(gate);
            System.out.println("Flight " + this.getId() + " acquiring gate " + this.getGate().getId() + ".");
            Thread.sleep(2000);
            System.out.println("Flight " + this.getId() + " has docked at Gate " + this.getGate().getId() + ".");
            Thread.sleep(1000);

            System.out.println("Flight " + this.getId() + " docking procedures complete.");
            Thread.sleep(2000);

            this.setStatus("Docked");

            atc.runwaySemaphore.release();
        } else {
            // Handle no available gate scenario
            System.out.println("Flight " + this.getId() + " is waiting for an available gate.");
        }
    }

    private synchronized void operateAirport() throws InterruptedException {
        // Disembarking passengers
        System.out.println("\n-------------- Disembark passengers ---------------");
        this.setStatus("Docked");
        int passengerCount = this.getPassengers();
        for (int i = 1; i <= passengerCount; i++) {
            Passenger passenger = new Passenger(i, this);
            passenger.start();
            passenger.join();
        }
        System.out.println("-------------------------------------------------------");
        System.out.println("Flight " + this.getId() + ": All passengers disembarked.");

        // Cleaning
        Clean clean = new Clean(this);
        clean.start();
        clean.join();

        // Refilling supplies
        Supplies supplies = new Supplies(this);
        supplies.start();
        supplies.join();

        // Refueling
        System.out.println("Flight " + this.getId() + " requesting refueling.");
        this.getAtc().deployFuelService(this);

        // Boarding new passengers
        System.out.println("\n-------------- Boarding passengers ---------------");
        this.setStatus("Boarding");
        Random random = new Random();
        int randomPassengerCount = random.nextInt((50 - 20) + 1) + 20;
        this.setPassengers(randomPassengerCount); // Update passenger count for boarding
        for (int i = 1; i <= randomPassengerCount; i++) {
            Passenger passenger = new Passenger(i, this);
            passenger.start();
            passenger.join();
        }
        System.out.println("-----------------------------------------------------");
        System.out.println("Flight " + this.getId() + ": All passengers boarded.");

        // Prepare for departure
        this.setStatus("Depart");
    }

    private void depart() throws InterruptedException {
        atc.requestToTakeoff(this);
        Statistics.planesServed.getAndIncrement();
        this.setStatus("Completed");
        atc.releaseGate(this.getGate()); // Release the gate after departure
    }
}
