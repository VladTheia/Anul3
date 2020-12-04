package listReadAndWrite;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Main {
    static ArrayList<Integer> mainList = new ArrayList<>();

    public static void main(String[] args) {
        Semaphore sem = new Semaphore(-2);
        Thread threads[] = new Thread[4];

        for (int i = 0; i < 4; i++)
            threads[i] = new Thread(new MyThread(i, sem));
        for (int i = 0; i < 4; i++)
            threads[i].start();
        for (int i = 0; i < 4; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < mainList.size(); i++) {
            System.out.println(mainList.get(i));
        }
    }
}
