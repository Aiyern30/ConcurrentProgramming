import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FuelService implements Runnable {
    private BlockingQueue<RefuelTask> refuelQueue = new LinkedBlockingQueue<>();
    private AirTrafficControl atc; // Reference to AirTrafficControl

    public FuelService(AirTrafficControl atc) {
        this.atc = atc; // Initialize AirTrafficControl reference
    }

    @Override
    public void run() {
        try {
            while (true) {
                RefuelTask task = refuelQueue.take(); // Take the next refuel task
                System.out.println("FuelService: Moving to Gate " + task.getGate().getId() + " for Flight "
                        + task.getFlight().getId());
                Thread.sleep(2000); // Simulate travel time to gate
                System.out.println("FuelService: Refueling in progress for Flight " + task.getFlight().getId()
                        + " at Gate " + task.getGate().getId());
                Thread.sleep(2000); // Simulate refueling time
                System.out.println("FuelService: Refueling complete for Flight " + task.getFlight().getId()
                        + " at Gate " + task.getGate().getId());

                // Notify ATC or main simulation thread that refueling is complete
                Gate gate = task.getGate();
                atc.releaseGate(gate); // Use the 'atc' reference to call methods
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void refuelAircraft(Flight flight, Gate gate) {
        refuelQueue.offer(new RefuelTask(flight, gate));
    }

    private static class RefuelTask {
        private final Flight flight;
        private final Gate gate;

        public RefuelTask(Flight flight, Gate gate) {
            this.flight = flight;
            this.gate = gate;
        }

        public Flight getFlight() {
            return flight;
        }

        public Gate getGate() {
            return gate;
        }
    }
}
