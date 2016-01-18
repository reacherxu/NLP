package xzt.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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
			System.out.println("加载字典单词总数是:" + vectors.size());
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
	
	public FMM(String str) {
		
		str = kickPunctuation(str);
//		System.out.println(str);
		int maxlen = 11;
		Vector<String> dictionary = new Vector<String>();

		dictionary = ReadFileToVector.ReadFile("D:\\dic2.txt");// 从文件中加载词典，结果放到vector向量中

		int i = 0, j = 0;
		System.out.println("" + str.length());
		for (i = 0; i < str.length();) {
			if (str.length() - i == 1) {
				str.substring(i);
				System.out.print(str.substring(i) + "/");
				break;
			} else if (str.length() - i < maxlen) {
				maxlen = str.length() - i;
			}

			j = i + maxlen - 1;
			String key = str.substring(i, j);
			if (dictionary.contains(key)) {
				System.out.print(key + "/");
				i = i + key.length();
				continue;
			}
			while (key.length() > 1) {
				j--;
				key = str.substring(i, j);
				if (dictionary.contains(key)) {
					System.out.print(key + "/");
					i = i + key.length();
					break;
				}

				else if (key.length() == 1) {
					System.out.print(key + "  ");
					i = i + key.length();
					break;
				}
			}

		}
	}
	private String kickPunctuation(String str) {
		String phrase = "";
		for(int i=0; i< str.length();i++) {
			if(!str.substring(i,i+1).equals(":") &&
			   !str.substring(i,i+1).equals(";") &&
			   !str.substring(i,i+1).equals("！") && 
			   !str.substring(i,i+1).equals("。") && 
			   !str.substring(i,i+1).equals("，") &&
			   !str.substring(i,i+1).equals(",") && 
			   !str.substring(i,i+1).equals(".") &&
			   !str.substring(i,i+1).equals("!") &&
			   !str.substring(i,i+1).equals("、") &&
			   !str.substring(i,i+1).equals("“") &&
			   !str.substring(i,i+1).equals("”") &&
			   !str.substring(i,i+1).equals("\"") &&
			   !str.substring(i,i+1).equals("；") ) {
				phrase += str.substring(i,i+1);
			}
		}
		return phrase;
	}
	public static void main(String[] args) throws IOException {
		String str = "宝宝:一寸光阴一寸金;寸金难买寸光阴;时间有多么珍贵，不用我多说大家都非常清楚。光知道时间的珍贵是不够的，重要的是我们如何合理的安排自己的时间。让每一分每一秒都过得有价值！我们已经进入了一个信息化的时代，大多数的工作都可以找到合适的工具帮我们完成。同样，管理时间制定计划也有非常好的工具。";
		new FMM(str);
	}
	@SuppressWarnings("unused")
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
