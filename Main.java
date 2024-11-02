import java.util.concurrent.Semaphore;
import  java.util.Random;

public class Main {
    public static void main(String[] args){
        Semaphore semaphore = new Semaphore(10);
        Customer[] customers = new Customer[15];
        for(int i=0;i<15;i++){
            customers[i] = new Customer(semaphore);
        }
        for(int i=0;i<15;i++){
            if (customers[i].time<6){System.out.println(customers[i].getName() + " не встиг, час - "+customers[i].time);}
            else {
                customers[i].start();
            }
        }
    }
}
class Customer extends Thread{
    private final Semaphore semaphore;
    int time;
    public Customer(Semaphore s){
        semaphore = s;
        time = new Random().nextInt(24);
    }
    @Override
    public void run() {
        if(semaphore.tryAcquire()){
            try{
                Thread.sleep(100);
                System.out.println(currentThread().getName() + " забронював квиток");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                semaphore.release();
            }
        } else {
            System.out.println("для "+currentThread().getName() + " не лишилось мість");
        }
    }
}