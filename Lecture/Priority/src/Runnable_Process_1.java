public class Runnable_Process_1 implements Runnable{
    @Override
    public void run(){
        System.out.println(Thread.currentThread().getName() + "Priority: " + Thread.currentThread().getPriority() + " Executed.");
    }
}
