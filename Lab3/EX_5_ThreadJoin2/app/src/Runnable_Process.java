public class Runnable_Process implements Runnable {
    public void run(){
        System.out.println(Thread.currentThread().getName() + " : Started ...");

        SubThread p2_1 = new SubThread();
        SubThread p2_2 = new SubThread();

        Thread T4 = new Thread(p2_1);
        Thread T5 = new Thread(p2_2);

        T4.start();
        T5.start();

        try{
            T5.join();
        } catch (InterruptedException e){
            System.out.println(Thread.currentThread().getName() + ": " + "Completed!");
        }
    }   
}
