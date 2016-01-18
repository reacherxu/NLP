package xzt.restore_word;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Scanner;

/*
 * Proj4. 实现一个基于简单英语语法的chart句法分析器
 */
public class ChartParser {
	ArrayList<RulePair> lexicalList = new ArrayList<RulePair>();// 存储所有的产生式规则
	LinkedList<Edge> edgeList = new LinkedList<Edge>();// 存储所有的边，包括活动边和非活动边
	/*
	 * 存储句法分析中各边的关系，便于输出结果。 例如：A->BC。， B->DE。, C->FG。
	 * 则C->FG。为A->BC。的后代，B->DE。为C->FG。的兄弟(。标记当前匹配位置)
	 */
	LinkedList<Relation> relationList = new LinkedList<Relation>();

	public static void main(String args[]) {
		ChartParser chartParser = new ChartParser();
		String file = "E:/rule.txt"; // 存储产生式规则的文件
		chartParser.loadLexicalRule(file, "->"); // 把产生式规则load到内存中
		chartParser.parseSentenceUsingParser("e:/sample.txt"); // 解析待解析句法的文件
	}

	/*
	 * 把产生式load到内存并保存在一个动态数组链表中， 该数组中的每一个元素分别包含了产生式的左部和右部
	 */
	public void loadLexicalRule(String file, String splitChar) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String row;
			String[] content;
			while ((row = br.readLine()) != null) {
				content = row.split(splitChar);
				lexicalList.add(new RulePair(content[0].toLowerCase().trim(), content[1].toLowerCase().trim()));
			}
			// sortRuleByFormer();
		} catch (FileNotFoundException e) {
			System.out.println("Exception " + e + " occured");
		} catch (IOException e) {
			System.out.println("Exception " + e + " occured");
		}
	}

	// 将数组中的产生式规则按左部的字母顺序排列
	public void sortRuleByFormer() {
		boolean exchanged = true;

		while (exchanged) {
			exchanged = false;
			int j = 0;
			for (int i = lexicalList.size() - 1; i > j; --i) {
				if (lexicalList.get(i).isLessThan(lexicalList.get(i - 1))) {
					RulePair tmpRule = lexicalList.get(i);
					lexicalList.set(i, lexicalList.get(i - 1));
					lexicalList.set(i - 1, tmpRule);
					exchanged = true;
				}
			}
			++j;
		}
	}

	/*
	 * 用Chart Parsing方法进行句法分析
	 * 
	 * 基本的策略是：对于Agenda中所有的非活动边(C->C1C2。位置为p1-p2)利用以下规则进行分析，直到Agenda为空 1.
	 * 对每条X->CX1...Xn的规则，在边链表中edgeList增加一条活动边：X-> C。X1...Xn，位置为：p1-p2 2.
	 * 对edgeList中的每条活动边按以下规则进行边扩展 对每个形式为：X->X1... 。
	 * C...Xn的活动边，若它在p0-p1之间，则在edgeList中增加一条活动边：X->X1... C。...Xn，位置:p0-p2
	 * 对每个形式为： X->X1... Xn 。C的活动边，若它在p0-p1之间，则在agenda中增加一个成分：X，位置为：p0-p2
	 */
	public boolean chartParseSyntax(String orig) {
		// 去掉句子的标点符号
		if (orig.endsWith(".") || orig.endsWith("!") || orig.endsWith("?")
				|| orig.endsWith(",")) {
			orig = orig.substring(0, orig.length() - 1);
		}
		// 把句子分解成单词
		String[] words = orig.split("\\s");
//		String[] words = orig.toLowerCase().split("\\s");

		// 初始化非活动边队列
		Queue<Edge> agenda = new LinkedList<Edge>();

		// 把各个单词存入非活动边队列
		for (int i = 0; i < words.length; ++i) {
			agenda.add(new Edge(words[i], words[i], "", i, i + 1));
		}

		Edge e = null;

		while (!agenda.isEmpty()) {
			e = agenda.remove();

			/*
			 * 产生式 A -> B C D + head B => head A done F done B rest rest C D
			 * start 5 start 5 end 8 end 8
			 */

			for (int i = 0; i < lexicalList.size(); ++i) {
				if (lexicalList.get(i).latter.startsWith(e.head + " ")
						|| lexicalList.get(i).latter.equals(e.head)) {
					String tmpRest = lexicalList.get(i).latter.substring(e.head.length()).trim();
					Edge newEdge = new Edge(lexicalList.get(i).former, e.head,tmpRest, e.start, e.end);
					edgeList.add(newEdge);
					
					//TODO
					int pos = edgeList.indexOf(e);
					if (tmpRest.equals("")) {
						agenda.add(newEdge);
						// 插入关系表中，以备输出使用     两个位置上分别是 offspring,sibling
						relationList.add(new Relation(pos, -1));
					} else {
						relationList.add(new Relation(-1, pos));
					}
				}
			}

			if (!edgeList.isEmpty()) {
				int length = edgeList.size();
				/*
				 * head A + head C => head A done B done E done B C rest C D
				 * rest rest D start 5 start 9 start 5 end 9 end 12 end 12
				 */
				for (int i = 0; i < length; ++i) {
					Edge tmpEdge = edgeList.get(i);
					if (tmpEdge.end == e.start &&
						(tmpEdge.rest.equals(e.head) || tmpEdge.rest.startsWith(e.head + " "))) {

						String tmpDone = tmpEdge.done + " " + e.head;
						String tmpRest = tmpEdge.rest.substring(e.head.length()).trim();
						Edge newEdge = new Edge(tmpEdge.head, tmpDone, tmpRest,tmpEdge.start, e.end);
						edgeList.add(newEdge);
						
						//TODO
						int pos = edgeList.indexOf(e);
						if (tmpRest.equals("")) {
							agenda.add(newEdge);
							relationList.add(new Relation(pos, -1));
							if(pos != -1) {
								relationList.get(pos).addSibling(i);
								System.out.println("fdsfsd");
							}
						} else {
							relationList.add(new Relation(-1, pos));
							if(pos != -1) {
								relationList.get(pos).addSibling(i);
								System.out.println("fdsfsd");
							}
						}
					}
				}
			}
		}

		int length = edgeList.size();
		boolean parsed = false;
		for (int i = 0; i < length; ++i) {
			Edge tmpEdge = edgeList.get(i);
			if (tmpEdge.head.equals("s") && tmpEdge.rest.isEmpty()
					&& tmpEdge.start == 0 && tmpEdge.end == words.length) {
				if (false == parsed) {
					System.out.println("Sentence parsed for \"" + orig + "\"");
					parsed = true;
				}
				System.out.println("One of the possible parsing process is:");
				printedPos = 0;
				resultPrint(i, "s");
				System.out.println();
			}
		}
		return parsed;
	}

	int printedPos = 0; // 当前已经输出单词的位置，防止错误输出
	int printStyle = 1; // 输出方式：1. 使用"->" 2.使用"()"

	/*
	 * 句法分析后，输出句法分析的过程 index为当前边在关系及边链表中的索引 latter当前边的父边的右部
	 * 
	 * 基本的策略是先输出根边的产生式，再输出其最左子的产生式， 最后输出其右子树的产生式，全部输出递归进行
	 * 
	 * 存储时，每个父边只存储了其最右子边为其offspring， 其他offspring通过最右子的sibling得到
	 */
	public void resultPrint(int index, String latter) {
		// 得到当前边及其在分析中的关系
		Relation tmpR = relationList.get(index);
		Edge tmpE = edgeList.get(index);

		// 当前边不应在结果集中出现，直接返回
		if (!tmpE.rest.equals("") && !latter.endsWith(tmpE.done)
				|| ((tmpE.head.equals("s") && tmpE.start != 0))) {
			return;
		}

		// 遍历当前边的所有左兄弟
		ListIterator<Integer> iter = tmpR.siblingList.listIterator();
		while (iter.hasNext()) {
			int sibling = iter.next();
			// 左部存在且其起始位置不小于当前已输出位置
			if (sibling != -1 && edgeList.get(sibling).start >= printedPos) {
				// 输出当前边的所有左兄弟
				if (tmpE.rest.equals("")) {
					int tmp = latter.length() - tmpE.head.length();
					if (latter.endsWith(tmpE.head)) {
						resultPrint(sibling, latter.substring(0, tmp).trim());
					}
				} else {
					resultPrint(sibling, latter);
				}
			}
		}

		// 输出当前边
		if (tmpE.rest.equals("")) {
			tmpE.print(printStyle);
			// 当前边右部为终结符，记录输出位置
			if (tmpE.done.equals(tmpE.done.toLowerCase())) {
				printedPos = tmpE.end;
			}
		}

		// 输出当前边的后代
		if (tmpR.offspring != -1) {
			if (printStyle == 1) {
				resultPrint(tmpR.offspring, tmpE.done);
			} else {
				System.out.print("(");
				resultPrint(tmpR.offspring, tmpE.done);
				System.out.print(")");
			}
		}
	}

	/*
	 * 从指定文件中获取带解析的句子并按指定方式输出句法分析结果
	 */
	public void parseSentenceUsingParser(String file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));

			// 接收输出方式
			System.out.println("Please input 1 for presenting the result using \"->\" or ");
			System.out.println("input 2 for presenting the result using \" ()\": ");
			Scanner in = new Scanner(System.in);
			printStyle = in.nextInt();

			String row;
			while ((row = br.readLine()) != null) {
				// 每次解析文件中一个句子
				if (false == chartParseSyntax(row)) {
					System.out.println("Sorry! The following sentence can't be parsed: ");
					System.out.println(row);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Exception " + e + " occured");
		} catch (IOException e) {
			System.out.println("Exception " + e + " occured");
		}
	}
}
