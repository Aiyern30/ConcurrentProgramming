public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("The Main Thread Name: " + Thread.currentThread().getName());

        Runnable_Process P1 = new Runnable_Process();

        Thread T1 = new Thread(P1);
        Thread T2 = new Thread(P1);
        Thread T3 = new Thread(P1);

        T1.start();

        try{
            T1.join();
        } catch (InterruptedException e){

        }

        T2.start();

        try{
            T2.join();
        } catch (InterruptedException e){

        }

        T3.start();

        try{
            T3.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        System.out.println("Main Thread is Terminated");
    }
}
