package ml_knn_project;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public class CSVReader {

	PrintStream fout; 
	BufferedReader fin;


	CSVReader(String filename)
	{
		try{
			fout= new PrintStream ( new FileOutputStream(filename));
		}catch(IOException fo){
			System.out.println(fo); 
		}
	}

	CSVReader(String filename, String in)
	{
		try{
			fin = new BufferedReader(new FileReader(filename));

		}catch(IOException fo){
			System.out.println(fo); 
		}
	}

	public void writer(String out)
	{

		fout.println(out);

	}
	public void writer(int out)
	{

		fout.println(out);

	}
	public void writer(char out)
	{

		fout.println(out);

	}
	public void writer(double out)
	{

		fout.println(out);

	}
	public void writer(float out)
	{

		fout.println(out);

	}
	public String reader(){
		StringBuffer sb = new StringBuffer();
		try{
			String s = fin.readLine();
			while (s != null) {
				sb.append(s);
				sb.append("\n");
				s = fin.readLine();
			}
			return sb.toString();     
		}catch(IOException e){
			System.out.println("error reading from file " + e);
			return sb.toString();
			// return "error\t";
		}
	}
}