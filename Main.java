import java.util.concurrent.Semaphore;
import  java.util.Random;

public class Main {
    public static void main(String[] args){
        Semaphore semaphore = new Semaphore(10);     // Семафор який обмежує кількість потоків, наразі 10
        Customer[] customers = new Customer[15];            // 15 клієнтів, для демонстрації роботи семофора. Якщо всі забронгюють вчасно, 5 людей не отримають квитки
        for(int i=0;i<15;i++){                              // Ініціалізація кожного клієнта,
            customers[i] = new Customer(semaphore);         // Для того щоб семафор коректно працював, потрібна щоб всі потоки використувавали один і той самий семафор
        }
        for(int i=0;i<15;i++){
            if (customers[i].time<6){System.out.println(customers[i].getName() + " не встиг, час - "+customers[i].time);}   // Умова практичної, неможна брати квитки з 0 - 6 годин
            else {
                customers[i].start();                       // Якщо клієнт пройшов умову, він може бронювати квитки
            }
        }
    }
}
class Customer extends Thread{
    private final Semaphore semaphore;                      // Семафор клієнта
    int time;                                               // Час коли клієнт бронює квиток
    public Customer(Semaphore s){
        semaphore = s;
        time = new Random().nextInt(24);              // Час генерується випадково
    }
    @Override
    public void run() {                                     // Функція run() запускається після запускання потока за допомогою start()
        if(semaphore.tryAcquire()){                         // !ВАЖЛИВО! ця функція робить так, щоб вільні потоки не стояли в черзі а виконували код в else
            try{
                Thread.sleep(100);                    // Чомусь без цього програма не працює коректно -_-
                System.out.println(currentThread().getName() + " забронював квиток");   // Якщо Клієнт встиг в першу десятку, йому буде квиток
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                semaphore.release();                        // Семафор відпускає потік
            }
        } else {
            System.out.println("для "+currentThread().getName() + " не лишилось мість");    // Якщо не втиг то квитка не буде
        }
    }
}