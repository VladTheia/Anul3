
import java.util.concurrent.atomic.AtomicInteger;

class MyLock {
	private AtomicInteger sw = new AtomicInteger(1);
	
	void lock() {
		while(!sw.compareAndSet(1, 0))
			;
	}
	
	void unlock() {
		sw.set(1);
	}
}
public class TreeNode {
	int name;
	public TreeNode left = null;
	public TreeNode right = null;
    MyLock lock = new MyLock();

	TreeNode(int name) {
		this.name = name;
	}

	void addChild(TreeNode child) {
        lock.lock();
		if (left == null) {
			left = child;
		}
        else {
		    right = child;
        }
        lock.unlock();
	}

	TreeNode getNode(int name) {
		TreeNode aux = null;
		if (this.name == name)
			return this;
		if (left != null)
			aux = left.getNode(name);
		if (aux != null)
			return aux;
		if (right != null)
			aux = right.getNode(name);
		if (aux != null)
			return aux;
		return null;
	}
}
