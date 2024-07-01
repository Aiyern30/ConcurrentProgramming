public class App {

    
    public static void main(String[] args) throws Exception {


        for(int i = 0; i < 10; i ++){
            Threading thread = new Threading(i);
            Thread t = new Thread(thread);
            t.start();
            t.join();
        }

    }
}
