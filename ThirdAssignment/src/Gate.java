public class Gate {
    private final int gateNumber;
    private boolean occupied;

    public Gate(int gateNumber) {
        this.gateNumber = gateNumber;
        this.occupied = false;
    }

    public int getGateNumber() {
        return gateNumber;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}
