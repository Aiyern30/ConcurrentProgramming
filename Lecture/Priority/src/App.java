
public class App {
    public static void main(String[] args) throws Exception {
        Runnable_Process_1 t1 = new Runnable_Process_1();
        Runnable_Process_2 t2 = new Runnable_Process_2();
        Runnable_Process_3 t3 = new Runnable_Process_3();
        Runnable_Process_4 t4 = new Runnable_Process_4();

        Thread P1 = new Thread(t1);
        Thread P2 = new Thread(t2);
        Thread P3 = new Thread(t3);
        Thread P4 = new Thread(t4);

        P1.setPriority(1);
        P1.setPriority(Thread.MAX_PRIORITY);
        P1.setPriority(Thread.MIN_PRIORITY);
        P1.setPriority(5);

        P1.start();
        P2.start();
        P3.start();
        P4.start();

    }

}
