public class Gate extends Thread {
    private final int id;
    private volatile boolean occupied;

    public Gate(int id) {
        this.id = id;
        this.occupied = false;
    }

    public int getGateId() {
        return id;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                try {
                    wait(); // Wait until notified
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
