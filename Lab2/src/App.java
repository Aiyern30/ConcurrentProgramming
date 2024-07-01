class ThreadExtend extends Thread{
    private Thread t;
    private String threadName;
 
    ThreadExtend(String name){
        threadName = name;
        System.out.println("Creating " + threadName);
    }

    public void run(){
        System.out.println("Running " + threadName);
        try{
            for (int i = 4; i > 0; i--){
                System.out.println("Thread: " + threadName + ", Loop: " + i);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            System.out.println("Thread " + threadName + " interrupted");
        }

        System.out.println("Thread " + threadName + " exiting");
    }

    public void start(){
        System.out.println("Starting " + threadName);
        if(t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }

}

public class App {
    
    public static void main(String[] args) throws Exception {

        ThreadExtend T1 = new ThreadExtend("Thread-1");
        T1.start();
        ThreadExtend T2 = new ThreadExtend("Thread-2");
        T2.start(); 

    }
}
