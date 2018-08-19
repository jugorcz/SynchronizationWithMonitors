import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Justyna Gorczyca on 30.11.2017.
 */
public class Buffer {

    private final Lock lock = new ReentrantLock();
    private final Condition canUseBuffer = lock.newCondition();

    private int bufferSize;
    private boolean[] buffer;
    private int elementsInBuffer;

    public Buffer (int buffSize) {
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
            while (bufferSize - elementsInBuffer < portion)
                canUseBuffer.await();

            System.out.println("Producent " + id + " wstawia " + portion + " elementów");
            for(int i = elementsInBuffer; i < elementsInBuffer + portion; i++)
                buffer[i] = true;

            elementsInBuffer += portion;
            System.out.println("    Stan bufora: "+ elementsInBuffer);
            canUseBuffer.signalAll();

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
            while (elementsInBuffer < portion)
                canUseBuffer.await();

            System.out.println("Konsument " + id + " pobiera " + portion + " elementów");
            for(int i = elementsInBuffer - 1; i >= elementsInBuffer - portion; i--)
                buffer[i] = false;

            elementsInBuffer -= portion;
            System.out.println("    Stan bufora: "+ elementsInBuffer);
            canUseBuffer.signalAll();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}
