package HMM;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MTpre {
	public int tranCountSum = 0; // 记录转移的总次数
	public HashMap<String, Integer> posContext = new HashMap<String, Integer>();
	// pos part of speech词性，后面也是这样
	public HashMap<String, HashMap<String, Integer>> wordInfo = new HashMap<String, HashMap<String, Integer>>();
	public HashMap<String, Integer> posSum = new HashMap<String, Integer>();

	// 存储词性出现的总次数

	public void preprocess(File corpus) throws IOException
	/*
	 * 预处理 输入为文件夹 输出为一个序列化之后的两个文件 文件transfer存储的是词性的转移次数 文件wordInfo
	 * 存储的是词以及对应出现的词性和次数
	 */
	{
		if (!corpus.isDirectory()) {
			System.out.println("corpus不是文件夹，检查格式");
			return;
		}
		for (int k = 0; k < corpus.listFiles().length; k++) {

			System.out.println(corpus.listFiles()[k].getName());
			if (!corpus.listFiles()[k].isDirectory()) {
				System.out.println(corpus.listFiles()[k].getName()
						+ "不是文件夹，检查格式");
				return;
			}
			for (int i = 0; i < corpus.listFiles()[k].listFiles().length; i++) {
				// System.out.println(corpus.listFiles()[i].getName());

				FileReader read = new FileReader(
						corpus.listFiles()[k].listFiles()[i]);
				BufferedReader re = new BufferedReader(read);
				String row = "";

				row = re.readLine(); // row用来存一行
				while (row != null) {
					String sentence = "";
					sentence = row.substring(row.indexOf(" ") + 2); // 把前面没用的东西去掉
					ArrayList<String> aPos = new ArrayList<String>();// 存储所有的词性，然后再进行遍历
					StringBuilder bWord = new StringBuilder(); // 用来得到词
					StringBuilder bPos = new StringBuilder();
					int ci = 0, cj = 1;
					int signal = 0; // 信号量，0的时候进行词的累加，1的时候进行词性的累加

					for (ci = 0; ci < sentence.length(); ci++) {

						if ((!sentence.substring(ci, cj).equals(" "))
								&& (!sentence.substring(ci, cj).equals("/"))) { // 设计信号量，0的时候追加word,1的时候追加词性
							// System.out.println(sentence.substring(ci, cj));
							if (signal == 0) {
								bWord.append(sentence.substring(ci, cj));
							}
							if (signal == 1) {
								bPos.append(sentence.substring(ci, cj));
							}
							if (signal != 0 && signal != 1) {
								System.out.println("signal格式错误");
								return;
							}
						} else if (sentence.substring(ci, cj).equals("/")) {
							signal = 1;
						} else if (sentence.substring(ci, cj).equals(" ")
								&& bWord.length() != 0 && bPos.length() != 0)// 空格，结算阶段把词和对应的词性加入wordInfo中，找不到的时候是null，nnd,老忘
						{
							signal = 0; // 将信号量置回0

							// System.out.println(bWord.toString());
							// System.out.println(bPos.toString());
							String word = bWord.toString();
							String pos = bPos.toString();

							bWord.setLength(0);
							bPos.setLength(0);

							HashMap<String, Integer> addPos = new HashMap<String, Integer>();
							int wpCount = 0; // 用来对词性出现的次数进行计数

							if (pos.length() != 0) // 把词性存到ArrayList中
							{
								aPos.add(pos);
								int cPos = 1;
								if (posSum.get(pos) != null) {
									cPos = posSum.get(pos) + 1; // 如果在词表中，计数加1，不在就添加
								}
								posSum.put(pos, cPos);
								// System.out.println(pos);
							}

							if (wordInfo.get(word) == null) // 在词的信息中，没有word这个词
							{
								addPos.put(pos, 1);
								wordInfo.put(word, addPos); // 哪一种条件下这个都不能省的
							} else {
								addPos = wordInfo.get(word);
								if (addPos.get(pos) == null) // 有word这个词但是没有对应的词性的信息
								{
									addPos.put(pos, 1);
									wordInfo.put(word, addPos);
								} else // 二者都有
								{
									addPos = wordInfo.get(word);
									wpCount = addPos.get(pos);
									wpCount++;
									addPos.put(pos, wpCount);
									wordInfo.put(word, addPos);
								}
							}

						}// 结算完毕，更新变量，aPos,wordInfo

						cj++;

					}// for循环结束，对一句话的遍历完成了

					for (int j = 0; j < aPos.size(); j++) // 求转移的次数，转移概率的预处理
					{
						// System.out.println(aPos.get(j));
						String tran = "";
						int tCount = 0;

						if (j == 0) {
							tran = j + "-" + aPos.get(j);
							tranCountSum++; // 记录总转移的次数
						} else {
							tran = aPos.get(j - 1) + "-" + aPos.get(j);
							tranCountSum++;
						}

						if (posContext.get(tran) == null) {
							posContext.put(tran, 1);
						} else {
							tCount = posContext.get(tran);
							tCount++;
							posContext.put(tran, tCount);
						}
					}

					row = re.readLine();

				}
			}// 对每组文件的每个文件夹进行遍历
		}// 最外部for循环，对每组文件进行遍历
			// System.out.println(posSum);
			// System.out.println(posContext);
			// FileOutputStream out = new FileOutputStream("E:\\MTpre");
			// ObjectOutput obOut = new ObjectOutputStream(out);
			// obOut.writeObject(wordInfo);
			// obOut.writeObject(posContext);
			// obOut.flush();

		// Iterator iteratorWord = wordInfo.keySet().iterator();//测试结果的
		// while(iteratorWord.hasNext())
		// {
		// String iWord ="";
		// iWord = (String)iteratorWord.next();
		// System.out.println(iWord);
		// HashMap<String, Integer> posInt = wordInfo.get(iWord);
		// Iterator iteratorPos = posInt.keySet().iterator();
		// while(iteratorPos.hasNext())
		// {
		// String iPos="";
		// int t=0;
		// iPos = (String)iteratorPos.next();
		// t = posInt.get(iPos);
		// System.out.print(iPos+":"+t+"    ");
		// }
		// System.out.println("");
		// }
	}
}