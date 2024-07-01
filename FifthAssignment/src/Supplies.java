public class Supplies extends Thread {

    Flight flight;

    public Supplies(Flight flight) {
        this.flight = flight;
    }

    synchronized public void run() {
        try {
            System.out.println("\n-------------- Supplies -----------------\nFlight "
                    + flight.getId() + ": Refilling food and drinks for Flight " + flight.getId()
                    + ". (5 seconds)\n--------------------------------------");

            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            System.out.println("Flight " + flight.getId() + ": Supply Error");
        }
    }
}
