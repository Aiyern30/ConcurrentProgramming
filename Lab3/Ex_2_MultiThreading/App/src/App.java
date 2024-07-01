public class App {
    public static void main(String[] args) throws Exception {

        Threading thread_1 = new Threading(1);
        Threading thread_2 = new Threading(2);

        for( int i = 3; i < 6; i++){
            Threading thread = new Threading(i);
            thread.start();
        }

        thread_1.start();
        thread_2.start();
    }
}
