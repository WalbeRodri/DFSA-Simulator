package simulator;

import java.util.Random;
import java.util.Scanner;

public class MainClass {

	private static final int E = 0;
	private static final int S = 1;
	private static final int INITIAL_FRAME_SIZE = 128;
	private static final int SIM_NUMBER = 2000; // Number of simulations

	public static void main(String[] args) {
		//array gigantesco onde simularemos o leitor e a aceitacao de slots
		int[] frames = new int[10000];

		//constantes que indicarao onde estamos dentro do array 
		double frameEnd = INITIAL_FRAME_SIZE, it = 0;

		Scanner in = new Scanner(System.in);
		System.out.println("digite o numero de etiquetas");
		int n = Integer.parseInt(in.nextLine());
		int nBackup = n;
		in.close();

		Random r = new Random();
		int aux, i = 0;
		int collisions = 0, empty = 0, success = 0;
		int totalCollisions = 0, totalEmpty = 0, totalSuccess = 0;
		int arrayLimiter = 0;

		for(int s = 0; s < SIM_NUMBER; s++) {
			n = nBackup;
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
				for (i = 0; i < n; i++) {
					aux = r.nextInt(arrayLimiter);
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
				it++;

				totalCollisions += collisions;
				totalEmpty += empty;
				totalSuccess += success;

				// lower bound
				if (collisions > 0) {
					frameEnd = (collisions * 2.39);
				} else frameEnd = INITIAL_FRAME_SIZE;

			} while (n > 0);
			
		}
		
		System.out.println("\nAverage of " + SIM_NUMBER + " simulations:");
		System.out.println("Success: " + (totalSuccess / SIM_NUMBER));
		System.out.println("Empty: " + (totalEmpty / SIM_NUMBER));
		System.out.println("Collisions: " + (totalCollisions / SIM_NUMBER));
		System.out.println("Frames: " + (it / SIM_NUMBER));
	}

}
