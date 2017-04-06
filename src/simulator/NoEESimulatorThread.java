package simulator;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NoEESimulatorThread extends Thread {

	private static final int E = 0;
	private static final int S = 1;
	//"JAVA", "AKARI1" , "AKARI2", "TRUERANDOM", 
	private static final String RANDOM = "AKARI2";
	private double[] xData;
	private int j;
	private int simPerThread;
	private AtomicInteger totalEmpty;
	private AtomicInteger totalSuccess;
	private AtomicInteger totalCollisions;
	private AtomicInteger it;
	AtomicLong addAtomic;
	AtomicLong subAtomic;
	AtomicLong multAtomic;
	AtomicLong compAtomic;
	AtomicLong divAtomic;
	AtomicLong sqrtAtomic;
	AtomicLong expAtomic;
	AtomicLong logAtomic;
	AtomicLong trigAtomic;
	AtomicLong facAtomic;
	AtomicLong eom_lee_iterationsAtomic;
	private int localIt;
	private int localTotalEmpty;
	private int localTotalSuccess;
	private int localTotalCollisions;
	private long add = 0, sub = 0, mult = 0, comp = 0, div = 0,
			sqrt = 0, exp = 0, log = 0, trig = 0, fac = 0;
	private long eom_lee_iterations = 0;
	private Random r;
	private int init_frame_size;
	private String estimator;
	private double[][][] simulationData;
	private int tID;

	public NoEESimulatorThread (double[] xData, int j, int sim, AtomicInteger totalEmpty,
			AtomicInteger totalSuccess, AtomicInteger totalCollisions, AtomicInteger it, Random r,
			AtomicLong addA, AtomicLong subA, AtomicLong multA, AtomicLong compA,
			AtomicLong divA, AtomicLong sqrtA, AtomicLong expA, AtomicLong logA,
			AtomicLong trigA, AtomicLong facA, AtomicLong eom_leeI, int size, 
			String estimator, double[][][] simulationData, int tID) {
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
		this.addAtomic = addA;
		this.subAtomic = subA;
		this.multAtomic = multA;
		this.compAtomic = compA;
		this.divAtomic = divA;
		this.sqrtAtomic = sqrtA;
		this.expAtomic = expA;
		this.logAtomic = logA;
		this.trigAtomic = trigA;
		this.facAtomic = facA;
		this.eom_lee_iterationsAtomic = eom_leeI;
		this.init_frame_size = size;
		this.estimator = estimator;
		this.simulationData = simulationData;
		this.tID = tID;
	}

	public void run () {
		int frames[] = new int[10000];
		double frameEnd;
		int aux = 0, i = 0;
		double collisions = 0, empty = 0, success = 0;
		int arrayLimiter = 0;
		int n = 0;

		DataInputStream arquivo = null;
		try {
			arquivo = new DataInputStream(new FileInputStream("D:\\WorksPace _IC\\RNFM"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		MathUtil mu = new MathUtil();
		for(int s = 0; s < this.simPerThread; s++) {
			n = (int) xData[j];
			frameEnd = this.init_frame_size;
			//System.out.println("Sim " + s);
			double simCol = 0;
			double simEmp = 0;
			double simSuc = 0;
			do {
				collisions = 0;
				empty = 0;
				success = 0;

				arrayLimiter = (int) frameEnd;

				// Initialize array / Clear frame
				for (i = 0; i < arrayLimiter; i++) {
					frames[i] = 0;
				}

				// Apply random tag inputs in the frame
				int num1 = 0, num2 = 0;
				for (i = 0; i < n; i++) {
					switch(RANDOM){
					case "JAVA":
						aux = r.nextInt(arrayLimiter);
						break;
						
					case "AKARI1":
						try{
							num1 = arquivo.readInt();
							num2 = arquivo.readInt();
						} catch (Exception e) {					
						}
						aux = mu.Akari1(num1 + "", num2 + "")% arrayLimiter;;
//						aux = aux % arrayLimiter;
						break;
						
					case "AKARI2":
						try{
							num1 = arquivo.readInt();
							num2 = arquivo.readInt();
						} catch (Exception e) {					
						}
						aux = mu.Akari2(num1 + "", num2 + "") % arrayLimiter;
						break;
						
					case "TRUERANDOM":
						try {
							aux = arquivo.readInt();
						} catch (IOException e) {								
						}
//						System.out.println("era: "+aux+"\n");
						aux = Math.abs(aux) % arrayLimiter;
//						System.out.println("agora: "+aux+"\n");
						break;
						
					}
					frames[aux]++;
				}

				/* Count collisions, tags accepted 
		 		   and empty slots in the frame    */
				for (i = 0; i < arrayLimiter; i++) {

					switch(frames[i]) {

					case E:
						empty++;
						break;

					case S:
						success++;
						n--;
						break;

					default:
						collisions++;
						break;

					}

				}
				localIt++; // Frame count

				localTotalCollisions += collisions;
				localTotalEmpty += empty;
				localTotalSuccess += success;
				simCol += collisions;
				simEmp += empty;
				simSuc += success;

				//System.out.println("Col = " + collisions + " | Suc = " + success + " | Empt = " + empty + " | n = " + n + " | f = " + frameEnd);

				if (collisions > 0) {

					switch(estimator) {

					case "LowerBound":
						frameEnd = (collisions * 2); mult++;
						break;
					case "Schoute":
						frameEnd = (int) Math.ceil(collisions * 2.39); mult++;
						break;
					case "Eom-Lee":
						frameEnd = eom_lee(frameEnd, collisions, success);
						break;
					case "Vogt":
						frameEnd = vogt(frameEnd, collisions, empty, success);
						break;
					case "Vogt-Imp1":
						frameEnd = vogt_imp_1(frameEnd, collisions, empty, success);
						break;
					case "Vogt(Eom-Lee)":
						frameEnd = vogt_eom_lee(frameEnd, collisions, empty, success);
						break;
					case "IV-2":
						// first argument is the Delta value
						int size = this.init_frame_size;
						if (size == 64) frameEnd = iv2(4, frameEnd, collisions, empty, success);
						else if (size == 128) frameEnd = iv2(2, frameEnd, collisions, empty, success);
						break;
					default:
						break;

					}

				} 
				//else frameEnd = INITIAL_FRAME_SIZE;
			} while (n > 0);
			this.simulationData[this.j][this.tID][s] = (simCol + simEmp + simSuc);
		}

		// Update the values
		this.totalEmpty.addAndGet(localTotalEmpty);
		this.totalSuccess.addAndGet(localTotalSuccess);
		this.totalCollisions.addAndGet(localTotalCollisions);
		this.it.addAndGet(localIt);
		this.addAtomic.addAndGet(this.add);
		this.subAtomic.addAndGet(this.sub);
		this.multAtomic.addAndGet(this.mult);
		this.compAtomic.addAndGet(this.comp);
		this.divAtomic.addAndGet(this.div);
		this.sqrtAtomic.addAndGet(this.sqrt);
		this.expAtomic.addAndGet(this.exp);
		this.facAtomic.addAndGet(this.fac);
		this.logAtomic.addAndGet(this.log);
		this.trigAtomic.addAndGet(this.trig);
		this.eom_lee_iterationsAtomic.addAndGet(this.eom_lee_iterations);
	}

	/**
	 * Uses Petar Solic's efficient implementation in
	 * "Increasing throughput in RFID systems with
	 * large number of tags" while LIMITING the frame size
	 * by a power of 2 and maximum of 256 slots
	 */
	private double vogt(double frameEnd, double collisions, double empty, double success) {
		comp++;
		if (frameEnd == collisions) {
			mult++;
			return chooseBestFrameSize(Math.ceil(2 * frameEnd)); //Math.ceil(Math.min(2 * collisions, 256));
		} else return chooseBestFrameSize(eq3(frameEnd, collisions, empty, success));
	}

	/**
	 * My interpretation of Vogt's article
	 * removing hardware limitations
	 */
	private double vogt_imp_1(double frameEnd, double collisions, double empty, double success) {
		//if (frameEnd == collisions) return chooseBestFrameSize(Math.min(256, Math.ceil(2 * frameEnd))); //Math.ceil(Math.min(2 * collisions, 256));
		//else return chooseBestFrameSize(eq3v(frameEnd, collisions, empty, success));
		return eq3v(frameEnd, collisions, empty, success);
	}

	/**
	 * Uses Petar Solic's efficient implementation in
	 * "Increasing throughput in RFID systems with
	 * large number of tags"
	 * This implementation, contrary to Vogt's original
	 * implementation, does not have size limitations
	 * for the frame size
	 */
	private double vogt_eom_lee(double frameEnd, double collisions, double empty, double success) {
		comp++;
		if (frameEnd == collisions) {
			mult++;
			return Math.ceil(2 * frameEnd); //Math.ceil(Math.min(2 * collisions, 256));
		} else return eq3(frameEnd, collisions, empty, success);
	}

	public double eq3v(double frameEnd, double collisions, double empty, double success) {
		double pe, ps, pc, tE, tS, tC;
		double nEstimate = (double) (success + (2.0 * collisions));	add++; mult++;
		//System.out.println(success + " --- " + collisions);
		//System.out.println(nEstimate + " antes (" + success + " + (2 * " + collisions + ")");
		double distN = -1.0;
		double distP = 0.0;
		while (distN < distP) {
			comp++;
			//pe = Math.pow((1.0 - (1.0 / frameEnd)), nEstimate);
			//ps = (nEstimate / frameEnd) * Math.pow(1.0 - (1.0 / frameEnd), nEstimate - 1.0);
			// r = occupancy number
			pe = Binomial(nEstimate, frameEnd, 0); // r = 0 = EMPTY
			ps = Binomial(nEstimate, frameEnd, 1); // r = 1 = SUCCESS
			pc = (1.0 - pe) - ps; sub+=2;
			tE = pe * frameEnd; mult++;
			tS = ps * frameEnd; mult++;
			tC = pc * frameEnd; mult++;
			distP = distN;
			distN = Math.pow(tE - empty, 2.0) + 
					Math.pow(tS - success, 2.0) + 
					Math.pow(tC - collisions, 2.0);
			exp+=3; sub+=3; add+=2;
			comp++; add++; mult++;
			if (nEstimate == (success + (2.0 * collisions))) { distP = distN + 1.0; add++; }
			nEstimate = nEstimate + 1.0; add++;
		}
		comp++; sub+=2;
		//System.out.println(nEstimate + " depois");
		return ((nEstimate - 1.0) - success);
	}

	private double Binomial (double n, double N, double r) {
		mult+=2; exp+=2; div+=2; sub+=2;
		return Comb(n, r) * Math.pow(1.0 / N, r) * Math.pow((N-1.0) / N, n - r);
	}

	private double Comb(double n, double r) {
		double result = 1;
		sub++;
		for (double i = 0; i < n-r; i++) {
			result = (result * (n-i)) / (i+1);
			mult++; sub++; div++; add++;
			comp++; add++;
		}

		return result;
	}

	private double eq3(double frameEnd, double collisions, double empty, double success) {
		double nEstimate = success + (2 * collisions); add++; mult++;
		double newEpsilon = -1;
		double oldEpsilon = 0;
		double a0 = 0, a1 = 0, a2 = 0;
		while (newEpsilon < oldEpsilon) {
			comp++;
			a0 = Math.pow((1.0 - (1.0 / frameEnd)), nEstimate); exp++; sub++; div++;
			div++; mult++; exp++; sub++; div++; sub++;
			a1 = (nEstimate / frameEnd) * Math.pow((1.0 - (1.0 / frameEnd)), nEstimate - 1.0);
			a2 = 1.0 - (a1 + a0); sub++; add++;
			a0 = a0 * frameEnd; mult++;
			a1 = a1 * frameEnd; mult++;
			a2 = a2 * frameEnd; mult++;
			oldEpsilon = newEpsilon;
			sqrt++; exp++; sub++; add++; exp++; sub++; add++; exp++; sub++;
			newEpsilon = Math.sqrt(Math.pow(a0-empty, 2.0) + Math.pow(a1-success, 2.0) + Math.pow(a2-collisions, 2.0));
			comp++; add++; mult++; add++;
			if (nEstimate == success + (2 * collisions)) oldEpsilon = newEpsilon + 1.0;
			nEstimate = nEstimate + 1.0; add++;
			//if (Math.abs(oldEpsilon - newEpsilon) < 0.00001) break;
		}
		sub++; sub++;
		return ((nEstimate - 1.0) - success);
	}

	private double chooseBestFrameSize(double n) {
		double size = 256;
		if (n > 0 && n < 10) size = 16;
		else if (n > 9 && n < 18) size = 32;
		else if (n > 17 && n < 52) size = 64;
		else if (n > 51 && n < 113) size = 128;
		return size;
	}

	/**
	 * Improved Vogt II from the article "Um Estimador Acurado para o
	 * Protocolo DFSA em Sistemas RFID"
	 * The article can be accessed through http://www.cin.ufpe.br/~pasg/gpublications/AnGo13.pdf
	 */
	public double iv2(int delta, double frameEnd, double collisions, double empty, double success) {
		double result = 0;
		comp++;
		if (frameEnd != collisions) {
			return eq3v(frameEnd, collisions, empty, success); // Resultado da eq. 3
		} else {
			if (delta == 0) { result = Math.floor(2.001001000 * (frameEnd - 1) + 2); comp++; }
			else if (delta == 1) { result = Math.floor(3.947947950 * (frameEnd - 1) + 2); comp += 2; }
			else if (delta == 2) { result = Math.floor(6.851851850 * (frameEnd - 1) + 2); comp += 3; }
			else if (delta == 3) { result = Math.floor(9.497497500 * (frameEnd - 1) + 2); comp += 4; }
			else if (delta == 4) { result = Math.floor(12.047047047 * (frameEnd - 1) + 2); comp += 5; }
			else if (delta == 5) { result = Math.floor(14.518518500 * (frameEnd - 1) + 2); comp += 6; }
			else if (delta == 6) { result = Math.floor(17.011011000 * (frameEnd - 1) + 2); comp += 7; }
		}
		return result;
	}

	private double eom_lee(double frameEnd, double collisions, double success) {
		double beta;
		double newGamma = 2.0;
		double oldGamma;
		double num;
		double den;
		do {
			oldGamma = newGamma;
			beta = frameEnd / ((oldGamma * collisions) + success); div++; mult++; add++;
			num = (1.0 - Math.exp(-(1.0 / beta))); sub++; exp++; div++;
			den = (beta * (1.0 - (1.0 + (1.0 / beta)) * Math.exp(-(1.0 / beta)))); mult++; sub++; add++; div++;
			newGamma = num / den; div++;
			sub++; comp++; comp++;
			eom_lee_iterations++;
		} while (Math.abs(oldGamma - newGamma) >= 0.001);
		mult++;
		return Math.ceil(newGamma * collisions);
	}
}
