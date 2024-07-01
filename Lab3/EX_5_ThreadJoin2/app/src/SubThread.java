public class SubThread implements Runnable{
    @Override
    public void run(){
        System.out.println("\tSubThread is Executed: " + Thread.currentThread().getName());
    }
}
