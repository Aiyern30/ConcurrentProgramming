import java.util.logging.Logger;

public class Runnable_Process implements Runnable{

    static Integer X = new Integer(0);

    @Override
    public void run(){
        System.out.println(Thread.currentThread().getName() + ": Waiting !");

        synchronized(X){
            try{
                X.wait();
                System.out.println(Thread.currentThread().getName() + ": d");
            } catch (InterruptedException ex) {
                Logger.getLogger(Thread.currentThread().getName()).log(null, null, ex);
            }
        }
    }
}
