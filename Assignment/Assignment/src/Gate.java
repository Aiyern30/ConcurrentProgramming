import java.util.concurrent.ThreadLocalRandom;

public class Gate {
    private final int gateNumber;
    private boolean available = true;

    public Gate(int gateNumber) {
        this.gateNumber = gateNumber;
    }

    public int getGateNumber() {
        return gateNumber;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void disembarkPassengers() {
        System.out.println(Thread.currentThread().getName() + ": Gate " + gateNumber + ": Disembarking passengers.");
        try {
            Thread.sleep(getRandomTime(200, 500));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + ": Gate " + gateNumber
                + ": Disembarking passengers completed.");
    }

    public void embarkPassengers() {
        System.out.println(Thread.currentThread().getName() + ": Gate " + gateNumber + ": Embarking passengers.");
        try {
            Thread.sleep(getRandomTime(200, 500));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(
                Thread.currentThread().getName() + ": Gate " + gateNumber + ": Embarking passengers completed.");
    }

    public void refillSupplies() {
        System.out.println(Thread.currentThread().getName() + ": Gate " + gateNumber + ": Refilling supplies.");
        try {
            Thread.sleep(getRandomTime(100, 300));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out
                .println(Thread.currentThread().getName() + ": Gate " + gateNumber + ": Refilling supplies completed.");
    }

    public void cleanAircraft() {
        System.out.println(Thread.currentThread().getName() + ": Gate " + gateNumber + ": Cleaning aircraft.");
        try {
            Thread.sleep(getRandomTime(100, 300));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out
                .println(Thread.currentThread().getName() + ": Gate " + gateNumber + ": Cleaning aircraft completed.");
    }

    private static int getRandomTime(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}