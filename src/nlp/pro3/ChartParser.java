package nlp.pro3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import xzt.restore_word.RulePair;

public class ChartParser {
	ArrayList<RulePair> ruleList = new ArrayList<RulePair>();// 规则
	ArrayList<Edge> chart = new ArrayList<Edge>();// 非活动边
	ArrayList<Edge> activearcs = new ArrayList<Edge>();// 活动边
	Stack<Edge> agenda = new Stack<Edge>();  //待处理表
	int  printStyle = 1;
	
	public static void main(String[] args) {
		ChartParser chartParser = new ChartParser();
		String file = "E:/rule.txt"; // 存储产生式规则的文件
		chartParser.loadLexicalRule(file, "->"); // 把产生式规则load到内存中
		chartParser.parseSentenceUsingParser("e:/sample.txt"); // 解析待解析句法的文件
		System.out.println("最终的规则为：");
		chartParser.printParse();
	}

	private void printParse() {
		//记录  开始和结束
		String head=null , done=null;
		for(int i=chart.size()-1; i>0; i--) {
			Edge tmpEdge = chart.get(i);
			
			if(i == chart.size()-1){
				head = tmpEdge.head;
				done = tmpEdge.done;
			} 
			if(tmpEdge.head.equals(tmpEdge.done) || (tmpEdge.head.equals(head) && tmpEdge.done.equals(done) && i != chart.size()-1) ) 
				continue;
			//TODO  结尾判断
			tmpEdge.print(printStyle);
		}
	}

	// 把规则读进来
	private void loadLexicalRule(String file, String splitChar) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String row;
			String[] content;
			while ((row = br.readLine()) != null) {
				content = row.split(splitChar);
				// 将规则和句子中的东西均转化为小写
				ruleList.add(new RulePair(content[0].toLowerCase().trim(),content[1].toLowerCase().trim()));
			}
		} catch (FileNotFoundException e) {
			System.out.println("Exception " + e + " occured");
		} catch (IOException e) {
			System.out.println("Exception " + e + " occured");
		}
	}

	// 把已经标注好词性的文件读进来
	private void parseSentenceUsingParser(String file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			line = toLexical(line);
			System.out.println("句法分析如下：");
			parse(line);
		} catch (FileNotFoundException e) {
			System.out.println("Exception " + e + " occured");
		} catch (IOException e) {
			System.out.println("Exception " + e + " occured");
		}

	}

	private String toLexical(String line) {
		Dic_ec dictionary = new Dic_ec();
		
		// 去掉句子的标点符号
		if (line.endsWith(".") || line.endsWith("!") || line.endsWith("?")) {
			line = line.substring(0, line.length() - 1);
		}
		String[] sepWords = line.split("\\s");
		String newStr = "";
		for(int i=0 ; i<sepWords.length;i++) {
			newStr += dictionary.doSearch(sepWords[i]) +" ";
		}
			
		return newStr;
	}

	private void parse(String line) {
		
		// 存放所有的单词
		String[] words = line.split("\\s");
		ArrayList<Edge> wordStore = new ArrayList<Edge>();
		for (int i = 0; i < words.length; i++) {
			wordStore.add( new Edge(words[i],words[i],"",i+1,i+2) );
		}
		
		//重复下面的操作直到agenda为空并且输入中没有下一个词
		int i = 0;
		while( !agenda.isEmpty() || i<words.length ) {
			if(agenda.isEmpty() ) {
				agenda.push(wordStore.get(i));
				i++;
			}
			Edge e = agenda.pop();
			
			//对每一条规则
			for(int j=0 ; j<ruleList.size() ; j++) {
				String head,done,rest = "";
				if( ruleList.get(j).latter.startsWith(e.head + " ")) {
					head = ruleList.get(j).former;
					done = e.head;
					rest = ruleList.get(j).latter.substring(e.head.length()).trim();
					Edge newEdge = new Edge(head,done,rest,e.start,e.end);
					activearcs.add(newEdge);
					newEdge.print(printStyle);
				}
				
				if(ruleList.get(j).latter.equals(e.head)) {
					head = ruleList.get(j).former;
					done = ruleList.get(j).latter;
					Edge newEdge = new Edge(head,done,rest,e.start,e.end);
					agenda.push(newEdge);
					newEdge.print(printStyle);
				}
			}
			//如果没有规则的右部匹配   则放入非活动边chart中  比如说 N
//			if(addInto  == false ) {
//				chart.add(e);
//				e.print(1);
//			}
			//边扩展
			chart.add(e);
			e.print(printStyle);
			for(int j=0; j<activearcs.size() ; j++) {
				Edge tmpEdge = activearcs.get(j);
				
				String head,done,rest = "";
				if(tmpEdge.end == e.start &&  tmpEdge.rest.startsWith(e.head+" ") ) {
					head = tmpEdge.head;
					done = tmpEdge.done + " " + e.head;
					rest = tmpEdge.rest.substring(e.head.length());
					Edge newEdge = new Edge(head,done,rest,tmpEdge.start,e.end);
					activearcs.add(newEdge);
				} 
				if(tmpEdge.end == e.start && tmpEdge.rest.equals(e.head)) {
					head = tmpEdge.head;
					done = tmpEdge.done + " " + e.head;
					rest = "";
					Edge newEdge = new Edge(head,done,rest,tmpEdge.start,e.end);
					agenda.push(newEdge);
				}
			}
			//已经匹配的边  加入到chart中
		/*	for(int j=0; j<activearcs.size() ; j++) {
				Edge tmpEdge = activearcs.get(j);
				Edge newEdge = new Edge(tmpEdge.done, tmpEdge.done, "", tmpEdge.start, tmpEdge.end);
				if( !chart.contains(newEdge)) {
					chart.add(newEdge);
					///////////////////
					newEdge.print(1);
				}
			}*/
		}

	}
}
