package xzt.restore_word;

import java.util.LinkedList;

/*
 * 记录句法分析各边的关系
 */
public class Relation {
	int offspring;
	// 一个句子的分析可能有多种结果，相应的一个边的左兄弟也可能有多个
	// 因此用链表存储所有兄弟
	LinkedList<Integer> siblingList = new LinkedList<Integer>();

	public Relation(int offspring, int sibling) {
		this.offspring = offspring;
		this.siblingList.add(sibling);
	}

	public void addSibling(int _sibling) {
		this.siblingList.add(_sibling);
	}
}