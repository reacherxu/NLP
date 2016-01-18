package nlp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/*
 * Proj. 2 实现一个基于词典与规则的汉语自动分词系统。
 */
class ReadFileToVector {
	// 从文件中读取一行内容，把读取的内容放入vector中返回
	public static Vector<String> ReadFile(String str) {
		String word = null;
		Vector<String> vectors = new Vector<String>();
//		int i = 0;
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(str),"UTF-8"));
			//BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(str)));
			while ((word = input.readLine()) != null) {
				String[] tmp = word.split(",");
				
				vectors.add(tmp[0]);
//				System.out.println(vectors.get(i++));
			}
		//	System.out.println("加载字典单词总数是:" + vectors.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vectors;

	}
}

public class PositiveMatch {
	String result = "";
	
	//Description：最大正向匹配算法
	public void FMM(Vector<String> dictionary,String str) {
		int maxlen = 5;
		int i = 0, j = 0; //i,j分别标记字符串开始和结束的位置
//		System.out.println("str.length():"+str.length());
//		System.out.print("最大正向匹配算法的结果：");
		for( i = 0 ; i<str.length();) {
			if(str.length() - i == 1) {
				result += str.substring(i);
//				System.out.println(str.substring(i));
				break;  //最后一个的时候直接跳出循环
			} else if( i + maxlen > str.length()) { //以防数组越界
				maxlen = str.length() - i;
			}
			
			j = i + maxlen ;
 			String key = str.substring(i,j);
			if(dictionary.contains(key)){ 
				result += key+"/";
//				System.out.print(key+"/");
				i = i + key.length();
				continue;
			} else {
				while(key.length() > 1) {
					j--;
					key = str.substring(i,j);
					if(dictionary.contains(key)){ 
						result += key+"/";
//						System.out.print(key+"/");
						i = i + key.length();
						break;
					} 
				}
			}
		}
	}
	
	//Description：最大反向匹配算法
	public void RMM(Vector<String> dictionary,String str) {
		int maxlen = 5;
		int i , j = str.length() ; //i,j分别标记字符串开始和结束的位置
		for( i = str.length()-maxlen ; i >= 0 ; ) {
			if( i == 0 ) {
				System.out.print(str.substring(0,i)+"/");
				break;
			} else if(  i < maxlen) {//防止数组越界
				maxlen = i;
			}
			
			i = j - maxlen ;
			String key = str.substring(i,j);
			if(dictionary.contains(key)) {
				System.out.print("/"+key);
				i = i - key.length();
				continue;
			} else {
				while( key.length() > 1) {
					i ++;
					key = str.substring(i,j);
					if(dictionary.contains(key)) {
						System.out.print("/"+key);
						j = j - key.length();
						break;
					} 
				}
			}
		}
	}
	
	public void excute(String str) {
		Vector<String> dictionary = new Vector<String>();

		dictionary = ReadFileToVector.ReadFile("D:\\我的酷盘\\PosGra\\Course\\NLP\\project\\project2\\dic2.txt");// 从文件中加载词典，结果放到vector向量中

		FMM(dictionary, str);
	}
	public static void main(String[] args) throws IOException {
		PositiveMatch pm = new PositiveMatch();
		pm.excute("阿姨说昏暗的角落不适宜学习");
		System.out.println(pm.result);
		//System.out.println(pm.);
		//String str = "阿姨说油腻的东西最好少吃";
		//String str = "阿姨说昏暗的角落不适宜学习";  
		//String str = "其实想生活中增添一点浪漫和懒散的元素而不是今天知道明天要干什么这种看似很励志实则很悲哀的生活方式么";    //歧义字段
		//String str = "宝宝说一寸光阴一寸金寸金难买寸光阴时间有多么珍贵不用我多说大家都非常清楚光知道时间的珍贵是不够的重要的是我们如何合理的安排自己的时间让每一分每一秒都过得有价值";
		/*String str = "阿姨说_角落_学习";
		Vector<String> dictionary = new Vector<String>();

		dictionary = ReadFileToVector.ReadFile("D:\\我的酷盘\\PosGra\\Course\\NLP\\project\\project2\\dic2.txt");// 从文件中加载词典，结果放到vector向量中

		PositiveMatch pm = new PositiveMatch();
		
		pm.FMM(dictionary, str);
		System.out.print("\n最大反向匹配算法的结果：");*/
		//pm.RMM(dictionary, str);
	}

}
