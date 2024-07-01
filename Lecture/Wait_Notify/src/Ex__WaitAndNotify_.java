import java.util.logging.Logger;

public class Ex__WaitAndNotify_ {

    static Integer X = new Integer(0);

    public static void main(String[] args) throws Exception {

        Notifier_Process R1 = new Notifier_Process();
        Runnable_Process R2= new Runnable_Process();

        Thread T1 = new Thread(R1);
        Thread T2 = new Thread(R2);

        T1.start();
        T2.start();

        try{
            T1.join();
            T2.join();
        } catch (InterruptedException ex){
            Logger.getLogger(null);
        }

        System.out.println(Thread.currentThread().getName() + ": Program Terminates !");

    }
}
