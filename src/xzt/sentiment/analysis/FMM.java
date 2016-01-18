package xzt.sentiment.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Vector;


 class ReadFileToVector {
	// 从文件中读取一行内容，把读取的内容放入vector中返回
	public static Vector<String> ReadFile(String str) {
		String word = null;
		Vector<String> vectors = new Vector<String>();
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(str),"UTF-8"));
			//BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(str)));
			while ((word = input.readLine()) != null) {
				String[] tmp = word.split(",");
				
				vectors.add(tmp[0]);
			}
//			System.out.println("加载字典单词总数是:" + vectors.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vectors;

	}
}
 
 /**
	 * Description：最大正向匹配算法
	 * 
	 * @throws IOException
	 * @author：
	 */
public class FMM {
	
	ArrayList<String> token = new ArrayList<String>();
	
	public void launchFMM(String str) {
		str = kickPunctuation(str);
		write(str);
		if (str == "")
			return;
//		System.out.println(str);
		int maxlen = 8;
		Vector<String> dictionary = new Vector<String>();

		dictionary = ReadFileToVector.ReadFile("dic2.txt");// 从文件中加载词典，结果放到vector向量中

		int i = 0, j = 0;
//		System.out.println("" + str.length());
		for (i = 0; i < str.length();) {
			if (str.length() - i == 1) {
				token.add(str.substring(i));
			//	System.out.println(str.substring(i));
				break;
			} else if (str.length() - i < maxlen) {
				maxlen = str.length() - i;
			}

			j = i + maxlen - 1;
			String key = str.substring(i, j);
			if (dictionary.contains(key)) {
				token.add(key);
				i = i + key.length();
				continue;
			}
			if(!dictionary.contains(key) && key.length()==1) {
				token.add(key);
				i = i + key.length();
				continue;
			}
			while (key.length() > 1) {
				j--;
				key = str.substring(i, j);
				if (dictionary.contains(key)) {
					token.add(key);
				//	System.out.print(key);
					i = i + key.length();
					break;
				}

				else if (key.length() == 1) {
					
					token.add(key);
				//	System.out.print(key);
					i = i + key.length();
					break;
				}
			}

		}
	}
	private String kickPunctuation(String str) {
		String phrase = "";
		for(int i=0; i< str.length();i++) {
			if(str.substring(i,i+1).matches("[\\u4e00-\\u9fa5]")){
				phrase += str.substring(i,i+1);
			}
			
		}
		return phrase;
	}
	public static void main(String[] args) throws IOException {
		String str = "房間不錯，服務也好，下次去還會去那裡住,只是希望房價有優惠";
		FMM fmm = new FMM();
		fmm.launchFMM(str);
		for(String s : fmm.token) {
			System.out.println(s);
		}
	}
	private static void write(String str) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("e:/temp.txt"));
			bw.write(str);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
