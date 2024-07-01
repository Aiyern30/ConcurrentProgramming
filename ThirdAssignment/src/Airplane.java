import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Airplane extends Thread {
    private static int planeCount = 0;

    private final String airplaneName;
    private final Airport airport;
    private final boolean emergency;
    private boolean atGate;
    private int gateNumber;
    private final List<Passenger> passengers;

    public Airplane(String airplaneName, Airport airport, boolean emergency) {
        this.airplaneName = airplaneName;
        this.airport = airport;
        this.emergency = emergency;
        this.atGate = false;
        this.passengers = new ArrayList<>();
        planeCount++;

        int passengerCount = ThreadLocalRandom.current().nextInt(5, 10); // Random passenger count between 20 and 50
        for (int i = 0; i < passengerCount; i++) {
            passengers.add(new Passenger("Passenger " + (i + 1), this, false));
        }
    }

    @Override
    public void run() {
        try {
            airport.requestLanding(this);
        } catch (InterruptedException e) {
            System.out.println(airplaneName + " was interrupted during landing.");
            Thread.currentThread().interrupt();
        }
    }

    public static int getPlaneCount() {
        return planeCount;
    }

    public String getAirplaneName() {
        return airplaneName;
    }

    public boolean isEmergency() {
        return emergency;
    }

    public boolean isAtGate() {
        return atGate;
    }

    public void setAtGate(boolean atGate) {
        this.atGate = atGate;
    }

    public int getGateNumber() {
        return gateNumber;
    }

    public void setGateNumber(int gateNumber) {
        this.gateNumber = gateNumber;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }
}
