import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Justyna Gorczyca on 30.11.2017.
 */
public class Buffer {

    private final Lock lock = new ReentrantLock();
    private final Condition firstProducer = lock.newCondition();
    private final Condition producerRest = lock.newCondition();
    private final Condition firstConsumer = lock.newCondition();
    private final Condition consumerRest = lock.newCondition();

    private boolean firstProducerOccupied;
    private boolean firstConsumerOccupied;

    private int bufferSize;
    private boolean[] buffer;
    private int elementsInBuffer;

    public Buffer (int buffSize) {
        firstConsumerOccupied = false;
        firstProducerOccupied = false;
        this.bufferSize=buffSize;
        buffer = new boolean[buffSize];
        for(int i = 0; i < buffSize; i++)
            buffer[i] = false;
        this.elementsInBuffer = 0;
    }

    public void write(int id, int portion) {
        lock.lock();
        try {
            System.out.println("Producent " + id + " chce wstawić " + portion + " el");

            if(firstProducerOccupied)
                producerRest.await();
            firstProducerOccupied = true;

            while (bufferSize - elementsInBuffer < portion)
                firstProducer.await();

            System.out.println("Producent " + id + " wstawia " + portion + " elementów");
            for(int i = elementsInBuffer; i < elementsInBuffer + portion; i++)
                buffer[i] = true;

            elementsInBuffer += portion;
            System.out.println("    Stan bufora: "+ elementsInBuffer);

            firstProducerOccupied = false;
            producerRest.signal();
            firstConsumer.signal();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


    public void read(int id, int portion) {
        lock.lock();
        try {
            System.out.println("Konsument " + id + " chce pobrać " + portion + " el");

            if(firstConsumerOccupied)
                consumerRest.await();
            firstConsumerOccupied = true;

            while (elementsInBuffer < portion)
                firstConsumer.await();


            System.out.println("Konsument " + id + " pobiera " + portion + " elementów");
            for(int i = elementsInBuffer - 1; i >= elementsInBuffer - portion; i--)
                buffer[i] = false;

            elementsInBuffer -= portion;
            System.out.println("    Stan bufora: "+ elementsInBuffer);

            firstConsumerOccupied = false;
            firstProducer.signal();
            consumerRest.signal();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}
