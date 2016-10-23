package simulator;

import java.util.Random;
import java.util.Scanner;

public class MainClass {

	public static void main(String[] args) {
		//array gigantesco onde simularemos o leitor e a aceitacao de slots
		int[] frames = new int[10000000]; 

		//constantes que indicarao onde estamos dentro do array 
		int frameStart = 0, frameEnd = 100, it=0;

		//scanner para descobrir junto ao usuario a chance de cada etiqueta querer transmitir
		Scanner in = new Scanner(System.in);
		System.out.println("digite o numero de etiquetas");
		int n  = Integer.parseInt(in.nextLine());
		System.out.println("digite a probabilidade de colisão das etiquetas p");
		int p = Integer.parseInt(in.nextLine());

		//randomico que usaremos para descobrir que etiqueta transmitira por frame
		Random r = new Random();
		int aux, collisions =0, empty =0, success = 0;
		//iteracao externa indica o slot a ser preenchido iteracao interna indica a etiqueta atual transmitindo
		do {
			for (int i = frameStart ; i<frameEnd ;i++  ) {

				for (int j = 0; j < n; j++ ){
					aux = r.nextInt(100);
					//procura o caso ideal em que a etiqueta conseguira alocar e o fram esta vazio
					if (aux>p){
						//etiqueta nao tenta transmitir, sai desta iteracao sem realizar alteracoes
						continue;
					} else if (aux <= p && frames[i]== 1){
						//colisao, pula 
						System.out.println(""+aux);
						frames[i] = 2 ;					
					} else if (aux <= p && frames[i]==0){
						//sem colisao, aloca e torce para nao colidir com as proximas :D
						frames[i] = 1;						
					}
					
				} 
				// vamos agora ver se houveram colisoes nas etiquetas passadas e reajustar o tamanho do frame  :)
				if (frames[i]==1){
					//decrementa o total de etiquetas para a próxima iteracao
					n--;
					success++;

				}  else if (frames[i]==2) {
					//incrementamos o inteiro colisoes para estatisticas futuras
					collisions++;
				} else {
					empty++;
				}
			}
			it++;
			//lower bound 
			if(collisions>0){
				frameStart = frameEnd;
				frameEnd = frameStart+collisions*2 ;
			} else {
				frameStart = frameEnd;
				frameEnd +=100;
				System.out.println("ops não colidiu");
			}
		} while (n>0);
		System.out.println("sucessos: "+success+" \n"+"vazios: "+empty+" \n"+"colisões: "+collisions+"\n"+"iteracoes: "+it);

	}

}
