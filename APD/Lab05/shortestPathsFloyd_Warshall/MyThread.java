package shortestPathsFloyd_Warshall;

import javax.xml.xpath.XPathVariableResolver;

public class MyThread implements Runnable{
    int id;
    int P;
    int[][] graph;

    MyThread(int id, int P, int[][] graph) {
        this.id = id;
        this.P = P;
        this.graph = graph;
    }

    @Override
    public void run() {
        int start = (int)(id * Math.ceil((float)5/(float)P));
        int end = Math.min(5, (int)((id + 1) * Math.ceil((float)5/(float)P)));

        for (int k = start; k < end; k++) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    graph[i][j] = Math.min(graph[i][k] + graph[k][j], graph[i][j]);
                }
            }
        }
    }
}
