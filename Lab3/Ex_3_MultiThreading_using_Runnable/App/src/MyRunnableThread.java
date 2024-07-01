public class MyRunnableThread implements Runnable {
    @Override
    public void run(){
        System.out.println("Thread is running: " + Thread.currentThread().getName());

        for (int i = 1; i <=5; i++){
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e){
                System.out.println("Thread interrupted: " + Thread.currentThread().getName());
            }

            System.out.println("Thread progress: " + i);
        }

        System.out.println("Thread finished: " + Thread.currentThread().getName());
        }
}
