/**
 * ESTA CLASSE INICIA AS THREADS QUE FARÃO AS CONTAS
 * COM OS ESTIMADORES DFSA QUE USAM EARLY-END
 * neste caso ILCM-sbs *
 */

package simulator;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class EESimulator implements Runnable {
	
	private static final int SIM_NUMBER = 10000; // Number of simulations
	private int nThreads;
	private double[] yData;
	private double[] xData;
	private String estimator;

	public EESimulator(String estimator, double[] xData, int nThreads) {
		this.estimator = estimator;
		this.xData = xData;
		this.yData = new double[xData.length];
		this.nThreads = nThreads;
	}

	public void run() {
		long simulatorInitialRuntime = System.nanoTime();

		Random r = new Random();

		for (int j = 0; j < xData.length; j++) {
			AtomicInteger it = new AtomicInteger();
			AtomicInteger totalCollisions = new AtomicInteger(), 
					totalEmpty = new AtomicInteger(), 
					totalSuccess = new AtomicInteger();
			
			long tInit = System.nanoTime(); // --- Begin Time
			
			// Share workload with threads
			int simPerThread = SIM_NUMBER / nThreads;
			EESimulatorThread[] eeSimulatorThreads = new EESimulatorThread[nThreads];
			for (int t = 0; t < nThreads; t++) {
				eeSimulatorThreads[t] = new EESimulatorThread(xData, j, simPerThread, totalEmpty, totalSuccess, totalCollisions, it, r);
				eeSimulatorThreads[t].start();
			}
			
			// Wait for threads to end simulations for current number of tags
			for(int m = 0; m < nThreads; m++) {
				try {
					eeSimulatorThreads[m].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			long tEnd = System.nanoTime(); // --- End Time

			this.yData[j] = (totalCollisions.get() + totalSuccess.get() + totalEmpty.get()) / SIM_NUMBER;
			
			// Putting data into file
			String[] strings = new String[xData.length];
			//strings[0] = "0 0";
			for (int i = 0; i < xData.length; i++) {
				strings[i] = xData[i] + " " + yData[i];
			}
			List<String> lines = Arrays.asList(strings);
			Path file = Paths.get(estimator + ".txt");
			try {
				Files.write(file, lines, Charset.forName("UTF-8"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("\nUsing " + estimator + " estimator.");
			System.out.println("Average of " + SIM_NUMBER + " simulations:");
			System.out.println("Delay[slots]: " + ((totalSuccess.get() / SIM_NUMBER) + (totalEmpty.get() / SIM_NUMBER) + (totalCollisions.get() / SIM_NUMBER)));
			System.out.println("Success: " + (totalSuccess.get() / SIM_NUMBER));
			System.out.println("Empty: " + (totalEmpty.get() / SIM_NUMBER));
			System.out.println("Collisions: " + (totalCollisions.get() / SIM_NUMBER));
			System.out.println("Frames: " + (it.get() / SIM_NUMBER));
			System.out.println("Average process time: " + (totalSuccess) + "us");
		}
		
		long simulatorFinalRuntime = System.nanoTime();
		System.out.println("Total simulation time: " + ((simulatorFinalRuntime - simulatorInitialRuntime) /1000000000) + "s");
		
	}

}
