package nlp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class FillPhrase_1 {
	
	private static final String[] phrase = null;
	Map<String,Integer> pair = new LinkedHashMap<String, Integer>();
	Map<String,Integer> words = new LinkedHashMap<String, Integer>();
	String max = null;
	
	public FillPhrase_1 () {
		//统计两两词频  单个单词  放到HashMap 中
		countPairs();
	}
	public static void main(String[] args) {
		FillPhrase_1 fp = new FillPhrase_1();
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
				String subPhrase[] = line.split("，");  //逗号分隔的子句
				System.out.print("填词后为：");
				for(int i=0; i<subPhrase.length; i++) {
					String phrase[] = subPhrase[i].split(" ");
					if(subPhrase[i].startsWith(" ")) { //开头填词
						former = "";

						PositiveMatch pm = new PositiveMatch();
						pm.excute(subPhrase[i].substring(1));
						latter = pm.result;
						String tmp1[] = latter.split("/");
						b = tmp1[0];

						a = null;
					} else if( !subPhrase[i].startsWith(" ") && phrase.length == 1) {  //结尾填词
						PositiveMatch pm = new PositiveMatch();
						pm.excute(phrase[0]);
						former = pm.result;
						String tmp0[] = former.split("/");
						a = tmp0[tmp0.length-1];

						latter = "";
						b = null ;
					} else {
						PositiveMatch pm = new PositiveMatch();
						pm.excute(phrase[0]);
						former = pm.result;
						String tmp0[] = former.split("/");
						a = tmp0[tmp0.length-1];

						pm = new PositiveMatch();
						pm.excute(phrase[1]);
						latter = pm.result;
						String tmp1[] = latter.split("/");
						b = tmp1[0];
					}

					if(former == "") {
						fp.noFormerLatter(b);
					}
					if(latter == "") {
						fp.formerNoLatter(a);
					}
					if(former!=""  && latter!="" ) {
						fp.formerLatter(a,b);
					}
					if(i == subPhrase.length-1)
						System.out.println(former+"("+fp.max+")"+latter);
					else
						System.out.print(former+"("+fp.max+")"+latter+",");
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void noFormerLatter(String b) {
		Map<String,Integer> first = new HashMap<String, Integer>();
		for(Entry<String, Integer> entry : pair.entrySet()) {
			String str[] = entry.getKey().split(" ");
			String latter = str[1];
			if(latter.equals(b)  && !(entry.getKey().equals("，") || entry.getKey().equals("。") || entry.getKey().equals("“") || entry.getKey().equals("”")) ) {
				first.put(str[0], entry.getValue());
			}
		}
		
		double maxPro =0;
		String max = null;
		for(Entry<String, Integer> entry : first.entrySet()) {
			if(entry.getValue() > maxPro ) {
				max = entry.getKey();
				maxPro = entry.getValue();
			}
		}
		this.max = max;
	}
	private void formerNoLatter(String a) {
		Map<String,Double> next = this.searchNext(a);
		double maxPro =0;
		String max = null;
		for(Map.Entry<String, Double> entry : next.entrySet()) {
			if(entry.getValue() > maxPro && !(entry.getKey().equals("，") || entry.getKey().equals("。") || entry.getKey().equals("“") || entry.getKey().equals("”")|| entry.getKey().equals(",")) ) {
				max = entry.getKey();
				maxPro = entry.getValue();
			}
		}
		this.max = max;
	}
	private void formerLatter(String a,String b) {
		Map<String,Double> next = this.searchNext(a);
		//空格后出现b的概率
		for(Map.Entry<String, Double> entry : next.entrySet()) {
			if( entry.getKey().equals("，") || entry.getKey().equals("。") || entry.getKey().equals("“") || entry.getKey().equals("”")) {
				next.put(entry.getKey(), 0.0);
			} else {
				double probablity = this.previousNext(entry.getKey(),b)*entry.getValue();
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
		this.max = max;
		
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
	
	private  void countPairs() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("h:/my.txt"));
			String line = null;
			while( (line = reader.readLine())!=null  ) {
				String tmp[] = line.split(" ") ;
				for(int i=0; i<tmp.length; i++) {
					//记录每个词的词频
					if( !words.containsKey(tmp[i])) {
						words.put(tmp[i], new Integer(1));
					} else {
						int count = words.get(tmp[i]);
						words.put(tmp[i], new Integer(count+1));
					}
					
					//记录两个词连在一起的概率
					if(i < tmp.length-1) {
						String former = tmp[i];
						String latter =tmp[i+1];
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

}
