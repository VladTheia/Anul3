import java.util.concurrent.ExecutorService;

public class MyRunnable implements Runnable {
	TreeNode tree;
	ExecutorService tpe;

	public MyRunnable(ExecutorService tpe, TreeNode tree) {
		this.tree = tree;
		this.tpe = tpe;
	}

	@Override
	public void run() {
		if (tree == null) {
			tpe.shutdown();
			return;
		}
		System.out.print(tree.name + " ");
        tpe.submit(new MyRunnable(tpe, tree.left));
        tpe.submit(new MyRunnable(tpe, tree.right));
	}

}
