package nlp.pro3;

/*
 * 边类，用于记录chart parsing句法分析中各个活动或非活动边
 */
public class Edge {
	public String head, done, rest;
	public int start, end;


	/*
	 * head A 即 A -> B。CD 其中B产生了第5-8个终结符 done B rest C D start 5 end 9
	 */
	public Edge(String head, String done, String rest, int start, int end) {
		this.head = head;
		this.done = done;
		this.rest = rest;
		this.start = start;
		this.end = end;
	}

	// 输出当前边
	public void print(int printStyle) {
		// 以"->"输出
		if (printStyle == 1) {
			System.out.println(head + " -> " + done + " . " + rest);
		} else // 以"()"输出
		{
			System.out.print(head);
			if (done.equals(done.toLowerCase())) {
				System.out.print("(");
				System.out.print(done);
				System.out.println(")");
			}
		}
	}
}