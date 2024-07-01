public class Runnable_Process_2 implements Runnable{
    @Override
    public void run(){
        System.out.println(Thread.currentThread().getName() + "Priority: " + Thread.currentThread().getPriority() + " Executed.");
    }
}
