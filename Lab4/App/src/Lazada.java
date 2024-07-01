public class Lazada {
    private int lazadaAcc;

    Lazada() {
        this.lazadaAcc = 1000; 
    }

    public synchronized void withdraw(int amount) {
        if (lazadaAcc >= amount) {
            lazadaAcc -= amount;
        } else {
            System.out.println("Insufficient amount in Lazada");
        }
    }

    public synchronized void deposit(int amount) {
        lazadaAcc += amount;
    }


    public int getBalance() {
        return lazadaAcc;
    }
}
