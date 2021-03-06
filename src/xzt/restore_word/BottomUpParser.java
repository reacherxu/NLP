package xzt.restore_word;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
/*
 * 一般的自底向上的句法分析(非chart parsing)
 */
public class BottomUpParser {
	ArrayList<RulePair> lexicalList = new ArrayList<RulePair>();
    public static void main(String args[])
    {
    	BottomUpParser parser = new BottomUpParser();
    	String file = "./data/MT-3/newRule.txt";
    	parser.loadLexicalRule(file, "->");
    	System.out.println(parser.BottomUpParse("Time flies like an arrow."));
    }
    
    public void loadLexicalRule(String file, String splitChar)
    {
    	try
    	{
	    	BufferedReader br= new BufferedReader(new FileReader(file));
	    	String row;
	    	String[] content;
	    	while((row = br.readLine()) != null)
	    	{
	    		content = row.split(splitChar);
	    		lexicalList.add(new RulePair(content[0].trim(), content[1].trim()));
	    	}
//	    	sortRuleByFormer();
    	}
    	catch(FileNotFoundException e)
    	{
    		System.out.println("Exception " + e + " occured");
    	}
    	catch(IOException e)
    	{
    		System.out.println("Exception " + e + " occured");
    	}
    }
    
    public void sortRuleByFormer()
    {
    	boolean exchanged = true;
    	   	
    	while(exchanged)
    	{
    		exchanged = false;
	    	int j = 0;
	    	for(int i = lexicalList.size() - 1; i > j; --i)
	    	{
	    		if(lexicalList.get(i).isLessThan(lexicalList.get(i - 1)))
	    		{
	    			RulePair tmpRule = lexicalList.get(i);
	    			lexicalList.set(i, lexicalList.get(i - 1));
	    			lexicalList.set(i - 1, tmpRule);
	    			exchanged = true;
	    		}
	    	}
	    	++j;
    	}
    }
    
    public boolean BottomUpParse(String orig)
    {
    	if(orig.endsWith(".") || orig.endsWith("!") || orig.endsWith("?") || orig.endsWith(","))
    	{
    		orig = orig.substring(0, orig.length() - 1);
    	}
    	orig = orig.toLowerCase();

    	Queue<String> agenda = new LinkedList<String>();
    	Queue<String> process = new LinkedList<String>();
    	agenda.add(orig.trim());
    	process.add(orig.trim());
    	
    	String token;
//    	String curProcess = "";
    	while(!agenda.isEmpty())
    	{
    		token = agenda.remove();
//    		curProcess = process.remove();
    		
    		String[] content = token.split("\\s");
    		int length = content.length;
    		
    		for(int i = 0; i < length; ++i)
    		{
    			for(int j = i + 1; j <= length; ++j)
    			{
	    			String tmpToken = "";
	    			for(int x = i; x < j; ++x)
	    			{
	    				tmpToken += content[x] + " ";
	    			}
    	    		for(int k = 0; k < lexicalList.size(); ++k)
    	    		{
    	    			if(lexicalList.get(k).latter.equals(tmpToken.trim()))
    	    			{
    		    			String tmpTokenEnding = "";
    		    			for(int x = 0; x < i; ++x)
    		    			{
    		    				tmpTokenEnding += content[x] + " ";
    		    			}
    		    			tmpTokenEnding += lexicalList.get(k).former + " ";
        	    			for(int x = j; x < length; ++x)
        	    			{
        	    				tmpTokenEnding += content[x] + " ";
        	    			}
        	    			if(!agenda.contains(tmpTokenEnding.trim()))
        	    			{
	        	    			agenda.add(tmpTokenEnding.trim());
	        	    			System.out.println(tmpTokenEnding.trim());
        	    			}
    	    			}
    	    		}
    			}
    		}
    	}
    	return false;
    }   
}
