import java.util.concurrent.ThreadLocalRandom;

public class Runway {
    private boolean available = true;

    public boolean isAvailable() {
        return available;
    }

    public void land(Airplane airplane) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + ": " + airplane.getAirplaneName() + ": Landing.");
        Thread.sleep(getRandomTime(1000, 3000)); // Simulate landing time
        System.out.println(Thread.currentThread().getName() + ": " + airplane.getAirplaneName() + ": Landed.");
        available = false;
    }

    public void takeOff(Airplane airplane) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + ": " + airplane.getAirplaneName() + ": Taking off.");
        Thread.sleep(getRandomTime(1000, 3000)); // Simulate take-off time
        System.out.println(Thread.currentThread().getName() + ": " + airplane.getAirplaneName() + ": Took off.");
        available = true;
    }

    private static int getRandomTime(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}