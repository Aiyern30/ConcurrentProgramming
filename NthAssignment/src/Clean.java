public class Clean extends Thread {
    Flight flight;

    public Clean(Flight flight) {
        this.flight = flight;
    }

    @Override
    synchronized public void run() {
        try {
            System.out.println("Clean Service: Cleaning in progress for Flight "
                    + flight.getId());
            Thread.sleep(5000);
            System.out.println("Clean Service: Cleaning complete for Flight "
                    + flight.getId());
        } catch (InterruptedException ex) {
            System.out.println("Cleaning error for Flight " + flight.getId());
        }
    }
}
