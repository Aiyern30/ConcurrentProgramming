public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("The Main Thread Name: " + Thread.currentThread().getName());

        Runnable_Process P1 = new Runnable_Process();

        Thread T1 = new Thread(P1);
        Thread T2 = new Thread(P1);
        Thread T3 = new Thread(P1);
        Thread T4 = new Thread(P1);

        T1.start();

        try{
            T1.join();
        } catch (InterruptedException ex){

        }

        T2.start();

        try{
            T2.join();

        } catch (InterruptedException ex){

        }

        T3.start();
        T4.start();

        try{
            T4.join();
        } catch (InterruptedException ex){
            ex.printStackTrace();
        }

        System.out.println("Main Thread is Terminated");
    }
}
