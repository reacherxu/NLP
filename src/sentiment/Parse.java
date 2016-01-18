package sentiment;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

public class Parse {
	static ArrayList<String> words = new ArrayList<String>();
	static ArrayList<Integer> num = new ArrayList<Integer>();

	public static void main(String[] args) throws UnsupportedEncodingException,
			FileNotFoundException {
		getResult();
	}

	/*
	 * 结果转换
	 */
	public static void getResult() throws FileNotFoundException {
		Scanner input = new Scanner(new File("data\\maxpredict"));
		PrintWriter output = new PrintWriter("data\\maxresult.txt");
		double[] result = new double[4000];
		String line;
		int count = 0;
		line = input.next();
		double temp = Double.parseDouble(line);
		result[count++] = temp;
		while (input.hasNext()) {
			line = input.next();
			temp = Double.parseDouble(line);
			int i = 0;
			for (i = 0; i < count; i++)
				if (result[i] > temp)
					break;

			if (i == count - 1)
				result[count] = temp;
			else {
				for (int j = count; j > i; j--)
					result[j] = result[j - 1];
				result[i] = temp;
			}
			count++;
		}

		Scanner input2 = new Scanner(new File("data\\maxpredict"));
		String line2;
		while (input2.hasNext()) {
			line2 = input2.next();
			temp = Double.parseDouble(line2);
			int re;
			if (temp > result[2000])
				re = 1;
			else
				re = -1;
			if (re == 1)
				output.write("+" + re + "\n");
			else
				output.write(re + "\n");
		}
		output.close();
	}
}
