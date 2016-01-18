package xzt.test;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import net.paoding.analysis.analyzer.PaodingAnalyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
//测试"庖丁解牛"中文分词器的分词效果     
public class Test { 
	
	public static String text = "";
	public static ArrayList<String> words = new ArrayList<String>();
	public static void wordDecomposition(String str)  {
		Analyzer analyzer = new PaodingAnalyzer(); 
		StringReader reader = new StringReader(str); 
		TokenStream ts = analyzer.tokenStream(str, reader); 
		Token t;
		try {
			t = ts.next();
			while (t != null) { 
				words.add(t.termText());
//				System.out.print(t.termText()+"  "); 
				t = ts.next(); 
			} 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public static void main(String[] args) throws Exception { 
//		String str = "小路由器上网模式将可能导致出校访问速度慢";
		
		read();
		wordDecomposition(text);
		write(words);
//		wordDecomposition(str);
	}

	private static void write(ArrayList<String> words2) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("e:/n1.txt"));
			for(String s : words2) {
				bw.write(s+" ");
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("unused")
	private static void write(String str) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("e:/negativeTemp.txt"));
			bw.write(str);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void read() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("e:/negativeTemp.txt"));
			String line = null;
			while(reader.ready()) {
				line = reader.readLine();
			}
			reader.close();
			System.out.println(line.length());
			text = line;
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 
	
	/*private static String kickPunctuation(String str) {
		String phrase = "";
		for(int i=0; i< str.length();i++) {
			if(str.substring(i,i+1).matches("[\\u4e00-\\u9fa5]")){
				phrase += str.substring(i,i+1);
			}
			if(i%10000==0 )
				System.out.println(i/10000  );
		}
		return phrase;
	}*/
} 