public class Carpark implements Runnable {
    private String Gate;
    private static int allowCarPark = 200;
    private static int availableParkingLots = 2000;

    public Carpark(String Gate) {
        this.Gate = Gate;
    }

    public void openGate(String Gate) {
        System.out.println(Gate + " is open!");
    }

    public static int getAvailableParkingLots() {
        return availableParkingLots;
    }

    public static synchronized void decAndPrint(String gate) {
        if (availableParkingLots > 0 && allowCarPark > 0) {
            System.out.println(gate + " is open!");
            availableParkingLots--;
            allowCarPark--;
            System.out.println(availableParkingLots);
            if (allowCarPark == 0) {
                System.out.println("We only allow for 200 cars today");
            }
        }
    }

    @Override
    public void run() {
        decAndPrint(Gate);
    }
}