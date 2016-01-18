package sentiment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class TestXzt {

	public static LinkedHashMap<String,Integer> words = new LinkedHashMap<String,Integer>();

	public static void main(String[] args) throws Exception {
//		String path1="d:/positive vector";
//		String path2="d:/negative vector";
//		mergeFile(path1,path2,"d:/trainWords");
		
//		wordFreq("data/newPositive.txt");
//		wordFreq("data/newNegative.txt");
		
		String path1="d:/positiveVec";
		String path2="d:/negativeVec";
		mergeFile(path1,path2,"d:/trainSVM");
	}
	
	private static void wordFreq(String file) {
		try {
			BufferedReader input = new BufferedReader(new FileReader(file));
			BufferedWriter output = new BufferedWriter(new FileWriter("data\\featureVector.txt"));
			String line = null;
			
			while( (line=input.readLine())!=null) {
				String tmp[] = line.split(" ");
				for(String word : tmp) {
					if( !words.containsKey(word)) {
						words.put(word, new Integer(1));
					} else {
						int count = words.get(word) + 1;
						words.put(word, new Integer(count));
					}
				}
			} 
			int count = 0;
			for(Map.Entry<String,Integer> entry : words.entrySet()) {
				if(entry.getValue() > 1) {
					output.write(entry.getKey());
					output.newLine();
					count++;
				}
			}
			output.flush();
			output.close();
			System.out.println("wordCount:"+count);
			input.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
		writer.flush();
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

	private static String kick(String review) {
		return review.replaceAll("[[^\\w\\s]&&[^\\u4e00-\\u9fa5]]", "");
	}


}
