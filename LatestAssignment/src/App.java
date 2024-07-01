public class App {
    public static void main(String[] args) {
        Airport airport = new Airport();
        Airplane airplane1 = new Airplane("Airplane 1", airport);
        Airplane airplane2 = new Airplane("Airplane 2", airport);
        Airplane airplane3 = new Airplane("Airplane 3", airport);
        Airplane airplane4 = new Airplane("Airplane 4", airport);

        airplane1.start();
        airplane2.start();
        airplane3.start();
        airplane4.start();
    }
}
