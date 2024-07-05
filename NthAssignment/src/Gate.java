public class Gate {
    private int id;
    private boolean occupied;
    private AirTrafficControl atc; // Reference to AirTrafficControl

    public Gate(int id) {
        this.id = id;
        this.occupied = false;
        this.atc = null; // Initialize to null initially
    }

    public int getId() {
        return id;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public AirTrafficControl getAtc() {
        return atc;
    }

    public void setAtc(AirTrafficControl atc) {
        this.atc = atc;
    }
}
