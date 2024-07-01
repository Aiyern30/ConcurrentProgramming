public class App {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 200; i++) {
            Carpark carpark1 = new Carpark("Gate 1");
            Carpark carpark2 = new Carpark("Gate 2");
            Carpark carpark3 = new Carpark("Gate 3");

            Thread c1 = new Thread(carpark1);
            Thread c2 = new Thread(carpark2);
            Thread c3 = new Thread(carpark3);
            c1.start();
            c2.start();
            c3.start();

            try {
                c1.join();
                c2.join();
                c3.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("Simulation finished. Total available parking lots: " + Carpark.getAvailableParkingLots());
    }
}