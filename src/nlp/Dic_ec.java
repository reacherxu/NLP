package nlp;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/*
 * Proj. 1 实现一个英语单词还原工具
 */
public class Dic_ec {
	String file = "D:\\我的酷盘\\PosGra\\Course\\NLP\\project\\project1\\dic_ec\\dic_ec.txt";
	BufferedReader readWord = null;
	BufferedReader readLine = null;
	String line = null;
	String print = "";
	
	
	private  boolean locate(String word) {
		boolean flag = false;
		print = "";
				
		if(word != null ) {
			try {
				readLine = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
				while((line = readLine.readLine()) != null ) {
					String[] query = line.split("");
					if(word.equals(query[0])) {  //查询到单词
						flag = true;
						for(String s : query ) {
							//System.out.print(s+"  ");
							this.print += s+"  ";
						}
						break;
					}
				}
				/*if (flag == false) {
				//TODO   单词还原

				System.out.println("词典中无次单词！");
				}*/
			} catch (UnsupportedEncodingException  e) {
				e.printStackTrace();
			} catch ( FileNotFoundException e) {
				System.out.println("对不起 ，不存在此文件！");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flag;
		
	}
	
	public  String restore(String word) {
		if(word.endsWith("s")) { //以s结尾
			if(word.endsWith("ies")) {  //以ies结尾
				return word.substring(0,word.lastIndexOf("ies")).concat("y");
			} else if(word.endsWith("es")) { //以es结尾
				return word.substring(0,word.lastIndexOf("es"));
			}
			return word.substring(0,word.lastIndexOf("s"));
		} else if(word.endsWith("d")) {//以d结尾
			if(word.endsWith("ed")) {
				if (word.endsWith("ied")) {
					return word.substring(0,word.lastIndexOf("ied")).concat("y");
				} else if(word.charAt(word.lastIndexOf("ed")-1) == word.charAt(word.lastIndexOf("ed")-2)) {
					return  word.substring(0,word.lastIndexOf("ed")-1);
				}else {
					if( ( this.locate(word.substring(0,word.lastIndexOf("ed")))) == false) {
						return word.substring(0,word.lastIndexOf("d"));
					} else {
						return word.substring(0,word.lastIndexOf("ed"));
					}
				}
			}  
			return word.substring(0,word.lastIndexOf("d"));
		} else if(word.endsWith("ing")) {//以ing结尾
			if (word.endsWith("ying")) {
				return word.substring(0,word.lastIndexOf("ying")).concat("ie");
			} else if(word.charAt(word.lastIndexOf("ing")-1) == word.charAt(word.lastIndexOf("ing")-2)) {
				return  word.substring(0,word.lastIndexOf("ing")-1);
			} else {
				if( ( this.locate(word.substring(0,word.lastIndexOf("ing")))) == false) {
					return word.substring(0,word.lastIndexOf("ing")).concat("e");
				} else {
					return word.substring(0,word.lastIndexOf("ing"));
				}
			}
			//return word.substring(0,word.lastIndexOf("ing"));
		}
		return null;
	}
	
	public static void main(String[] args) {
		Dic_ec dic = new Dic_ec();
		
		try {
			System.out.print("input a word(press Enter to exit):");
			dic.readWord = new BufferedReader(new InputStreamReader(System.in));
			
			String word = null;
			while(!( word = dic.readWord.readLine()).matches("\\s*")) {
				//查询单词
				if( ( dic.locate(word)) == false) {
					String tmp = dic.restore(word);
					if( ( dic.locate(tmp)) == false) {
						System.out.println("词典中无次单词！");
					} else  {
						System.out.println(dic.print);
					}
				} else  {
					System.out.println(dic.print);
				}
				System.out.print("input a word(press Enter to exit):");
			}
			
			dic.readWord.close();
			dic.readLine.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				dic.readWord.close();
				dic.readLine.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
       
	}

	
	
}
