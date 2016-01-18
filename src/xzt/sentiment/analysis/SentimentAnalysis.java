package xzt.sentiment.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.paoding.analysis.analyzer.PaodingAnalyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

public class SentimentAnalysis {

	public static Map<String,ArrayList<Integer>> sentiment 
	= new HashMap<String, ArrayList<Integer>>();
	public static int totalCount = 0;
	public static int totalPositive = 0;
	public static int totalNegative = 0;
	public static ArrayList<String> prediction = new ArrayList<String>();

	public void launchAnalysis() {
//		System.out.println("train list");
//		trainSentiment();

		System.out.println("test list");
		predictSentiment();

		write(prediction,"max1","\n");
	}

	private static void write(ArrayList<String> array,String name,String token) {
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter("e:/"+name+".txt"));
			for(String s : array) {
				bw.write(s+token);
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static ArrayList<String> wordDecomposition(String str)  {
		ArrayList<String> words = new ArrayList<String>();
		
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
		return words;
		
	}
	private void predictSentiment() {
		String testFileList = "test2.list" ;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(testFileList));
			String line = null;
			//读列表
			while( (line=reader.readLine()) != null ) {
				double positive = 1;
				double negative = 1;
				//读文件
				String testFile = "D:\\我的酷盘\\PosGra\\Course\\NLP\\project\\project6\\review_sentiment.v1\\test2\\" + line;
//				System.out.println(line);
				BufferedReader readReview = new BufferedReader(new InputStreamReader(new FileInputStream(
						testFile), "gbk"));
				String lineReview = null;
				String review = "";
				while( (lineReview=readReview.readLine()) != null ) {
					review += lineReview;
				}
				readReview.close();

//				FMM fmm = new FMM();
//				fmm.launchFMM(review);
				ArrayList<String> words = wordDecomposition(review);
				
				for(int i=0;i< words.size();i++ ) {
					if(sentiment.containsKey(words.get(i))) {
						ArrayList<Integer> freq = sentiment.get(words.get(i));
						if( freq.get(0) == 0) {
							positive *= 1.0 / (totalPositive + 1);
						} else {
							positive *=  1.0 * freq.get(0) / totalPositive;
						}
						 
						if( freq.get(1) == 0) {
							negative *= 1.0 / (totalNegative + 1);
						} else {
							negative *= 1.0 * freq.get(1) / totalNegative;
						}
					} 
				}
				prediction.add(positive > negative ? "+1" : "-1");

			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("找不到文件");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 训练数据
	 */
	@SuppressWarnings("unused")
	private void trainSentiment() {
		String postiveFileList = "positiveReview.txt" ;
		String negativeFileList = "negativeReview.txt";
		String positiveReview = "";
		String negativeReview = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(postiveFileList));
			String line = null;
			//读列表
			while( (line=reader.readLine()) != null ) {
				positiveReview += line;
			}
			reader.close();
			reader = new BufferedReader(new FileReader(negativeFileList));
			while( (line=reader.readLine()) != null ) {
				negativeReview += line;
			}
			reader.close();

			//统计 positive
			FMM fmm = new FMM();
			fmm.launchFMM(positiveReview);
			ArrayList<String> words = fmm.token;
			write(words,"positive"," ");
			
			totalCount += words.size();

			for(int i=0;i< words.size();i++ ) {
				if(!sentiment.containsKey(words.get(i))) {
					ArrayList<Integer> freq = new ArrayList<Integer>();
					freq.add(1);
					freq.add(0);
					totalPositive ++;
					sentiment.put(words.get(i), freq);
				} else {
					ArrayList<Integer> freq = sentiment.get(words.get(i));
					int positive = freq.get(0) + 1 ;
					freq.set(0, positive);
					totalPositive ++;
				}
			}
			System.out.println("positiveReview analysing..");
			//negative
			fmm = new FMM();
			fmm.launchFMM(negativeReview);
			words = fmm.token;
			write(words,"negative"," ");
			
			totalCount += words.size();

			for(int i=0;i< words.size();i++ ) {
				if(!sentiment.containsKey(words.get(i))) {
					ArrayList<Integer> freq = new ArrayList<Integer>();
					freq.add(0);
					freq.add(1);
					totalNegative ++;
					sentiment.put(words.get(i), freq);
				} else {
					ArrayList<Integer> freq = sentiment.get(words.get(i));
					int negative = freq.get(1) + 1;
					freq.set(1, negative);
					totalNegative ++;
				}
			}
			System.out.println("negativeReview analysing..");
		} catch (FileNotFoundException e) {
			System.out.println("找不到文件");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new SentimentAnalysis().launchAnalysis();
	}
}
