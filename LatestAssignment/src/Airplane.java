public class Airplane extends Thread {
    private final String name;
    private final Airport airport;
    private boolean atGate = false;

    public Airplane(String name, Airport airport) {
        this.name = name;
        this.airport = airport;
    }

    public String getAirplaneName() {
        return name;
    }

    public void setAtGate(boolean atGate) {
        this.atGate = atGate;
    }

    @Override
    public void run() {
        try {
            airport.requestLanding(this);
            Thread.sleep(1000); // Simulate time at the gate
            airport.requestTakeoff(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
