package HMM;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MTtest {
	public static void main(String[] args) throws IOException,
			ClassNotFoundException {
		int correct = 0; // 标注正确的数量
		int sum = 0; // 标注的总数量
		double p = .0; // 正确率
		long bTrain, eTrain, bTest, eTest; // 训练开始时间
		Date dateBTrain = new Date();
		bTrain = dateBTrain.getTime();

		MTtrain mtTrain = new MTtrain();
		mtTrain.train();
		MTtest testMT = new MTtest();

		Date dateETrain = new Date(); // 训练结束时间
		eTrain = dateETrain.getTime(); // 训练结束即是测试开始
		bTest = eTrain;
		File testSum = new File("E:\\test_sum\\test1"); // 测试语料,修改后面的路径，手动变换测试集
		if (!testSum.isDirectory()) {
			System.out.println("不是文件夹");
			return;
		}
		System.out.println(testSum.getPath());

		for (int i = 0; i < testSum.listFiles().length; i++) {
			FileReader rtest = new FileReader(testSum.listFiles()[i]);// 每一个要测试的文件
			BufferedReader bRT = new BufferedReader(rtest);
			// System.out.println(testSum.listFiles()[i].getName());
			File answerFile = new File("E:\\pos\\"
					+ testSum.listFiles()[i].getName());
			// 测试文件对应的结果的集合
			File resultOutput = new File("E:\\result\\test1\\"
					+ "21009246_"
					+ testSum.listFiles()[i].getName().substring(0,
							testSum.listFiles()[i].getName().indexOf("."))
					+ ".txt");
			// 获得对应的文件名，建立输出结果文件
			if (resultOutput.exists()) {
				resultOutput.delete();
			}
			resultOutput.createNewFile();// 有该文件就删除，然后重新建立

			FileWriter wr = new FileWriter(resultOutput);
			BufferedWriter writeResult = new BufferedWriter(wr);

			FileReader ra = new FileReader(answerFile);
			BufferedReader bRA = new BufferedReader(ra);

			String tRow = "";
			String aSentence = "";

			tRow = bRT.readLine();
			aSentence = bRA.readLine();
			while (tRow != null) {
				ArrayList<String> result = new ArrayList<String>();// 标注结果
				ArrayList<String> answer = new ArrayList<String>();// 正确答案
				ArrayList<String> wordList = new ArrayList<String>();// 要测试的词的集合（每一句）

				testMT.vertebi(tRow, mtTrain.wordPosP, mtTrain.wordPosAr,
						mtTrain.posTrans, mtTrain.posSet, result, wordList);// 进行vertebi标注，得到结果集

				for (int wi = 0; wi < wordList.size(); wi++)
				// 由vertebi算法，wordlist和rssult一定是一样大的，异常情况由vertebi抛出
				{
					writeResult.write(wordList.get(wi));
					writeResult.write("/");
					writeResult.write(result.get(wi));
					writeResult.write(" "); // 写结果文件
				}

				int ri = 0, rj = 1;
				StringBuilder pos = new StringBuilder();
				for (ri = 0; ri < aSentence.length(); ri++) // 得到词性的结果集
				{

					if ((!aSentence.substring(ri, rj).equals(" "))) {
						pos.append(aSentence.substring(ri, rj));
					} else {
						if (pos.length() != 0) {
							answer.add(pos.toString()); // 取出词性
							pos.setLength(0);
						}
					}
					rj++;
				}
				// System.out.println(answer);
				// System.out.println(result);
				if (answer.size() == result.size()) {
					for (int j = 0; j < answer.size(); j++) // 得到的结果与正确答案比较
					{
						sum++;
						if (answer.get(j).equals(result.get(j))) {
							correct++;
						}
					}
				} else {
					System.out.println(testSum.listFiles()[i].getName());
					return;
				}

				tRow = bRT.readLine();
				aSentence = bRA.readLine();
				writeResult.newLine(); // 一句处理完了，换行，写下一句
			}

			writeResult.close();
			wr.close();
			bRT.close();
			rtest.close();
		}

		Date dateETest = new Date();
		eTest = dateETest.getTime();
		p = correct / (double) sum;
		System.out.println(sum);
		System.out.println(correct);
		System.out.println(p);
		System.out.println("训练时间" + (eTrain - bTrain));
		System.out.println("测试时间" + (eTest - bTest));
	}

	public void vertebi(String sentence,
			HashMap<String, HashMap<String, Double>> vWordPosP,
			HashMap<String, ArrayList<String>> vWordPosAr,
			HashMap<String, Double> vPosTrans, ArrayList<String> vPosSet,
			ArrayList<String> result, ArrayList<String> wordList) {
		/*
		 * 1）参数依次为：需要进行标注的句子，每个词的发射概率信息，每个词关于词性的索引，
		 * 转移概率信息，所有词性的集合,作为结果输出的list类型的result，作为得到的要 标注的词的集合wordList
		 * 2）函数的功能，通过已知的发射概率和转移概率，进行vertebi标注，得到磁性的集合
		 */

		int ci = 0, cj = 1;
		StringBuilder word = new StringBuilder();

		for (ci = 0; ci < sentence.length(); ci++) {

			if ((!sentence.substring(ci, cj).equals(" "))) {
				word.append(sentence.substring(ci, cj));
			} else {
				if (word.length() != 0) {
					wordList.add(word.toString()); // 取出词，把词存入List便于计算
					word.setLength(0);
				}
			}
			cj++;
		}
		// System.out.println(wordList);

		for (int i = 0; i < wordList.size(); i++) { // 对于每一个词
			ArrayList<String> posAr = new ArrayList<String>();// 可能的词性的集合
			HashMap<Double, String> compareP = new HashMap<Double, String>();
			double maxP = .0; // 对大的概率，一辩
			String tranPre = ""; // 根据前面的格式，转移开率对应的串的前缀
			String maxPos = ""; // 存储最大可能的词性

			if (result.size() != 0) {
				tranPre = result.get(result.size() - 1) + "-"; // 得到结果集里面的最后一个
			} else {
				tranPre = 0 + "-";
			}

			if (vWordPosAr.get(wordList.get(i)) != null) // 在词表里面有，正常的情况
			{
				posAr = vWordPosAr.get(wordList.get(i));
				for (int j = 0; j < posAr.size(); j++) // 一边遍历一边得到最大概率
				{
					double ini = vWordPosP.get(wordList.get(i)).get(
							posAr.get(j));
					// 发射概率
					String trans = tranPre + posAr.get(j); // 转移概率的字符串
					double tra = .0;
					double sum = .0;

					if (vPosTrans.get(trans) != null) {
						tra = vPosTrans.get(trans);
					} else {
						tra = 1 / 100000;
						System.out.println(trans);
					}

					sum = ini * tra;
					compareP.put(sum, posAr.get(j)); // 反过来存是为了通过最大的概率找到对应的词性，
														// 如果相等，按照hashmap的性质，就是后进入的
					if (sum > maxP) // 每一次都比，这样找出最大的概率
					{
						maxP = sum;
					}
				}
			}/*
			 * else { compareP.put(1.0, "n"); maxP =1.0; }
			 */

			else // 未登录词处理
			{
				posAr = vPosSet; // 词表里面没有，所有词性都算
				for (int j = 0; j < posAr.size(); j++) {
					String trans = tranPre + posAr.get(j); // 转移概率的字符串
					double tra = .0;
					if (vPosTrans.get(trans) != null) {
						tra = vPosTrans.get(trans);
					} else {
						tra = 0;
					}
					compareP.put(tra, posAr.get(j));
					if (tra > maxP) // 每一次都比，这样找出最大的概率
					{
						maxP = tra;
					}
				}
			}

			maxPos = compareP.get(maxP);
			result.add(maxPos);

		}// for循环结束，完成对每一个词的遍历
			// System.out.println(result);
	}

}