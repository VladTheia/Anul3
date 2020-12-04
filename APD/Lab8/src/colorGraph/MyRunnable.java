import java.util.concurrent.ExecutorService;
import java.util.*;

public class MyRunnable implements Runnable {
	ExecutorService tpe;
    int[] colors;
    int step;

	public MyRunnable(ExecutorService tpe, int[] colors, int step) {
		this.tpe = tpe;
        this.colors = colors;
        this.step = step;
	}

	@Override
	public void run() {
        if (step == Main.N) {
			printColors(colors);
			tpe.shutdown();
		}

		// for the node at position step try all possible colors
		for (int i = 0; i < Main.COLORS; i++) {
			int[] newColors = colors.clone();
			newColors[step] = i;
			if (verifyColors(newColors, step))
                tpe.submit(new MyRunnable(tpe, newColors, step+1));
		}
        
	}

    private static boolean verifyColors(int[] colors, int step) {
		for (int i = 0; i < step; i++) {
			if (colors[i] == colors[step] && isEdge(i, step))
				return false;
		}
		return true;
	}

	private static boolean isEdge(int a, int b) {
		for (int i = 0; i < Main.graph.length; i++) {
			if (Main.graph[i][0] == a && Main.graph[i][1] == b)
				return true;
		}
		return false;
	}
	
	static void printColors(int[] colors) {
		String aux = "";
		for (int i = 0; i < colors.length; i++) {
			aux += colors[i] + " ";
		}
		System.out.println(aux);
	}

}
