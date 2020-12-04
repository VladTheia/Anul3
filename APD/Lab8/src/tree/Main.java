import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

	public static void main(String[] args) {
		Thread threads[] = new Thread[2];

		TreeNode tree = new TreeNode(1);
		threads[0] = new Thread(new ReadTreePart(tree, "treePart1.txt"));
		threads[1] = new Thread(new ReadTreePart(tree, "treePart2.txt"));
		for (int i = 0; i < 2; i++)
			threads[i].start();
		for (int i = 0; i < 2; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
        ExecutorService tpe = Executors.newFixedThreadPool(4);
		tpe.submit(new MyRunnable(tpe, tree));
	}
}
