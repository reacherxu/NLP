package HMM;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MTtrain {
	public HashMap<String, HashMap<String, Double>> wordPosP = new HashMap<String, HashMap<String, Double>>();
	// 存储词，词性，极其概率，这里不考虑未登录词的情况，若是考虑的话得都做平滑了
	public HashMap<String, ArrayList<String>> wordPosAr = new HashMap<String, ArrayList<String>>();
	// 存储的是一个索引，每个词，及出现的词性
	public HashMap<String, Double> posTrans = new HashMap<String, Double>();
	// 存储转移概率
	public ArrayList<String> posSet = new ArrayList<String>();

	// 存储所有的词性

	public void train() throws IOException
	/*
	 * 把预处理的结果进行处理，得到发射概率和转移概率 得到的结果输入三个公共变量
	 */
	{
		MTpre mtTrain = new MTpre();
		File corpus = new File("E:\\MT_1");
		mtTrain.preprocess(corpus); // 语料预处理

		Iterator iteratorWord = mtTrain.wordInfo.keySet().iterator();
		while (iteratorWord.hasNext()) // 对word的信息进行遍历，整合成最后的结果
		{
			String iWord = "";
			iWord = (String) iteratorWord.next();
			HashMap<String, Integer> posInt = mtTrain.wordInfo.get(iWord);

			int sum = 0; // 存储一个词，所有词性出现的总次数
			ArrayList<String> posAr = new ArrayList<String>(); // 暂存的是每个词的词性列表
			HashMap<String, Double> posP = new HashMap<String, Double>();
			// 存储词性，极其概率，即发射概率

			Iterator iteratorPos_1 = posInt.keySet().iterator();
			while (iteratorPos_1.hasNext()) { // 第一次遍历，求转移概率，存储词与词性的索引
				String iPos = "";
				int t = 0;

				iPos = (String) iteratorPos_1.next();
				t = posInt.get(iPos);
				sum = mtTrain.posSum.get(iPos);
				posAr.add(iPos);
				posP.put(iPos, t / (double) sum); // 发射概率是状态释放词w的概率除以，状态出现的总次数

			}

			wordPosP.put(iWord, posP); // 整合信息后存入hashmap
			wordPosAr.put(iWord, posAr);
		}// 对mtTrain.wordInfo循环结束

		Iterator iteratorTrans = mtTrain.posContext.keySet().iterator();
		while (iteratorTrans.hasNext()) {
			String trans = "";
			int no = 0;
			trans = (String) iteratorTrans.next();
			no = mtTrain.posContext.get(trans);
			posTrans.put(trans, (double) no / (double) mtTrain.tranCountSum);// 存储概率
		}

		Iterator iteratorPos = mtTrain.posSum.keySet().iterator(); // 遍历posSum得到词性的数组
		while (iteratorPos.hasNext()) {
			String pos = "";
			pos = (String) iteratorPos.next();
			posSet.add(pos);
		}
		// System.out.println(wordPosP);
		// System.out.print(wordPosAr);检验结果
	}
}