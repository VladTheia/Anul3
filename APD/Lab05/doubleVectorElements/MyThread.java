package doubleVectorElements;
import java.lang.Math;

public class MyThread implements Runnable {
    int id;
    int P;
    int N;
    int[] v;

    MyThread(int id, int P, int N, int[] v) {
        this.id = id;
        this.P = P;
        this.N = N;
        this.v = v;
    }

    @Override
    public void run() {
        int start = (int)(id * Math.ceil((float)N/(float)P));
        int end = Math.min(N, (int)((id + 1) * Math.ceil((float)N/(float)P)));

        for (int i = start; i < end; i++) {
            v[i] = v[i] * 2;
        }
    }
}
