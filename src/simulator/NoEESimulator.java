package simulator;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NoEESimulator implements Runnable {

	private static final int E = 0;
	private static final int S = 1;
	private static final int INITIAL_FRAME_SIZE = 64;
	private static final int SIM_NUMBER = 2000; // Number of simulations
	private double[] yData;
	private double[] xData;
	private String estimator;
	private String chartInfo;
	private int nThreads;

	public NoEESimulator(String estimator, String chartInfo, double[] xData, int nThreads) {
		this.estimator = estimator;
		this.chartInfo = chartInfo;
		this.xData = xData;
		this.yData = new double[xData.length];
		this.nThreads = nThreads;
	}

	public void run() {
		int[] frames = new int[10000];

		Random r = new Random();
		MathUtil mu = new MathUtil();
		Scanner scanner = null;
//		try {
//			scanner = new Scanner(new File("data\\RN.txt"));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		int simPerThread = SIM_NUMBER / nThreads;
		// Share workload with threads
		double[][][] simulationData = new double[xData.length][nThreads][simPerThread];
		for (int j = 0; j < xData.length; j++) {
			AtomicInteger it = new AtomicInteger(),
					totalCollisions = new AtomicInteger(), 
					totalEmpty = new AtomicInteger(), 
					totalSuccess = new AtomicInteger();
			AtomicLong add = new AtomicLong(),
					sub = new AtomicLong(),
					mult = new AtomicLong(),
					comp = new AtomicLong(),
					div = new AtomicLong(),
					sqrt = new AtomicLong(),
					exp = new AtomicLong(),
					log = new AtomicLong(),
					trig = new AtomicLong(),
					fac = new AtomicLong(),
					eom_lee_iterations = new AtomicLong();
			NoEESimulatorThread[] noEESimulatorThreads = new NoEESimulatorThread[nThreads];
			for (int t = 0; t < nThreads; t++) {
				noEESimulatorThreads[t] = new NoEESimulatorThread(xData, j, simPerThread,
						totalEmpty, totalSuccess, totalCollisions, it, r,
						add, sub, mult, comp, div, sqrt, exp, log, trig,
						fac, eom_lee_iterations, INITIAL_FRAME_SIZE, estimator,
						simulationData, t);
				noEESimulatorThreads[t].start();
			}
			
			// Wait for threads to end simulations for current number of tags
			for(int m = 0; m < nThreads; m++) {
				try {
					noEESimulatorThreads[m].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			switch (this.chartInfo) {
			case "totalSlots":
				printTotalSlots(j, it.get(), totalCollisions.get(), totalEmpty.get(), totalSuccess.get());
				// This part outputs the 2000 simulations
				//strings[0] = "0 0";
				List<String> lines = new ArrayList<String>();
				for (int i = 0; i < xData.length; i++) {
					for (int k = 0; k < nThreads; k++) {
						for (int k2 = 0; k2 < simPerThread; k2++) {
							lines.add(xData[i] + " " + simulationData[i][k][k2]);							
						}
					}
				}
				Path file = Paths.get("output/" + estimator + "-" + chartInfo + "-" + INITIAL_FRAME_SIZE + "-DETAIL.txt");
				try {
					Files.write(file, lines, Charset.forName("UTF-8"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case "detailedFLOP":
				printDetailedFLOP(j, add, sub, mult, div, comp, sqrt, exp, log, trig, fac);
				break;
			case "FLOP":
				System.out.println("\nUsing " + estimator + " estimator.");
				System.out.println("Tags = " + xData[j]);
//				System.out.println("Adição: " + add / SIM_NUMBER);
//				System.out.println("Subtração: " + sub / SIM_NUMBER);
//				System.out.println("Multiplicação: " + mult / SIM_NUMBER);
//				System.out.println("Divisão: " + div / SIM_NUMBER);
//				System.out.println("Comparação: " + comp / SIM_NUMBER);
//				System.out.println("Raiz quadrada: " + sqrt / SIM_NUMBER);
//				System.out.println("Exponenciação: " + exp / SIM_NUMBER);
//				System.out.println("Logarítmica: " + log / SIM_NUMBER);
//				System.out.println("Trigonométrica: " + trig / SIM_NUMBER);
//				System.out.println("Fatorial: " + fac / SIM_NUMBER);
				// FLOP costs according to table used in research
				double value = (add.get() + sub.get() + mult.get() + trig.get() + 
						(div.get() * 10) + 
						(comp.get() * 2) + 
						(sqrt.get() * 10) + 
						(exp.get() * 50) + 
						(log.get() * 50) + 
						(fac.get() * 100)) / SIM_NUMBER;
				value = Math.ceil(value);
				if (estimator.equals("LowerBound") || estimator.equals("Schoute")) {
					if (value == 0) value = 0;
					else value = (value - 1) * 2;
				}
				this.yData[j] = Math.round(value);
				System.out.println(this.yData[j]);
				break;
			case "Iterations":
				System.out.println("\nUsing " + estimator + " estimator.");
				System.out.println("Tags = " + xData[j]);
				this.yData[j] = eom_lee_iterations.get() / SIM_NUMBER;
				System.out.println("Iterations = " + this.yData[j]);
				break;
			case "IDTime":
				System.out.println("\nUsing " + estimator + " estimator.");
				System.out.println("Tags = " + xData[j]);
				this.yData[j] = ((totalSuccess.get() * 2312 + totalCollisions.get() * 337.5 + totalEmpty.get() * 67.5) / SIM_NUMBER) / 1000000;
				System.out.println(totalSuccess.get() * 2312 + " + " + totalCollisions.get() * 337.5 + " + " +
				totalEmpty.get() * 67.5);
				break;
			default:
				break;
			}
		}
		
		// Putting data into file
		List<String> lines = new ArrayList<String>();
		for (int i = 0; i < xData.length; i++) {
			lines.add(xData[i] + " " + yData[i]);
		}
		Path file = Paths.get("output/" + estimator + "-" + chartInfo + "-" + INITIAL_FRAME_SIZE + ".txt");
		try {
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void printDetailedFLOP(int j, AtomicLong add,
				AtomicLong sub,
				AtomicLong mult,
				AtomicLong div,
				AtomicLong comp,
				AtomicLong sqrt,
				AtomicLong exp,
				AtomicLong log,
				AtomicLong trig,
				AtomicLong fac) {
		System.out.println("\nUsing " + estimator + " estimator.");
		System.out.println("Tags = " + xData[j]);
		System.out.println("Adição: " + add.get() / SIM_NUMBER);
		System.out.println("Subtração: " + sub.get() / SIM_NUMBER);
		System.out.println("Multiplicação: " + mult.get() / SIM_NUMBER);
		System.out.println("Divisão: " + div.get() / SIM_NUMBER);
		System.out.println("Comparação: " + comp.get() / SIM_NUMBER);
		System.out.println("Raiz quadrada: " + sqrt.get() / SIM_NUMBER);
		System.out.println("Exponenciação: " + exp.get() / SIM_NUMBER);
		System.out.println("Logarítmica: " + log.get() / SIM_NUMBER);
		System.out.println("Trigonométrica: " + trig.get() / SIM_NUMBER);
		System.out.println("Fatorial: " + fac.get() / SIM_NUMBER);
	}

	private void printTotalSlots(int j, double it, double totalCollisions, double totalEmpty, double totalSuccess) {
		double dtSuccess, dtEmpty, dtCollisions, idTime;
		dtSuccess = totalSuccess;
		dtEmpty = totalEmpty;
		dtCollisions = totalCollisions;
		/**
		 *  Identification time as per:
		 *  Ts = T4 + Tquery + 2*T1 + 2*T2 + Trn16 + Tack + Tpc+epc+crc16 + Tqrep = 2312us
		 *  Tc = T1 + T2 + Trn16 = 337.5us
		 *  Te = T1 + T3 = 67.5us
		 */
		idTime = ((dtSuccess * 2312 + dtCollisions * 337.5 + dtEmpty * 67.5) / SIM_NUMBER) / 1000000; // <-- in seconds

		this.yData[j] = (totalCollisions + totalSuccess + totalEmpty) / SIM_NUMBER;
		//this.yData[j] = idTime; //--> used to show identification time calculations as graphs
		System.out.println("\nUsing " + estimator + " estimator.");
		System.out.println("Average of " + SIM_NUMBER + " simulations:");
		System.out.println("Total slots: " + (int) this.yData[j]);
		System.out.println("Success: " + (totalSuccess / SIM_NUMBER));
		System.out.println("Empty: " + (totalEmpty / SIM_NUMBER));
		System.out.println("Collisions: " + (totalCollisions / SIM_NUMBER));
		System.out.println("Frames: " + (it / SIM_NUMBER));
		System.out.println("Average identification time: " + idTime + "s");
	}

}
