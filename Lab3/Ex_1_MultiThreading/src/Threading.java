public class Threading extends Thread {
    // Default constructor
    public Threading() {
        // Call the superclass constructor
        super();
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Handle the InterruptedException
            }
        }
    }
}
