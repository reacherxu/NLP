package xzt.test;

import java.util.Vector;

public class TestVector {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Vector<String> v = new Vector<String>();
		v.add("xzt");v.add("reacher");v.add("lucien");v.add("xzt");v.add("reacher");v.add("lucien");
		System.out.println("v.size():"+v.size()+"  v.capacity(): "+v.capacity());
//		v.remove(0);
//		v.remove("lucien");
//		for(Iterator<String> it = v.iterator();it.hasNext();) {
//			System.out.println(it.next());
//		}
		for(int i=0; i<v.size(); i++ ) {
			System.out.println(v.get(i));
		}
		//知道元素  获取其下标
		System.out.println(v.contains("xzt"));
		System.out.println(v.indexOf("reacher"));
		System.out.println(v.lastIndexOf("reacher"));
		//知道下标   获取其元素
		System.out.println(v.elementAt(2));
//		System.out.println(v.get(1));
	}

}
