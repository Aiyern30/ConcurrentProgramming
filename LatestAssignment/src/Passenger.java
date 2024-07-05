public class Passenger implements Runnable {

    private final int ID;
    private boolean isBoarded;
    private Plane plane;

    public Passenger(int ID, boolean isBoarded, Plane plane) {
        this.ID = ID;
        this.isBoarded = isBoarded;
        this.plane = plane;
    }

    @Override
    public void run() {
        try {
            if (isBoarded) {
                disembark();
            } else {
                board();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    private void disembark() throws InterruptedException {
        System.out.println("Passenger " + ID + " disembarking flight " + plane.getID());
        Thread.sleep(150);
    }

    private void board() throws InterruptedException {
        System.out.println("Passenger " + ID + " boarding flight " + plane.getID());
        Thread.sleep(150);
    }

    public boolean isBoarded() {
        return isBoarded;
    }

    public void setBoarded(boolean boarded) {
        isBoarded = boarded;
    }

    public Plane getPlane() {
        return plane;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    public int getID() {
        return ID;
    }
}
