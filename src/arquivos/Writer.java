package arquivos;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Writer {
	public Writer(){
	}
	public static void main(String [] args) throws Exception{
		String linha = "";
		boolean exit = false;
		do{		
        Scanner in = new Scanner(System.in);
        System.out.println("Escreva algo: ");
        linha = in.nextLine();
        if (linha.equals("exit"))
        	exit = !exit;
        Writer w = new Writer();
        w.writer("C:\\Desktop", linha);
		} while (!exit);
	}
	public static  void writer(String path, String linha) throws IOException {
        BufferedWriter buffWrite = new BufferedWriter(new FileWriter(path));
        
        buffWrite.write(linha);
        buffWrite.close();
    }
}
