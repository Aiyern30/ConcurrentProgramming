

public class App {
    public static void main(String[] args) throws Exception {
        myAccount myAcc = new myAccount();
        Lazada lazada = new Lazada();
        Seller seller = new Seller();

        System.out.println("Initial balances:");
        System.out.println("myAcc: " + myAcc.getBalance());
        System.out.println("Lazada: " + lazada.getBalance());
        System.out.println("Seller: " + seller.getBalance());
        System.out.println("\n");


        Thread transferToLazada = new Thread(() -> {
            synchronized(myAcc) {
                synchronized(lazada) {
                    if (myAcc.getBalance() >= 100) {
                        myAcc.withdraw(100);
                        lazada.deposit(100);
                    }
                }
            }
        });

        Thread transferToSeller = new Thread(() -> {
            synchronized(lazada) {
                synchronized(seller) {
                    if (lazada.getBalance() >= 100) {
                        lazada.withdraw(100);
                        seller.deposit(100);
                    }
                }
            }
        });

        transferToLazada.start();
        transferToLazada.join();

        System.out.println("myAcc: " + myAcc.getBalance());
        System.out.println("Lazada: " + lazada.getBalance());
        System.out.println("Seller: " + seller.getBalance());
        System.out.println("\n");

        transferToSeller.start();
        transferToSeller.join();

        System.out.println("Final balances:");
        System.out.println("myAcc: " + myAcc.getBalance());
        System.out.println("Lazada: " + lazada.getBalance());
        System.out.println("Seller: " + seller.getBalance());
    }
}
