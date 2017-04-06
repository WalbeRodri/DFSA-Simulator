package charts;

import java.util.ArrayList;
import java.util.Iterator;

import simulator.EESimulator;
import simulator.NoEESimulator;

public class XChart {
	
	private static final double PASSO = 100;
	private static final double QTD = 1000 / PASSO;
	private static final String chartInfo = "totalSlots";
	
	public static void main(String[] args) throws Exception {
		double[] xData;
		//String ee = "withEE";
		String ee = "noEE";
		
		/**
		 * COM EARLY-END
		 */
		if (ee.equals("withEE")) {
			//xData = new double[] { 50, 100, 150, 200, 250 };
			xData = new double[26];
			int j = 0;
			for (int i = 0; i < 260;) {
				xData[j++] = i;
				i += 10;
			}
			
			String[] estimatorsEE = {"ILCM-sbs"};
			Thread[] withEEthreads = new Thread[estimatorsEE.length];
			for (int i = 0; i < estimatorsEE.length; i++) {
				withEEthreads[i] = new Thread(new EESimulator(estimatorsEE[i], xData, 2));
				withEEthreads[i].start();
			}
			
			for (int i = 0; i < withEEthreads.length; i++) {
				withEEthreads[i].join();
			}
		} else {
			/**
			 * SEM EARLY-END
			 */
//			xData = new double[] { 10, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000 };
//			xData = new double[qtd + 1];
//			xData[0] = 1;
//			for (int i = 1; i < 1000;) {
//				xData[i] = j;
//			}
			double j = PASSO;
			ArrayList<Double> a = new ArrayList<Double>();
			for (int i = 0; i < QTD; i++) { a.add(j); j += PASSO; }
			if (PASSO != 1) a.add(0, 1.0);
			xData = new double[a.size()];
			Iterator<Double> it = a.iterator();
			int i = 0;
			while(it.hasNext()) xData[i++] = it.next();
			
//			String[] noEEestimators = {"LowerBound", "Schoute", "Eom-Lee", "Vogt", "Vogt(Eom-Lee)", "IV-2"};
			String[] noEEestimators = {"Vogt(Eom-Lee)"};
			Thread[] noEEthreads = new Thread[noEEestimators.length];
			for (i = 0; i < noEEthreads.length; i++) {
				noEEthreads[i] = new Thread(new NoEESimulator(noEEestimators[i], chartInfo, xData, 20));
				noEEthreads[i].start();
			}
			
			
			for (i = 0; i < noEEthreads.length; i++) {
				noEEthreads[i].join();
			}
		}

	}
}