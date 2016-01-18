package nlp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class FillPhrase {
	
	Map<String,Integer> pair = new LinkedHashMap<String, Integer>();
	Map<String,Integer> words = new LinkedHashMap<String, Integer>();
	String max = null;
	
	public FillPhrase () {
		//统计两两词频  单个单词  放到HashMap 中
		countPairs();
	}
	public static void main(String[] args) {
		FillPhrase fp = new FillPhrase();
		//A __ B
		String a = "";
		String b= "";
		String former = "";
		String latter = "";
		
		//读文件  分词
		try {
			BufferedReader reader = new BufferedReader(new FileReader("phase.txt"));
			String line = null;
			
			while((line = reader.readLine()) != null) {
				String phase[] = line.split(" ");
				PositiveMatch pm = new PositiveMatch();
				pm.excute(phase[0]);
				former = pm.result;
				String tmp0[] = pm.result.split("/");
				a = tmp0[tmp0.length-1];

				pm = new PositiveMatch();
				pm.excute(phase[1]);
				latter = pm.result;
				String tmp1[] = pm.result.split("/");
				b = tmp1[0];
				System.out.println("原句为："+former+"___"+latter);
				Map<String,Double> next = fp.searchNext(a);

				//空格后出现b的概率
				for(Map.Entry<String, Double> entry : next.entrySet()) {
					if( entry.getKey().equals("，") || entry.getKey().equals("。") || entry.getKey().equals("“")) {
						next.put(entry.getKey(), 0.0);
					} else {
						double probablity = fp.previousNext(entry.getKey(),b)*entry.getValue();
						next.put(entry.getKey(), probablity);
					}
				}

				//返回最大概率的
				double maxPro =0;
				String max = null;
				for(Map.Entry<String, Double> entry : next.entrySet()) {
					if(entry.getValue() > maxPro) {
						max = entry.getKey();
						maxPro = entry.getValue();
					}
				}
				fp.max = max;
				System.out.println("填词后为："+former+max+latter);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private double previousNext(String key, String b) {
		if(!pair.containsKey(key+" "+b)) {
			return 1.0/pair.size();
		} else {
			return (pair.get(key+" "+b)+1)*1.0/(words.get(key)+1);
		}
	}
	
	//p(wi | wi-1)
	private  Map<String,Double> searchNext(String str) {
		Map<String,Double> wordPro = new LinkedHashMap<String, Double>();
		
		for(Map.Entry<String, Integer> entry : pair.entrySet()) {
			if(entry.getKey().startsWith(str)) {
				String tmp[] = entry.getKey().split(" ");
				//平滑
				wordPro.put(tmp[1], (entry.getValue()+1) *1.0 / (words.get(str)+1));
			}
		}
		return wordPro;
	}
	
	//记录某个单词出现的总次数
	private double countSum(String str) {
		// TODO Auto-generated method stub
		return 0;
	}
	private  void countPairs() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("199801.txt"));
			String line = null;
			while( (line = reader.readLine())!=null  ) {
				String tmp[] = line.split("  ") ;
				//第一个不要
				for(int i=1; i<tmp.length; i++) {
					//记录每个词的词频
					if( !words.containsKey(tmp[i])) {
						words.put(kick(tmp[i]), new Integer(1));
					} else {
						int count = words.get(tmp[i]);
						words.put(kick(tmp[i]), new Integer(count+1));
					}
					
					//记录两个词连在一起的概率
					if(i < tmp.length-1) {
						String former = kick(tmp[i]);
						String latter = kick(tmp[i+1]);
						String key = former+" "+latter;
						if(!pair.containsKey(key)){
							pair.put(key, new Integer(1));
						} else {
							int count = pair.get(key);
							pair.put(key, new Integer(count+1));
						}
					}
				}
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	private  String kick(String word) {
		return word.substring(0,word.indexOf("/"));
	}
}
