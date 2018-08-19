import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Justyna Gorczyca on 16.11.2017.
 */

public class Buffer {
    private final Lock lock = new ReentrantLock();
    private final Condition[] isMyTurn = new Condition[Main.N]; //czy proces B modyfikuje pole po procesie A
    private String[] bufforContent = new String[Main.M];
    private int[] lastVisitor = new int[Main.M];

    public Buffer() {

        for(int i = 0; i < Main.N; i++) {
            isMyTurn[i] = lock.newCondition();
        }

        for(int i =0; i < Main.M; i++) { //wszystkie miejsca w buforze są na początku do modyfikowania
            bufforContent[i] = "";
            lastVisitor[i] = -1;
        }
    }

    public void get(int id, int positon, String processName) {
        lock.lock();
        try{
            //System.out.println("Wchodzi proces " + processName);
            while((lastVisitor[positon] != id-1))
                isMyTurn[id].await();
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public void changeBufferContent(int position, String toAdd) {
        bufforContent[position] += toAdd;
        //System.out.println("    Proces " + toAdd + " zmienił pole " + position + " na " + bufforContent[position]);
    }

    public int put(int id, int position) {
        lock.lock();
        try {
            lastVisitor[position] = id;
            if(id != Main.N-1) isMyTurn[id+1].signal();
            position =(position+1)%Main.M;
        } finally {
            lock.unlock();
        }
        return position;
    }

    public String getContent(int x) {
        return bufforContent[x];
    }
}
