public class Passenger extends Thread {
    int id;
    Flight flight;

    public Passenger(int id, Flight flight) {
        this.id = id;
        this.flight = flight;
    }

    public void run() {
        if (flight.getStatus().equals("Docked")) {
            disembark();
        } else {
            embark();
        }
    }

    synchronized public void embark() {
        System.out.println("Boarding " + getOrdinal(id) + " passenger for Flight " + flight.getId() + ".");
        Statistics.passengersEmbarked.getAndIncrement();
        Statistics.passengerCount(1);
    }

    synchronized public void disembark() {
        System.out.println("Disembarking " + getOrdinal(id) + " passenger from Flight " + flight.getId() + ".");
        Statistics.passengersDisembarked.getAndIncrement();
        Statistics.passengerCount(-1);
    }

    private String getOrdinal(int number) {
        if (number % 100 >= 11 && number % 100 <= 13) {
            return number + "th";
        }
        switch (number % 10) {
            case 1:
                return number + "st";
            case 2:
                return number + "nd";
            case 3:
                return number + "rd";
            default:
                return number + "th";
        }
    }
}
