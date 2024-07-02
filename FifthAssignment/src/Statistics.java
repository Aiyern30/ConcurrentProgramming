import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;
import java.util.List;

public class Statistics {
    public static AtomicInteger totalWaitingTime = new AtomicInteger(0);
    public static AtomicInteger passengersEmbarked = new AtomicInteger(0);
    public static AtomicInteger passengersDisembarked = new AtomicInteger(0);
    public static AtomicInteger planesServed = new AtomicInteger(0);
    public static AtomicInteger totalPassengers = new AtomicInteger(0);

    private static List<Integer> waitingTimes = new ArrayList<>();

    public static void recordWaitingTime(int waitingTime) {
        waitingTimes.add(waitingTime);
        totalWaitingTime.getAndAdd(waitingTime);
    }

    public static void passengerCount(int passengers) {
        totalPassengers.getAndAdd(passengers);
    }

    public static void printStatistics() {
        int totalFlights = planesServed.get();
        int maxWaitingTime = waitingTimes.stream().max(Integer::compare).orElse(0);
        int minWaitingTime = waitingTimes.stream().min(Integer::compare).orElse(0);
        int avgWaitingTime = totalFlights == 0 ? 0 : totalWaitingTime.get() / totalFlights;

        System.out.println("\n---------- Airport Statistics ----------");
        System.out.println("Total flights served: " + totalFlights);
        System.out.println("Maximum waiting time: " + maxWaitingTime);
        System.out.println("Minimum waiting time: " + minWaitingTime);
        System.out.println("Average waiting time: " + avgWaitingTime);
        System.out.println("Total passengers embarked: " + passengersEmbarked.get());
        System.out.println("Total passengers disembarked: " + passengersDisembarked.get());
        System.out.println("----------------------------------------\n");
    }
}
