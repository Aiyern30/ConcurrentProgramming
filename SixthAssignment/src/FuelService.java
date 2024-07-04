
import java.util.concurrent.Semaphore;

public class FuelService {

    Semaphore FuelService = new Semaphore(1);

    public void refuelAircraft(Flight flight) {
        try {
            FuelService.acquire();
            System.out
                    .println(Thread.currentThread().getName() + ": Refueling in progress for Flight " + flight.getId());
            Thread.sleep(5000);
            System.out.println("Refueling complete for Flight " + flight.getId());
        } catch (InterruptedException e) {
            System.out.println("Refueling error for Flight " + flight.getId());
        } finally {
            FuelService.release();
        }
    }
}
