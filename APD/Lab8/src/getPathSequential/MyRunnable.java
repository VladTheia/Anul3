import java.util.concurrent.ExecutorService;
import java.util.*;

public class MyRunnable implements Runnable {
	ExecutorService tpe;
    ArrayList<Integer> partialPath;
    int destination;

	public MyRunnable(ExecutorService tpe, ArrayList<Integer> partialPath, int destination) {
		this.tpe = tpe;
        this.partialPath = partialPath;
        this.destination = destination;
	}

	@Override
	public void run() {
        if (partialPath.get(partialPath.size() - 1) == destination) {
			System.out.println(partialPath);
			tpe.shutdown();
		}

		int lastNodeInPath = partialPath.get(partialPath.size() - 1);
		for (int i = 0; i < Main.graph.length; i++) {
			if (Main.graph[i][0] == lastNodeInPath) {
				if (partialPath.contains(Main.graph[i][1]))
					continue;
				ArrayList<Integer> newPartialPath = (ArrayList<Integer>) partialPath.clone();
				newPartialPath.add(Main.graph[i][1]);
                tpe.submit(new MyRunnable(tpe, newPartialPath, destination));
			}
		}
	}

}
