import java.util.Random;

/**
 * Created by Justyna Gorczyca on 29.11.2017.
 */
public class Producer implements Runnable{
    private int id;
    Buffer buffer;
    private int portion;
    private int delay;

    public Producer (int id, Buffer buffer) {
        this.id = id;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while(true) {
            delay = new Random().nextInt(50);
            portion = new Random().nextInt(Main.M);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            buffer.write(id,portion);
        }
    }
}
