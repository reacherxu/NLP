package xzt.restore_word;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/*
 * Proj. 1 实现一个英语单词还原工具
 */
public class WordTranslator {


	HashMap<String, String> dictMap = new HashMap<String, String>();
	HashMap<String, String> irregularVerbMap = new HashMap<String, String>();

	public void loadMap(String DictFile, String splitChar) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(DictFile));
			String row;
			while ((row = br.readLine()) != null) {
				String[] content = row.split(splitChar);
				dictMap.put(content[0].trim(), row);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Exception " + e + " occured");
		} catch (IOException e) {
			System.out.println("Exception " + e + " occured");
		}
	}

	public void loadIrregularRestoreTable(String file, String splitChar) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String row;
			String[] content;
			while ((row = br.readLine()) != null) {
				content = row.split(splitChar);
				irregularVerbMap.put(content[0].trim(), content[1].trim());
			}
		} catch (FileNotFoundException e) {
			System.out.println("Exception " + e + " occured");
		} catch (IOException e) {
			System.out.println("Exception " + e + " occured");
		}
	}

	public String applyRestoreRule(String word) {
		String origWord = "";
		int length = word.length();
		// if(length > 1 && word.charAt(length - 1) == 's')
		if (word.endsWith("s")) {
			origWord = word.substring(0, length - 1);
			if (dictMap.containsKey(origWord)) {
				return origWord;
			}

			if (word.endsWith("es"))
				;
			{
				origWord = word.substring(0, length - 2);
				if (dictMap.containsKey(origWord)) {
					return origWord;
				}
			}

			if (word.endsWith("ies")) {
				origWord = word.substring(0, length - 3) + 'y';
				if (dictMap.containsKey(origWord)) {
					return origWord;
				}
			}
		}

		if (word.endsWith("ed")) {
			origWord = word.substring(0, length - 1);
			if (dictMap.containsKey(origWord)) {
				return origWord;
			}

			origWord = word.substring(0, length - 2);
			if (dictMap.containsKey(origWord)) {
				return origWord;
			}

			origWord = word.substring(0, length - 3);
			if (word.charAt(length - 3) == word.charAt(length - 4)
					&& dictMap.containsKey(origWord)) {
				return origWord;
			}

			if (word.endsWith("ied")) {
				origWord = word.substring(0, length - 3) + 'y';
				if (dictMap.containsKey(origWord)) {
					return origWord;
				}
			}
		}

		if (word.endsWith("ing")) {
			origWord = word.substring(0, length - 3);
			if (dictMap.containsKey(origWord)) {
				return origWord;
			}

			origWord = origWord + 'e';
			if (dictMap.containsKey(origWord)) {
				return origWord;
			}

			origWord = word.substring(0, length - 4);
			if (word.charAt(length - 4) == word.charAt(length - 5)
					&& dictMap.containsKey(origWord)) {
				return origWord;
			}

			origWord = origWord + "ie";
			if (word.endsWith("ying")) {
				return origWord;
			}
		}

		if (irregularVerbMap.containsKey(word))
			return irregularVerbMap.get(word);

		return "";
	}

	public void lookupDict() {
		System.out.println("Please input the word you want to translate:");
		Scanner scanner = new Scanner(System.in);
		String word = scanner.next();
		String meaning;
		while (!word.toLowerCase().equals("n")) {
			if (dictMap.containsKey(word)) {
				meaning = dictMap.get(word);
			} else {
				meaning = applyRestoreRule(word);
				if (!meaning.equals("")) {
					meaning = dictMap.get(meaning);
				} else {
					System.out
							.println("The word you want to look up doesn't exist in current dictionary.");
				}
			}
			System.out.println("The meaning of the word \"" + word + "\" is \""
					+ meaning + "\"");
			System.out
					.println("Please input other words you would like to look up(input \"N\" for exit):");
			word = scanner.next();
		}
	}

	public static void main(String args[]) {
		WordTranslator wt = new WordTranslator();
		String dictFile = "D:\\我的酷盘\\PosGra\\Course\\NLP\\project\\project1\\dic_ec\\dic_ec.txt";
		// String dictFile = "./data/MT-1/Transformation.txt";
		// String irregularTableFile = "./data/MT-1/irregular.txt";
		wt.loadMap(dictFile, "");
//		 wt.loadIrregularRestoreTable(irregularTableFile, "\\t");
		wt.lookupDict();
		System.out.println("System exit...");
	}
}
