import java.util.concurrent.ThreadLocalRandom;

public class Passenger extends Thread {
    private final String passengerName;
    private final Airplane airplane;
    private final boolean embarking;

    public Passenger(String passengerName, Airplane airplane, boolean embarking) {
        this.passengerName = passengerName;
        this.airplane = airplane;
        this.embarking = embarking;
    }

    @Override
    public void run() {
        try {
            if (embarking) {
                System.out.println(passengerName + " is embarking " + airplane.getAirplaneName());
                Thread.sleep(ThreadLocalRandom.current().nextInt(100, 500)); // Simulate embarking time
                System.out.println(passengerName + " has embarked " + airplane.getAirplaneName());
            } else {
                System.out.println(passengerName + " is disembarking " + airplane.getAirplaneName());
                Thread.sleep(ThreadLocalRandom.current().nextInt(100, 500)); // Simulate disembarking time
                System.out.println(passengerName + " has disembarked " + airplane.getAirplaneName());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public String getPassengerName() {
        return passengerName;
    }
}