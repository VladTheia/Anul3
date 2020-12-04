package parallelSearch;

public class Main {
    public static void main(String[] args) {
        int[] array = new int[10];
        for (int i = 0; i < 10; i++) {
            array[i] = i;
        }

        int target = 4;

        Thread threads[] = new Thread[3];
        for (int i = 0; i < 3; i++) {
            threads[i] = new Thread(new Search(i, 3, target, array));
        }
        for (int i = 0; i < 3; i++) {
            threads[i].start();
        }
        for (int i = 0; i < 3; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
