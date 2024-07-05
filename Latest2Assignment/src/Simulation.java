
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class Simulation {
    public static int MAX_PASSENGERS = 20;
    public static int NUM_PLANES = 6;

    public static void main(String[] args) {

        List<Gates> gates = new ArrayList<>();
        gates.add(new Gates(1));
        gates.add(new Gates(2));
        gates.add(new Gates(3));

        AirTrafficControl atc = new AirTrafficControl(gates);
        Thread atc_thread = new Thread(atc);
        atc_thread.start();

        // generate planes
        Plane[] planes = new Plane[NUM_PLANES];
        Thread[] plane_threads = new Thread[NUM_PLANES];

        for (int i = 0; i < planes.length; i++) {
            planes[i] = new Plane(i + 1, atc, false);
            plane_threads[i] = new Thread(planes[i]);
        }

        // start
        Random rdm = new Random();
        for (Thread thread : plane_threads) {
            try {
                int delay = rdm.nextInt(2000) + 1000; // 1-3 second delay
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }

            thread.start();
        }

        for (Thread thread : plane_threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Plane plane = new Plane(1, false);
        // Gate gate = new Gate(1);
        // plane.setGate(gate);
        // Thread test_plane = new Thread(plane);
        // test_plane.run();
    }

    public static Passenger[] generatePassengers(Plane plane, boolean is_boarded) {
        Random rdm = new Random();

        int count = rdm.nextInt(MAX_PASSENGERS - 4) + 5;
        Passenger[] passengers = new Passenger[count];

        for (int i = 0; i < count; i++) {
            passengers[i] = new Passenger(i + 1, is_boarded, null);
        }

        return passengers;
    }
}