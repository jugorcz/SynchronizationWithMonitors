/**
 * Created by Justyna Gorczyca on 29.11.2017.
 */

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static final int Producers = 10;
    public static final int Consumers = 10;
    public static final int M = 100; //połowa bufora

    public static void main(String args[]) {
        Buffer buffer = new Buffer(2*M);
        List<Thread> threadList = new ArrayList<>();
        for(int i = 0; i < Producers; i++){
            Producer p = new Producer(i,buffer);
            Thread t = new Thread(p);
            threadList.add(t);
        }

        for(int i = 0; i < Consumers; i++){
            Consumer c  = new Consumer(i,buffer);
            Thread t = new Thread(c);
            threadList.add(t);
        }

        for(Thread t: threadList)
            t.start();

        for(Thread t: threadList)
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
}
