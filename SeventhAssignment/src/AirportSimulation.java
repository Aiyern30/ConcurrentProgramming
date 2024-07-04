
public class AirportSimulation {

    public static void main(String[] args) {

        // Initialize Air Traffic Control
        AirTrafficControl atc = new AirTrafficControl();
        atc.setName("ATC-Thread");
        atc.start();

        // Start Plane Generator
        PlaneGenerator pg = new PlaneGenerator(atc);
        pg.setName("PlaneGenerator-Thread");
        pg.start();
    }
}
