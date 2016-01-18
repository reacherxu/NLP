package sentiment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

public class Test {
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader("data/maxresult.txt"));
//		BufferedReader br = new BufferedReader(new FileReader("d:\\max.txt"));
		String line = null;
		int count = 0;
		int rightCount = 0;
		while(br.ready()) {
			if(count <= 1999) {
				if(br.readLine().equals("+1"))
					rightCount ++;
			} else {
				if(br.readLine().equals("-1"))
					rightCount ++;
			}
			count ++;
		}
		System.out.println("rightCount:"+rightCount);
		System.out.println("precision:"+1.0 * rightCount / count);
		br.close();
	}
}
