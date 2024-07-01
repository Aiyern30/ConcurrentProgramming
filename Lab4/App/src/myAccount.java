public class myAccount {
    private int Bank;

    myAccount(){
        this.Bank = 500;
    }

    public synchronized void withdraw(int amount){
        if(Bank >= amount) {
            Bank -= amount;
        } else {
            System.out.println("Insufficient amount");
        }
    }
    public synchronized void deposit(int amount) {
        Bank += amount;
    }

    public int getBalance(){
        return Bank;
    }
}
