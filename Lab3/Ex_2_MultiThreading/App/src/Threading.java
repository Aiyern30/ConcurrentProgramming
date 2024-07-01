import java.util.logging.Logger;


public class Threading extends Thread{
    private int ThreadNumber;

    public Threading(int threadNumber){
        this.ThreadNumber = threadNumber;
    }

    @Override
    public void run(){
        for(int i=0; i < 5; i++){
            System.out.println(i + " from Thread #" + this.ThreadNumber);

            if(this.ThreadNumber == 2){
                throw new RuntimeException();
            }

            try {
                this.sleep(1000);
            } catch (InterruptedException ex) {

            }
        }
    }
}
