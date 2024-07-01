import java.util.concurrent.atomic.AtomicInteger;

public class Statistics {
    public static AtomicInteger totalWaitingTime = new AtomicInteger(0);
    public static AtomicInteger passengersEmbarked = new AtomicInteger(0);
    public static AtomicInteger passengersDisembarked = new AtomicInteger(0);
    public static AtomicInteger planesServed = new AtomicInteger(0);
    public static AtomicInteger totalPassengers = new AtomicInteger(0);

    public static void passengerCount(int passengers) {
        totalPassengers.getAndAdd(passengers);
    }

    public static void printStatistics() {
        int totalFlights = planesServed.get();
        int maxWaitingTime = totalWaitingTime.get();
        int minWaitingTime = totalWaitingTime.get();
        int avgWaitingTime = totalWaitingTime.get() / totalFlights;
    
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
