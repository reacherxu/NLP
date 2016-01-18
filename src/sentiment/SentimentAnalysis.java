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

public class SentimentAnalysis {

	public static Map<String, ArrayList<Integer>> sentiment = new HashMap<String, ArrayList<Integer>>();
	public static int totalCount = 0;
	public static int totalPositive = 0;
	public static int totalNegative = 0;
	public static ArrayList<String> prediction = new ArrayList<String>();
	ArrayList<String> stopwords = new ArrayList<String>();
	ArrayList<String> positive = new ArrayList<String>();
	ArrayList<String> negative = new ArrayList<String>();

	public void launchAnalysis() throws IOException {
//		System.out.println("train list");
//		trainSentiment();
		
		investigate();

		System.out.println("test list");
		predictSentiment();

		write("data\\max.txt",prediction);
	}

	/**
	 * 结果写入文件
	 */
	private static void write(String path,ArrayList<String> x) {
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(path));
			for (String s : x) {
				bw.write(s + "\n");
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

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

	/**
	 * 对test进行预测
	 */
	private void predictSentiment() {
		String testFileList = "test2.list";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					testFileList));
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

				for (int i = 0; i < words.size(); i++) {
					if( !stopwords.contains(words.get(i))) {
						if (sentiment.containsKey(words.get(i))) {
							ArrayList<Integer> freq = sentiment.get(words.get(i));
							if (freq.get(0) == 0) {
								positive *= 1.0 / (totalPositive + 1);
							} else {
								positive *= 1.0 * freq.get(0) / totalPositive;
							}

							if (freq.get(1) == 0) {
								negative *= 1.0 / (totalNegative + 1);
							} else {
								negative *= 1.0 * freq.get(1) / totalNegative;
							}
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
	private void trainSentiment() {
		String trainFileList = "train2.rlabelclass";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					trainFileList));
			BufferedWriter writerPositive = new BufferedWriter(new FileWriter(
					"e:/newPositive.txt"));
			BufferedWriter writerNegative = new BufferedWriter(new FileWriter(
					"e:/newNegative.txt"));
			String line = null;
			int count = 0;
			// 读列表
			while ((line = reader.readLine()) != null) {
				String tmp[] = line.split(" ");
				// 读文件
				String trainFile = "D:\\我的酷盘\\PosGra\\Course\\NLP\\project\\project6\\review_sentiment.v1\\train2\\"
						+ tmp[0];
				// System.out.println(tmp[0]);
				BufferedReader readReview = new BufferedReader(
						new InputStreamReader(new FileInputStream(trainFile),
								"gbk"));
				String lineReview = null;
				String review = "";
				while ((lineReview = readReview.readLine()) != null) {
					review += lineReview;
				}
				readReview.close();

				review = kick(review);
				// if(tmp[0].equals("153_wpcwoo_2005-12-31_1.0.txt")) {
				// System.out.println(review.matches("[\\w+\\s+]+"));
				// }
				if (review.matches("[\\w+\\s+]+")) {// 英文
					String temp[] = review.split("\\s+");
					for (String s : temp) {
						s = s.replaceAll("\\pP", ""); // 去掉英文标点
						if (tmp[1].equals("+1"))
							writerPositive.write(s + " ");
						else
							writerNegative.write(s + " ");
					}
					if (tmp[1].equals("+1")) {
						writerPositive.write("\n");
						writerPositive.flush();
					} else {
						writerNegative.write("\n");
						writerNegative.flush();
					}
				} else {
					String phrase = "";
					for (int i = 0; i < review.length(); i++) {
						if (review.substring(i, i + 1).matches(
								"[\\u4e00-\\u9fa5]")) {
							phrase += review.substring(i, i + 1);
						}
					}
					ArrayList<String> words = wordDecomposition(phrase);
					for (int i = 0; i < words.size(); i++) {
						if (tmp[1].equals("+1"))
							writerPositive.write(words.get(i) + " ");
						else
							writerNegative.write(words.get(i) + " ");
					}
					if (tmp[1].equals("+1")) {
						writerPositive.write("\n");
						writerPositive.flush();
					} else {
						writerNegative.write("\n");
						writerNegative.flush();
					}
				}

			} //读完了所有的文件 end while
			
			investigate();
			
			
			reader.close();
			writerPositive.close();
			writerNegative.close();

		} catch (FileNotFoundException e) {
			System.out.println("找不到文件");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 统计正例负例次数
	 * @throws IOException
	 */
	private void investigate() throws IOException {
		ArrayList<String> words = new ArrayList<String>();
		
		//停用词表
		BufferedReader reader = new BufferedReader(new FileReader("stopword.txt"));
		String line = null;
		while( (line=reader.readLine())!=null) {
			stopwords.add(line);
		}
		reader.close();
		
		reader = new BufferedReader(new FileReader("newPositive.txt"));
		while( (line=reader.readLine())!=null) {
			String tmp[] = line.split(" ");
			for(int i=0;i<tmp.length;i++) {
				words.add(tmp[i]);
			}
		}
		reader.close();
		for(int i=0;i< words.size();i++ ) {
			if( !stopwords.contains(words.get(i))) {
				if(!sentiment.containsKey(words.get(i)) ) {
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
		}
		System.out.println("+1 finished");
		reader = new BufferedReader(new FileReader("newNegative.txt"));
		while( (line=reader.readLine())!=null) {
			String tmp[] = line.split(" ");
			for(int i=0;i<tmp.length;i++) {
				words.add(tmp[i]);
			}
		}
		reader.close();
		
		totalCount = totalPositive;

		for(int i=0;i< words.size();i++ ) {
			if( !stopwords.contains(words.get(i))) {
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
		}
		
		featureExtract();
//		sentiment = kickOnce();
		System.out.println("-1 finished\n totalPositive:"+totalPositive+"\n totalNegative:"+totalNegative+"\nwordSize:"+sentiment.size());
	}

	/**
	 * 提取特征值
	 */
	private void featureExtract() {
		int countP = 0,countN = 0;
		for(Map.Entry<String, ArrayList<Integer>> entry : sentiment.entrySet()) {
			ArrayList<Integer> freq = entry.getValue();
			if(freq.get(0) > 68) {
				countP ++;
				if(countP <= 500)
					positive.add(entry.getKey());
			}
			if(freq.get(1) > 179) {
				countN ++;
				if(countN <= 500)
					negative.add(entry.getKey());
			}
		}
		System.out.println("countP"+positive.size()+"\n"+"countN"+negative.size());
		write("data\\positive vector",positive);
		write("data\\negative vector",negative);
	}

/*	private Map<String, ArrayList<Integer>> kickOnce() {
		Map<String, ArrayList<Integer>> map = new HashMap<String, ArrayList<Integer>>();
		for(Map.Entry<String, ArrayList<Integer>> entry : sentiment.entrySet()) {
			ArrayList<Integer> freq = entry.getValue();
			if(freq.get(0) + freq.get(1) >= 2) {
				map.put(entry.getKey(),entry.getValue());
			} else {
				totalPositive -= freq.get(0);
				totalNegative -= freq.get(1);
			}
		}
		return map;
	}*/

	/**
	 * 删掉没用的单词
	 * @return
	 */
	private String kick(String review) {
		return review.replaceAll("[[^\\w\\s]&&[^\\u4e00-\\u9fa5]]", "");
	}

	public static void main(String[] args) throws IOException {
		new SentimentAnalysis().launchAnalysis();
	}
}
