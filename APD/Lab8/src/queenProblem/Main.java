import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
	static int N = 4;

	static void findPositions(int[] positions, int step) {
		if (step == N) {
			printPositions(positions);
			return;
		}

		// for the node at position step try all possible colors
		for (int i = 0; i < N; i++) {
			int[] newPositions = positions.clone();
			newPositions[step] = i;
			if (verifyPositions(newPositions, step))
				findPositions(newPositions, step + 1);
		}
	}

	private static boolean verifyPositions(int[] positions, int step) {
		for (int i = 0; i <= step; i++) {
            for (int j = 0; j <= step; j++) {
			    if (i != j && positions[i] == positions[j])
				    return false;
                if (i != j && positions[i] - i == positions[j] - j)
				    return false;
                if (i != j && positions[i] + i == positions[j] + j)
				    return false;
            }
		}
		return true;
	}
	
	static void printPositions(int[] positions) {
		String aux = "";
		for (int i = 0; i < positions.length; i++) {
			aux += positions[i] + " ";
		}
		System.out.println(aux);
	}

	public static void main(String[] args) {
		int[] positions = new int[N];
		ExecutorService tpe = Executors.newFixedThreadPool(4);
		tpe.submit(new MyRunnable(tpe, positions, 0));
	}
}
