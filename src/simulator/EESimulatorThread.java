package simulator;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class EESimulatorThread extends Thread {
	
	private static final int E = 0;
	private static final int S = 1;
	private double[] xData;
	private int j;
	private int simPerThread;
	private AtomicInteger totalEmpty;
	private AtomicInteger totalSuccess;
	private AtomicInteger totalCollisions;
	private AtomicInteger it;
	private int localIt;
	private int localTotalEmpty;
	private int localTotalSuccess;
	private int localTotalCollisions;
	private Random r;
	
	public EESimulatorThread (double[] xData, int j, int sim, AtomicInteger totalEmpty, AtomicInteger totalSuccess, AtomicInteger totalCollisions, AtomicInteger it, Random r) {
		this.xData = xData;
		this.j = j;
		this.simPerThread = sim;
		this.totalCollisions = totalCollisions;
		this.totalEmpty = totalEmpty;
		this.totalSuccess = totalSuccess;
		this.it = it;
		this.r = r;
		this.localIt = 0;
		this.localTotalCollisions = 0;
		this.localTotalEmpty = 0;
		this.localTotalSuccess = 0;
	}
	
	public void run () {
		int frames[] = new int[10000];
		int arrayLimiter = 0, aux = 0, i = 0;
		for(int m = 0; m < simPerThread; m++) {
			int n = (int) xData[j];
			double currQ = 4;
			double newQ = -1;
			int index = 0;
			double N, R = 0, oldR = -1;
			//double two_currQ = Math.pow(2, currQ);
			double frameEnd = Math.pow(2, currQ);
			double L1, L2, ps1, ps2;
			boolean allCols;
			do {
				allCols = false;
				
				arrayLimiter = (int) frameEnd;
				
				// Initialize array / Clear frame
				for (i = 0; i < arrayLimiter; i++) {
					frames[i] = 0;
				}
				
				// Apply random tag inputs in the frame
				for (i = 0; i < n; i++) {
					aux = r.nextInt(arrayLimiter);
					frames[aux]++;
				}
//				int cols = 0;
//				for (int k = 0; k < arrayLimiter; k++) {
//					if (frames[k] > 1) cols++;
//				}
//				if (cols == arrayLimiter) allCols = true;
				
				//if (allCols) {
					//frameEnd = 64;
				//} else {
					index = 0;
					newQ = -1;
					int suc = 0, c = 0;
					while (newQ == -1 && index <= Math.pow(2, currQ)) {
						switch(frames[index]) {
						case E:
							this.localTotalEmpty++;
							break;
						case S:
							//System.out.println("Antes: " + this.totalSuccess);
							this.localTotalSuccess++;
							//System.out.println("Depois: " + this.totalSuccess);
							suc += 1;
							n--;
							break;
						default:
							this.localTotalCollisions++;
							c += 1;
							break;
						}
						++index;
						double a1 = (4.344 * index) - 16.28;
						double a2 = (index / ((-2.282 - 0.273 * index) * c));
						double a3 = 0.2407 * Math.log(index + 42.56);
						double kl = (c / (a1 + a2)) + a3;
						double ll = (1.2592 + (1.513 * index)) * (Math.tan(1.234 * Math.pow(index, -0.9907) * c));
						if (kl < 0) kl = 0;
						N = (kl * suc) + ll;
						R = (N * Math.pow(2, currQ)) / index;
						if (c == 0) R = (suc * Math.pow(2, currQ)) / index;
						//System.out.println((Math.abs((R - oldR)) <= 1));
						if (((suc + c) != 0) && index > 1 && (Math.abs((R - oldR)) <= 1)) {
							L1 = Math.pow(2, currQ);
							double optQ = Math.ceil(Math.log(R) / Math.log(2)); // <-- Math.round não funciona ao zerar
							//System.out.println(optQ + " == " + (Math.log(R) / Math.log(2)));
							L2 = Math.pow(2, optQ);
							ps1 = (N/L1) * Math.pow((1 - (1/L1)), N - 1);
							ps2 = (N/L2) * Math.pow((1 - (1/L2)), N - 1);
							if ((Math.floor(((L1 * ps1) - suc)) < Math.floor((L2 * ps2)))) {newQ = optQ;}
						}
						oldR = R;
					}
					if (newQ != -1) {
						currQ = newQ;
						frameEnd = Math.floor(Math.pow(2, newQ));
					} else {
						newQ = currQ;
						frameEnd = Math.floor(Math.pow(2, newQ));
					}
				//}
				this.localIt++; // Frame count
				
			} while (n > 0);
			
		}
		// Update the values
		this.totalEmpty.addAndGet(localTotalEmpty);
		this.totalSuccess.addAndGet(localTotalSuccess);
		this.totalCollisions.addAndGet(localTotalCollisions);
		this.it.addAndGet(localIt);
	}
}
