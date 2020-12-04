package listReadAndWrite;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class MyThread implements Runnable {
    int id;
    Semaphore sem;
    ArrayList<Integer> localList = new ArrayList<>();

    MyThread(int id, Semaphore sem) {
        this.id = id;
        this.sem = sem;
    }

    public void readList(int id) throws FileNotFoundException {
        if (id != 3) {
            String filename = "D:\\Facultate\\Sem1\\APD\\Lab7\\elemente" + (id + 1) + ".txt";
            File file = new File(filename);
            Scanner sc = new Scanner(file);

            while (sc.hasNextInt())
                localList.add(sc.nextInt());
        }
    }

    public void putInList(int id) {
        if (id != 3) {
            for (int i = 0; i < this.localList.size(); i++)
                Main.mainList.add(this.localList.get(i));
        }

        sem.release();
    }

    public void sortList(int id) throws InterruptedException {
        if (id == 3) {
            sem.acquire();
            Collections.sort(Main.mainList);
        }
    }

    @Override
    public void run() {
        synchronized (this) {
            try {
                readList(this.id);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        synchronized (this) {
            putInList(id);
        }

        try {
            sortList(id);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
