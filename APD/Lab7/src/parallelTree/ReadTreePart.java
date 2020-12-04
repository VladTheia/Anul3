package parallelTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author cristian.chilipirea
 *
 */
public class ReadTreePart implements Runnable {
	TreeNode tree;
	String fileName;

	public ReadTreePart(TreeNode tree, String fileName) throws FileNotFoundException {
		this.tree = tree;
		this.fileName = fileName;
	}

	public void ReadTreeFromFile() {
		File file = new File(fileName);
		Scanner sc = new Scanner(fileName);

		while (sc.hasNextInt())
			tree.addChild(new TreeNode(sc.nextInt()));
	}

	@Override
	public void run() {

	}
}
