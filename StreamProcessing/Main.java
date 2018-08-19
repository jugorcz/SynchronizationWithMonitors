import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Justyna Gorczyca on 16.11.2017.
 */
public class Main {
    public final static int N = 6; //procesy
    public final static int M = 100; //bufor
    public final static String[] processNames = {"A","B","C","D","E","F"};

    public static void main(String[] args) {
        Buffer buffer = new Buffer();
        List<Thread> threadList = new ArrayList<>();

        try {
            for (int i = 0; i < N; i++) {
                Thread t = new Thread(new Process(i, buffer, processNames[i]));
                threadList.add(t);
                t.start();
            }
            for(Thread t : threadList)
                t.join();
        } catch (Exception e) {}

        for(int i = 0; i < M; i++)
            System.out.println("Pole nr " + i + ": " + buffer.getContent(i));

    }

}
