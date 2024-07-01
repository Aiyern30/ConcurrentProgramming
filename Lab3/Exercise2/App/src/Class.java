public class Class extends Thread {
    
    private static final Object lock = new Object(); // Shared lock for synchronization
    private static int studentNumber = 1; // Shared variable to track student number
    private int classNumber;

    public Class(int classNumber) {
        this.classNumber = classNumber;
    }

    private void arrive(int studentNumber) {
        System.out.println("Student " + studentNumber + " arrives at classroom " + classNumber);
        try {
            Thread.sleep(1000); // Simulating time taken for arriving
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void greeting(int studentNumber) {
        System.out.println("Student " + studentNumber + " greets the teacher in classroom " + classNumber);
        try {
            Thread.sleep(1000); // Simulating time taken for greeting
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void removeShoes(int studentNumber) {
        System.out.println("Student " + studentNumber + " removes shoes in classroom " + classNumber);
        try {
            Thread.sleep(1000); // Simulating time taken for removing shoes
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void takeSeat(int studentNumber) {
        System.out.println("Student " + studentNumber + " takes a seat in classroom " + classNumber);
        try {
            Thread.sleep(1000); // Simulating time taken for taking a seat
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run(){
        synchronized (lock) {
            int currentStudentNumber = studentNumber++;
            arrive(currentStudentNumber);
            greeting(currentStudentNumber);
            removeShoes(currentStudentNumber);
            takeSeat(currentStudentNumber);
        }
        
    }

}
