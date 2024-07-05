public class Plane implements Runnable {
    private int ID;
    private boolean isEmergency;
    private boolean isRefueled;

    private boolean landingClearance = false;
    private boolean gateOperationsClearance = false;
    private boolean takeoffClearance = false;

    private final AirTrafficControl atc;
    private Passenger[] passengers;
    private Gates gate;

    public Plane(int ID, AirTrafficControl atc, boolean isEmergency) {
        this.ID = ID;
        this.atc = atc;
        this.isEmergency = isEmergency;
        this.passengers = new Passenger[Simulation.MAX_PASSENGERS];
        this.gate = null;
    }

    @Override
    public void run() {
        try {
            System.out.println("Flight " + ID + " entering airspace");

            // Request landing
            if (isEmergency) {
                System.out.println("Emergency Flight " + ID + " requesting emergency landing");
                atc.addRequest(new Request(Request.RequestType.EMERGENCY_LANDING, this));
            } else {
                System.out.println("Flight " + ID + " requesting permission to land");
                atc.addRequest(new Request(Request.RequestType.LANDING, this));
            }

            // Wait for landing clearance
            synchronized (this) {
                while (!landingClearance) {
                    wait();
                }
            }
            land();
            atc.runwaySemaphore.release();

            // Request a gate
            System.out.println("Flight " + ID + " requesting a gate");
            atc.addRequest(new Request(Request.RequestType.REQUEST_GATE, this));
            // Wait for a gate to be assigned
            synchronized (this) {
                while (gate == null) {
                    wait();
                }
            }
            System.out.println("Flight " + ID + " docked at gate " + gate.getID());

            // Wait for gate operations to complete
            gate.startOperations();
            synchronized (this) {
                while (!gateOperationsClearance) {
                    wait();
                }
            }
            System.out.println("Flight " + ID + " finished ground operations at gate " + gate.getID());

            // Request release from gate
            System.out.println("Flight " + ID + " requesting release from gate " + gate.getID());
            atc.addRequest(new Request(Request.RequestType.RELEASE_GATE, this));

            // Wait for gate release clearance
            synchronized (this) {
                while (gate != null) {
                    wait();
                }
            }

            // Request takeoff, wait for clearance
            System.out.println("Flight " + ID + " requesting takeoff");
            atc.addRequest(new Request(Request.RequestType.TAKEOFF, this));
            synchronized (this) {
                while (!takeoffClearance) {
                    wait();
                }
            }
            takeoff();
            atc.runwaySemaphore.release();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    public void land() throws InterruptedException {
        System.out.println("Flight " + ID + ": Affirmative, landing on the runway");
        Thread.sleep(4000); // Simulate landing time
        System.out.println("Flight " + ID + ": Landed on the runway");
    }

    public void takeoff() throws InterruptedException {
        System.out.println("Flight " + ID + ": Affirmative, heading to runway to takeoff");
        Thread.sleep(4000); // Simulate time to takeoff
        System.out.println("Flight " + ID + ": Airborne, now climbing to altitude");
    }

    public void setLandingClearance(boolean state) {
        synchronized (this) {
            this.landingClearance = state;
            notifyAll();
        }
    }

    public void setGateOperationsCompleted(boolean state) {
        synchronized (this) {
            this.gateOperationsClearance = state;
            notifyAll();
        }
    }

    public void setTakeoffClearance(boolean state) {
        synchronized (this) {
            this.takeoffClearance = state;
            notifyAll();
        }
    }

    public Gates getGate() {
        return gate;
    }

    public synchronized void setGate(Gates gate) {
        this.gate = gate;
        notifyAll();
    }

    public Passenger[] getPassengers() {
        return passengers;
    }

    public void setPassengers(Passenger[] passengers) {
        this.passengers = passengers;
    }

    public int getID() {
        return ID;
    }

    public void setEmergency(boolean state) {
        this.isEmergency = state;
    }

    public boolean isEmergency() {
        return isEmergency;
    }

    public void setRefueled(boolean state) {
        this.isRefueled = state;
    }

    public boolean isRefueled() {
        return isRefueled;
    }
}
