public class App {
    public static void main(String[] args) {
        Airport airport = new Airport();

        // Start 2 airplanes that will occupy the gates
        Airplane airplane1 = new Airplane("Airplane 1", airport, false);
        Airplane airplane2 = new Airplane("Airplane 2", airport, false);
        airplane1.start();
        airplane2.start();

        // Create a 3rd airplane that has an emergency
        Airplane airplane5 = new Airplane("Airplane 5", airport, true);
        airplane5.start();

        // Create and start 2 airplanes that will be waiting to land
        Airplane airplane3 = new Airplane("Airplane 3", airport, false);
        Airplane airplane4 = new Airplane("Airplane 4", airport, false);
        airplane3.start();
        airplane4.start();

        // Wait for all airplanes to complete their journeys
        try {
            airplane1.join();
            airplane2.join();
            airplane3.join();
            airplane4.join();
            airplane5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Shutdown the airport
        airport.shutdown();
        System.out.println("Airport shutdown complete.");
    }
}
