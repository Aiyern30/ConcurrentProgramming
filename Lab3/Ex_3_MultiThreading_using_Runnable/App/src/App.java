public class App {
    public static void main(String[] args) throws Exception {
        MyRunnableThread myRunnable = new MyRunnableThread();

        Thread thread1 = new Thread(myRunnable);
        Thread thread2 = new Thread(myRunnable);

        thread1.start();
        thread2.start();
    }
}
