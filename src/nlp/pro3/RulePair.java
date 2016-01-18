package nlp.pro3;

/*
 * 产生式规则存储类，former为左部，latter为右部
 */
public class RulePair {
	public String former, latter;

	public RulePair(String former, String latter) {
		this.former = former;
		this.latter = latter;
	}

	public boolean isLessThan(RulePair _argRule) {
		return this.former.compareTo(_argRule.former) < 0;
	}
}