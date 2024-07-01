public class App {
    public static void main(String[] args) throws Exception {

        for (int i = 1; i <= 2; i++) {
            Class class1 = new Class(1);
            Class class2 = new Class(2);
            class1.start();
            class2.start();
            class1.join();
            class2.join();
        }
    

    }
}
