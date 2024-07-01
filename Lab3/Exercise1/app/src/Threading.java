public class Threading extends Thread{
    private int studentNumber;

    public Threading(int studentNumber){
        this.studentNumber = studentNumber;
    }

    private void greeting() {
        System.out.println("Student " + studentNumber + " greets the teacher.");
        try {
            Thread.sleep(1000); // Simulating time taken for greeting
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void removeShoes() {
        System.out.println("Student " + studentNumber + " removes shoes.");
        try {
            Thread.sleep(1000); // Simulating time taken for removing shoes
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void takeSeat() {
        System.out.println("Student " + studentNumber + " takes a seat in the classroom.");
        try {
            Thread.sleep(1000); // Simulating time taken for taking a seat
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        greeting();
        removeShoes();
        takeSeat();

    }

    


}
