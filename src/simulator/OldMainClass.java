package simulator;

import java.util.Random;
import java.util.Scanner;

public class OldMainClass {
	
	private static final int E = 0;
	private static final int S = 1;
	private static final int C = 2;
	private static final int FRAME_SIZE = 128;
	
	public static void main(String[] args) {
		//array gigantesco onde simularemos o leitor e a aceitacao de slots
		int[] frames = new int[1000000];

		//constantes que indicarao onde estamos dentro do array 
		double frameStart = 0, frameEnd = FRAME_SIZE, it = 0;

		//scanner para descobrir junto ao usuario a chance de cada etiqueta querer transmitir
		Scanner in = new Scanner(System.in);
		System.out.println("digite o numero de etiquetas");
		int n = Integer.parseInt(in.nextLine());
		//System.out.println("digite a probabilidade de colis�o das etiquetas p");
		//int p = Integer.parseInt(in.nextLine());

		//randomico que usaremos para descobrir que etiqueta transmitira por frame
		Random r = new Random();
		int aux, collisions = 0, empty = 0, success = 0, m = 0;
		int totalCollisions = 0;
		double p = 0;
		// iteracao externa indica o slot a ser preenchido iteracao interna indica a etiqueta atual transmitindo
		do {
			//if (it > 1) {
				//p = (n / (frameEnd - frameStart)) * 100;
			//} else 
			p = (1 / (frameEnd - frameStart)) * 100;
			System.out.println("(" + 1 + " / " + (frameEnd - frameStart) + ") * 100 = " + p);
			for (int i = (int) frameStart; i < frameEnd; i++) {
				for (int j = 0; j < (n - m); j++) {
					aux = r.nextInt(100);
					//System.out.println("aux = " + aux + " / p = " + p);
					// procura o caso ideal em que a etiqueta conseguira alocar e o fram esta vazio
					if (aux > p) {
						// etiqueta nao tenta transmitir, sai desta iteracao sem realizar alteracoes
						continue;
					} else if (frames[i] == S || frames[i] == C) {
						// colisao, pula
						// System.out.println("" + aux);
						frames[i] = C;
						m++;
					} else if (frames[i] == E) {
						// sem colisao, aloca e torce para nao colidir com as proximas :D
						frames[i] = S;
					}
				}
				// vamos agora ver se houveram colisoes nas etiquetas passadas e reajustar o tamanho do frame  :)
				if (frames[i] == S) {
					// decrementa o total de etiquetas para a pr�xima iteracao
					n--;
					success++;
				} else if (frames[i] == C) {
					// incrementamos o inteiro colisoes para estatisticas futuras
					collisions++;
					totalCollisions++;
				} else {
					empty++;
				}
			}
			it++;
			m = 0;
			// lower bound
			if (collisions > 0) {
				frameStart = frameEnd;
				frameEnd = (frameStart + (collisions * 2));
				//System.out.println(collisions * 2);
			} else {
				frameStart = frameEnd;
				frameEnd += FRAME_SIZE;
				System.out.println("ops n�o colidiu");
			}
			collisions = 0;
		} while (n > 0);
		System.out.println("sucessos: "+success+" \n"+"vazios: "+empty+" \n"+"colis�es: "+totalCollisions+"\n"+"iteracoes: "+it);

	}

}