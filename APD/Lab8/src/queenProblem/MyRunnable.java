import java.util.concurrent.ExecutorService;
import java.util.*;

public class MyRunnable implements Runnable {
	ExecutorService tpe;
    int[] positions;
    int step;

	public MyRunnable(ExecutorService tpe, int[] positions, int step) {
		this.tpe = tpe;
        this.positions = positions;
        this.step = step;
	}

	@Override
	public void run() {
        if (step == Main.N) {
			printPositions(positions);
			tpe.shutdown();
		}

		// for the node at position step try all possible colors
		for (int i = 0; i < Main.N; i++) {
			int[] newPositions = positions.clone();
			newPositions[step] = i;
			if (verifyPositions(newPositions, step))
				tpe.submit(new MyRunnable(tpe, newPositions, step+1));
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

}
