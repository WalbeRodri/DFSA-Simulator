package simulator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class MathUtil {
	public final int U = (int) 7F;
	public final int S = (int) 07;
	public final int T = (int) 1F;
	public final int L = 262143;
	public final int P = (int) 7F;
//	public final BigInteger B = 675053731862; 
//	public final long C = 1029819072534;
	

	public MathUtil(){

	}
	
	public int Akari1(String  m1, String m2){
		/*Initialize x0 and x1 of m-bits 
	    	x0 = x0 + ((x0 * x0) v 5) 
	    	x1 = x1 + ((x1 * x1) v 13) 
	    	z = x0  
			for r from 0 to 63 
	    	z = (z >>1) + (z << 1) + z + x1  
			%Output m/2 bits 
	    	Lower half of z */
		int aux1, aux2;
		int x0  = Integer.parseInt(m1);
		int x1 = Integer.parseInt(m2);
		x0 = x0 + ((x0*x0) | 5) ;
		x1 = x1 + ((x1*x1) | 13);
		//System.out.println(Integer.toBinaryString(x0));
		int z = x0;
		for (int i = 0; i<64; i++){
			z = (Integer.rotateRight(z,1)) + (Integer.rotateLeft(z, 1)) + z + x1;

			//System.out.println(Integer.toBinaryString(z));
		}


		z = z & ((1 << 16)-1);
		return z;
	}

	public int Akari2(String m1, String m2){
		/*Initialize x0 and x1 of m-bits
		 * x0 = x0 + ((x0 * x0) v 5)
		 * x1 = x1 + ((x1 * x1) v 13)
		 * z = x0 ^ x1
		 * for r from 0 to 24
		 *		z = (z << 1) + ((z + (0x56AB0A)) >1)
		 * 		y = x1 ^ z
		 * for r from 0 to 24 
		 * 		y = (y >> 1) +  (y << 1) + y + (0x72A4FB))
		 *      z = z ^ y
		 *%Output m/2 bits
		 *Lower half of z
		 **/
		int aux1, aux2;
		int x0  = Integer.parseInt(m1);
		int x1 = Integer.parseInt(m2);
		x0 = x0 + ((x0*x0) | 5) ;
		x1 = x1 + ((x1*x1) | 13);
		int z = x0 ^ x1;
		int y = 0;
		for (int i = 0; i<24; i++){
			//Perguntar sobre  diferenca na rotacao aqui 
			z = Integer.rotateLeft(z, 1)+ (Integer.rotateRight((z + (0x56AB0A)),1));
			y = x1 ^ z;
		}
		for (int i = 0; i<24; i++){
			y = Integer.rotateRight(y, 1) + Integer.rotateLeft(y ,1) + y + (0x72A4FB);
//			System.out.println(Integer.toBinaryString(y));
//			System.out.println(Integer.toBinaryString(z));
//			Checar essa operacao, ela esta zerando todos os binarios 
			z = z ^ y;
//			System.out.println(Integer.toBinaryString(z));
		}
		z = z & ((1 << 16)-1);
		return z;
	}
	
	public long MersenneTwister(int trn){
//		x := TRN
//				x := ROT1 (x, x)
//				y := x ⊕ ROTr (x, u)
//				y := y ⊕ (ROT1 (y, s) AND b)
//				y := y ⊕ (ROT1 (y, t) AND c)
//				y := y ⊕ ROTr (y, 1)
//				z := y ⊕ ROT1 (y AND b, p)
//				u = 7Fh, s = 07h, t = 1Fh, 1 = 3FFFFh, p = 7Fh.
//				b = 9D2C568016 and c = EFC6000016.
		long x = (long) Integer.rotateRight(trn, trn);
		
//		long y = (long) (x ^ Integer.rotateRight(x,U));
//		 y = (long) (y ^ (Integer.rotateRight(y, S) & B));
		
		
			
		return 0;
	}
	

	public int shift(int k, int bits){
		return (bits >>> k) | (bits << (Integer.SIZE - k));
	}
	public int[] converteBinario(int numDec) {
		int indice = indiceBinario(numDec);
		int[] aux = new int[indice];
		int numeroDecimal = numDec; 

		if (numeroDecimal < 0) {
			System.out.println("entrada negativa no decimal convertido");
		} else {
			int i = 0;
			while(true){
				int resto;
				if (numeroDecimal <= 1) {
					aux[i++]= 1 ;
					break ;   
				}
				resto = numeroDecimal %2; 
				numeroDecimal = numeroDecimal >> 1;
				System.out.println(numeroDecimal);
				aux[i++]= resto ;
			}
		}
		System.out.println("");
		return aux;
	}	
	public int indiceBinario(int numero){
		int indice = 0 , aux = 1;
		while (aux < numero){
			aux = aux << 1;
			indice++;
		}	
		System.out.println(indice);
		return indice;
	}
	public int[] circularShiftDireita(int[] binario){

		int[] retorno =  new int[binario.length];
		for (int i = 0; i<binario.length;i++){
			if ((i+1)>= binario.length)
				retorno[i] = binario[0];
			else
				retorno[i] = binario[i+1];

		}		
		return retorno;
	}
	public int[] circularShiftEsquerda(int[] binario){

		int[] retorno =  new int[binario.length];

		for (int i = 0; i<binario.length; i++){
			if ((i-1) < 0)
				retorno[i] = binario[binario.length-1];
			else
				retorno[i] = binario[i-1];
		}		
		return retorno;
	}
	public int[] and(int[] a, int[] b){
		int[] retorno = new int[a.length];
		for(int i = 0; i<a.length; i++){

			retorno[i] = a[i] & b[i];

		}

		return retorno; 
	}
}