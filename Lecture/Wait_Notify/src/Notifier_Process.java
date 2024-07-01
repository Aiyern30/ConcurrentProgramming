public class Notifier_Process implements Runnable{
    
    @Override
    public synchronized void run(){
        System.out.println(Thread.currentThread().getName() + ": Notifier been Initiated");
    
        synchronized(X){
            System.out.println(Thread.currentThread().getName() +": In Progress...");
            X.notify(); // Notify the ONE waiting Thread that this Thread is done with the Locked Object.
            System.out.println(Thread.currentThread().getName() +": Notification been fired.");
        
        }
    }

}
