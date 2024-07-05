public class Passenger implements Runnable {

    private final int ID;
    private boolean isPassengerBoarded;
    private Plane plane;

    public Passenger(int ID, boolean isPassengerBoarded, Plane plane) {
        this.ID = ID;
        this.isPassengerBoarded = isPassengerBoarded;
        this.plane = plane;
    }

    @Override
    public void run() {
        try {
            if (isPassengerBoarded) {
                disembark();
            } else {
                board();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    private synchronized void disembark() throws InterruptedException {
        System.out.println("Passenger " + ID + " disembarking flight " + plane.getID());
        Thread.sleep(150);
    }

    private synchronized void board() throws InterruptedException {
        System.out.println("Passenger " + ID + " boarding flight " + plane.getID());
        Thread.sleep(150);
    }

    public int getID() {
        return ID;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }
}
