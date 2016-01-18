package sentiment;

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

public class WordToVector {
	
	public static ArrayList<String> wordVec;
	public static ArrayList<String> possitiveVec;
	public static ArrayList<String> negativeVec;
	
	public void launch() {
		wordVec = new ArrayList<String>();
		
		readWordVec();
		
		//testSet();
	}
	
	/**
	 * 读入词向量   +1文件的向量 和 -1文件的向量
	 */
	private void readWordVec() {
		try {
			BufferedReader input = new BufferedReader(new FileReader("data\\feature"));
			String line = null;
			while( (line=input.readLine()) != null ) {
				wordVec.add(line);
			
			}
			input.close();

			System.out.println("词的个数："+wordVec.size());
			readFile("data\\newPositive.txt","d:\\positiveVec");
			readFile("data\\newNegative.txt","d:\\negativeVec");
			mergeFile("d:\\positiveVec","d:\\negativeVec","d:\\trainsvm");
			
			readFile("data\\newTest.txt","d:\\testsvm");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void mergeFile(String path1, String path2,String name) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(path1));
		BufferedWriter writer = new BufferedWriter(new FileWriter(name));
		String line = null;
		
		while( (line=reader.readLine())!=null) {
			writer.write(line);
			writer.newLine();
		}
		//writer.flush();
		reader.close();
		
		reader = new BufferedReader(new FileReader(path2));
		while( (line=reader.readLine())!=null) {
			writer.write(line);
			writer.newLine();
		}
		writer.flush();
		writer.close();
		reader.close();
		
		
	}
	/**
	 * 文件中词出现的个数
	 * @param file
	 * 			分词的文件
	 * @return
	 * 			词频向量
	 */
	private void readFile(String in,String out) {
		try {
			BufferedReader input = new BufferedReader(new FileReader(in));
			BufferedWriter output = new BufferedWriter(new FileWriter(out));
			String line = null;
			int countLine = 0;
			while( (line=input.readLine()) != null ) {
				countLine ++;
				
				HashMap<String,Integer> wordFreq = new HashMap<String, Integer>();
				
				String tmp[] = line.split(" ");
				for(String s : tmp) {
					if( !wordFreq.containsKey(s)) {
						wordFreq.put(s, new Integer(1));
					} else {
						int count = wordFreq.get(s) + 1;
						wordFreq.put(s, new Integer(count));
					}
				}
				
				if(out.contains("positiveVec")) {
					output.write("+1 ");
				} else if(out.contains("negativeVec")) {
					output.write("-1 ");
				} else {
					if(countLine <= 2000)
						output.write("+1 ");
					else
						output.write("-1 ");
				}
				
				for(Map.Entry<String, Integer> entry : wordFreq.entrySet()) {
					if( wordVec.contains( entry.getKey() )) {
						int index = wordVec.indexOf(entry.getKey()) + 1;
//						System.out.print("index:"+index+"  ");
						output.write(String.valueOf(index));
						output.write(":");
						output.write(String.valueOf(entry.getValue()) +" ");
//						System.out.println(entry.getValue());
					}
				}
				output.newLine();
//				if( countLine % 100 == 0)
//					System.out.println( countLine );
			}
			
			output.flush();
			input.close();
			output.close();
			System.out.println(in+" has been done!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	private void testSet() {
		String testFileList = "test2.list";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					testFileList));
			BufferedWriter writer = new BufferedWriter(new FileWriter("data\\testcorpus"));
			
			String line = null;
			// 读列表
			while ((line = reader.readLine()) != null) {
				double positive = 1;
				double negative = 1;
				// 读文件
				String testFile = "D:\\我的酷盘\\PosGra\\Course\\NLP\\project\\project6\\review_sentiment.v1\\test2\\"
						+ line;
				// System.out.println(line);
				BufferedReader readReview = new BufferedReader(
						new InputStreamReader(new FileInputStream(testFile),
								"gbk"));
				String lineReview = null;
				String review = "";
				while ((lineReview = readReview.readLine()) != null) {
					review += lineReview;
				}
				readReview.close();
				
				review = kick(review);
				ArrayList<String> words = wordDecomposition(review);
				
				for(int i=0;i<words.size();i++) {
					writer.write(words.get(i)+" ");
				}
				writer.newLine();
			}
			writer.flush();
			reader.close();
			writer.close();
		} catch (FileNotFoundException e) {
			System.out.println("找不到文件");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 删掉没用的单词
	 * @return
	 */
	private String kick(String review) {
		return review.replaceAll("[[^\\w\\s]&&[^\\u4e00-\\u9fa5]]", "");
	}
	
	/**
	 * 利用开源工具进行分词
	 * @param str
	 * @return
	 */
	public static ArrayList<String> wordDecomposition(String str) {
		ArrayList<String> words = new ArrayList<String>();

		Analyzer analyzer = new PaodingAnalyzer();
		StringReader reader = new StringReader(str);
		TokenStream ts = analyzer.tokenStream(str, reader);
		Token t;
		try {
			t = ts.next();
			while (t != null) {
				words.add(t.termText());
				// System.out.print(t.termText()+"  ");
				t = ts.next();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return words;
	}
	public static void main(String[] args) {
		WordToVector wtv = new WordToVector();
		wtv.launch();
	}
}
