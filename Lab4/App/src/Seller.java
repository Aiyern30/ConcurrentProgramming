public class Seller {
    private int sellerAcc;

    Seller() {
        this.sellerAcc = 0;
    }

    public synchronized void deposit(int amount) {
        sellerAcc += amount;
    }

    public int getBalance() {
        return sellerAcc;
    }
}
