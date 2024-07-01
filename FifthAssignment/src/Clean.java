public class Clean extends Thread {
    Flight flight;

    public Clean(Flight flight) {
        this.flight = flight;
    }
    @Override
    synchronized public void run() {
        try {
            System.out.println("Cleaning in progress for Flight "
                    + flight.getId() + ". (5 seconds)");
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            System.out.println("Cleaning error for Flight " + flight.getId());
        }
    }
}
