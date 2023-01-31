package tmse.run;

import java.io.*;
import java.util.*;

import tmse.core.*;
import tmse.gui.*;

public final class Main
{
	public static void main(String[] args) throws IOException
	{
		new Master().setVisible(true);
	}

	public static void test() throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader("sample.csv"));
		String cache = reader.readLine();
		ArrayList<String> list = new ArrayList<String>();
		while(cache != null) {
			list.add(cache);
			cache = reader.readLine();
		}
		reader.close();

		int size = list.size();
		String[] split = null;
		double[] x = new double[size];
		double[] y = new double[size];
		for(int i=0; i<size; i++) {
			split = list.get(i).split("\t");
			x[i] = Double.parseDouble(split[0]);
			y[i] = Double.parseDouble(split[1]);
		}

		double[] solution = Solver.calc(size,0.2,x,y);
		for(int i=0; i<solution.length; i++) System.out.println(solution[i]);
	}
}
