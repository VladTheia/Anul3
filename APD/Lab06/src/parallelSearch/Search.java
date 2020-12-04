package parallelSearch;

public class Search implements Runnable {
    int target;
    int id;
    int P;
    int[] array;

    public Search(int id, int P, int target, int[] array) {
        this.target = target;
        this.id = id;
        this.P = P;
        this.array = array;
    }

    @Override
    public void run() {
        int start = (int)(id * Math.ceil((float)10/(float)P));
        int end = Math.min(10, (int)((id + 1) * Math.ceil((float)10/(float)P)));


    }
}
