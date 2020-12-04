import java.io.*;
import java.util.*;
public class ReadTreePart implements Runnable {
	TreeNode tree;
	String fileName;

	public ReadTreePart(TreeNode tree, String fileName) {
		this.tree = tree;
		this.fileName = fileName;
	}

	@Override
	public void run() {
        File file = new File(fileName);
        try {
            Scanner scanner = new Scanner(file);
    
            while (scanner.hasNext()) {
                int c = scanner.nextInt();
                int p = scanner.nextInt();
                
                TreeNode parent = null;
                while(parent == null)
                    parent = tree.getNode(p);
                parent.addChild(new TreeNode(c));
            }
            scanner.close();
        } catch (Exception e) {}
	}
}
