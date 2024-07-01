public class Runnable_Process implements Runnable {
    public void run(){
        // sharecounter++;
        System.out.println(Thread.currentThread().getName() + " : " + Thread.currentThread());
        try{
            Thread.sleep(500);
        } catch (InterruptedException ex){
            
        }
    }
}
