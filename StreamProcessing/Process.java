import java.util.Random;

/**
 * Created by Justyna Gorczyca on 16.11.2017.
 */
public class Process implements Runnable{
    private int id;
    private Buffer buffer;
    private int position;
    private String processName;
    private int delay;

    //utworzenie procesu
    public Process(int id, Buffer buffer, String processName) {
        //System.out.println("    Proces: " + id + " zaczyna przetwarzanie bufora.");
        this.id = id;
        this.buffer = buffer;
        this.position = 0;
        this.processName = processName;
        this.delay = new Random().nextInt(50);
    }

    public void run() {
        for(int i = 0; i < Main.M; i++) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            buffer.get(id, position, processName); //pobierz
            buffer.changeBufferContent(position, processName);
            int newPosition = buffer.put(id, position);
            position = newPosition;
        }
    }
}
