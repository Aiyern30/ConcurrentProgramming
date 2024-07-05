
public class Plane implements Runnable {
    private int ID;
    private boolean is_emergency;
    private boolean is_refueled;

    // flag variables
    private boolean landing_clearance = false;
    private boolean gate_operations_clearance = false;
    private boolean takeoff_clearance = false;

    private final AirTrafficControl atc;
    private Passenger[] passengers;
    private Gates gate;

    public Plane(int ID, AirTrafficControl atc, boolean is_emergency) {
        this.ID = ID;
        this.atc = atc;
        this.is_emergency = is_emergency;
        this.passengers = Simulation.generatePassengers(this, true);
        this.gate = null;
    }

    @Override
    public void run() {
        try {
            System.out.println("Flight " + ID + " entering airspace");

            // request landing
            if (is_emergency) {
                System.out.println("Emergency Flight " + ID + " requesting emergency landing");
                atc.addRequest(new Request(Request.RequestType.EMERGENCY_LANDING, this));
            } else {
                System.out.println("Flight " + ID + " requesting permission to land");
                atc.addRequest(new Request(Request.RequestType.LANDING, this));
            }

            // wait for landing clearance
            synchronized (this) {
                while (!landing_clearance) {
                    wait();
                }
            }
            land();
            atc.runwayLock.release();

            // request a gate
            System.out.println("Flight " + ID + " requesting a gate");
            atc.addRequest(new Request(Request.RequestType.ASSIGN_GATE, this));
            // wait for a gate to be assigned here
            synchronized (this) {
                while (gate == null) {
                    wait();
                }
            }
            System.out.println("Flight " + ID + " docked at gate " + gate.getID());

            // wait for gate operations to complete
            gate.startOperations();
            synchronized (this) {
                while (!gate_operations_clearance) {
                    wait();
                }
            }
            System.out.println("Flight " + ID + " finished ground operations at gate " + gate.getID());

            // request release from gate
            System.out.println("Flight " + ID + " requesting release from gate " + gate.getID());
            atc.addRequest(new Request(Request.RequestType.RELEASE_GATE, this));

            // wait for gate release clearence
            synchronized (this) {
                while (gate != null) {
                    wait();
                }
            }

            // request takeoff, wait for clearance
            System.out.println("Flight " + ID + " requesting takeoff");
            atc.addRequest(new Request(Request.RequestType.TAKEOFF, this));
            synchronized (this) {
                while (!takeoff_clearance) {
                    wait();
                }
            }
            takeoff();
            atc.runwayLock.release();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    public void land() throws InterruptedException {
        System.out.println("Flight " + ID + ": I am landing on the runway");
        Thread.sleep(4000); // simulate landing time
        System.out.println("Flight " + ID + ": I landed on the runway");
    }

    public void takeoff() throws InterruptedException {
        System.out.println("Flight " + ID + ": I am heading to runway to takeoff");
        Thread.sleep(4000); // simulate time to takeoff
        System.out.println("Flight " + ID + ": I have departed");
    }

    public void setLandingClearance(boolean state) {
        synchronized (this) {
            this.landing_clearance = state;
            notifyAll();
        }
    }

    public void setGateOperationsCompleted(boolean state) {
        synchronized (this) {
            this.gate_operations_clearance = state;
            notifyAll();
        }
    }

    public void setTakeoffClearance(boolean state) {
        synchronized (this) {
            this.takeoff_clearance = state;
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
        this.is_emergency = state;
    }

    public boolean isEmergency() {
        return is_emergency;
    }

    public void setRefueled(boolean state) {
        this.is_refueled = state;
    }

    public boolean isRefueled() {
        return is_refueled;
    }
}