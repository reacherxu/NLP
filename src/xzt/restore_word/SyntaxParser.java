package xzt.restore_word;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/*
 * Proj. 3 分别采用深度优先和广度优先实现基于一个简单英语语法的自顶向下句法分析器
 */
public class SyntaxParser {
	HashMap<String, ArrayList<String>> lexicalMap = new HashMap<String, ArrayList<String>>();
    public static void main(String args[])
    {
    	SyntaxParser syntaxParser = new SyntaxParser();
//    	String file = "./data/MT-3/rule.txt";
    	String file = "D:\\我的酷盘\\PosGra\\Course\\NLP\\project\\syntax.txt";
    	syntaxParser.loadLexicalRule(file, "->");
    	System.out.println(syntaxParser.DFParseSyntax("The cat catched a mouse."));
//    	System.out.println(syntaxParser.BFParseSyntax("The cat catched a mouse."));
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
	    		ArrayList<String> tempArray;
	    		if(!lexicalMap.containsKey(content[0].trim()))
				{
	    			tempArray = new ArrayList<String>();
				}
	    		else
	    		{
	    			tempArray = lexicalMap.get(content[0].trim());
	    		}
    			tempArray.add(content[1].trim());
	    		lexicalMap.put(content[0].trim(), tempArray);
	    	}
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
    
    public boolean DFParseSyntax(String orig)
    {
    	if(orig.endsWith(".") || orig.endsWith("!") || orig.endsWith("?") || orig.endsWith(","))
    	{
    		orig = orig.substring(0, orig.length() - 1);
    	}
    	String[] terminators = orig.split("\\s");
    	Stack<Status> backupStatus = new Stack<Status>();
    	Status currentStatus = new Status("S", 0);
    	String rule;
    	String parentRule;

    	// If the sentence is not handled successfully, loop
    	while(currentStatus.position != terminators.length)
    	{
    		rule = currentStatus.rule;
    		int tmpos = rule.indexOf(' ');
    		// If only one symbol left
    		if(tmpos == -1)
    		{
    			tmpos = rule.length();
    		}
    		parentRule = rule.substring(0, tmpos);	
    		if(lexicalMap.containsKey(parentRule))
    		{
    			boolean terminated = false;
				// Find the right terminate rule Eg: N->cat N->dog
    			for(int i = 0; i < lexicalMap.get(parentRule).size(); ++i)
    			{
        			if(lexicalMap.get(parentRule).get(i).equals(terminators[currentStatus.position].toLowerCase()))
        			{
        				terminated = true;
        				// If only one symbol left
        				if(tmpos == rule.length())
        				{
        					currentStatus.rule = "";
        				}
        				else
        				{
        					currentStatus.rule = rule.substring(tmpos + 1);
        				}
        				++currentStatus.position;
        				System.out.println(parentRule + "->" + terminators[currentStatus.position - 1]);
        			}
    			}
    			// Handle the not-terminated rule 
    			if(!terminated)
    			{
    				String ruleSuffix = rule.substring(tmpos);
	    			rule = lexicalMap.get(parentRule).get(0) + ruleSuffix;
	    			currentStatus.rule = rule;
	    			for(int i = 1; i < lexicalMap.get(parentRule).size(); ++i)
	    			{
	    				rule = lexicalMap.get(parentRule).get(i) + ruleSuffix;
	    				backupStatus.push(new Status(rule, currentStatus.position));
	    			}
    			}
    		}
    		else
    		{
    			// Need the pop operation, but the stack is empty, jump out
    			if(backupStatus.empty())
    			{
    				break;
    			}
    			currentStatus = backupStatus.pop();
    		}
    	}
    	
    	if(currentStatus.position != terminators.length)
    	{
    		return false;
    	}
    	return true;
    }
    
    public boolean BFParseSyntax(String orig)
    {
    	if(orig.endsWith(".") || orig.endsWith("!") || orig.endsWith("?") || orig.endsWith(","))
    	{
    		orig = orig.substring(0, orig.length() - 1);
    	}
    	String[] terminators = orig.split("\\s");
    	Queue<Status> backupStatus = new LinkedList<Status>();
    	Status currentStatus = new Status("S", 0);
    	String rule;
    	String parentRule;

    	// If the sentence is not handled successfully, loop
    	while(currentStatus.position != terminators.length)
    	{
    		rule = currentStatus.rule;
    		int tmpos = rule.indexOf(' ');
    		// If only one symbol left
    		if(tmpos == -1)
    		{
    			tmpos = rule.length();
    		}
    		parentRule = rule.substring(0, tmpos);	
    		if(lexicalMap.containsKey(parentRule))
    		{
    			boolean terminated = false;
				// Find the right terminate rule Eg: N->cat N->dog
    			for(int i = 0; i < lexicalMap.get(parentRule).size(); ++i)
    			{
        			if(lexicalMap.get(parentRule).get(i).equals(terminators[currentStatus.position].toLowerCase()))
        			{
        				terminated = true;
        				// If only one symbol left
        				if(tmpos == rule.length())
        				{
        					currentStatus.rule = "";
        				}
        				else
        				{
        					currentStatus.rule = rule.substring(tmpos + 1);
        				}
        				++currentStatus.position;
        				System.out.println(parentRule + "->" + terminators[currentStatus.position - 1]);
        			}
    			}
    			// Handle the not-terminated rule 
    			if(!terminated)
    			{
    				String ruleSuffix = rule.substring(tmpos);
	    			rule = lexicalMap.get(parentRule).get(0) + ruleSuffix;
	    			currentStatus.rule = rule;
	    			for(int i = 1; i < lexicalMap.get(parentRule).size(); ++i)
	    			{
	    				rule = lexicalMap.get(parentRule).get(i) + ruleSuffix;
	    				backupStatus.add(new Status(rule, currentStatus.position));
	    			}
    			}
    		}
    		else
    		{
    			// Need the pop operation, but the stack is empty, jump out
    			if(backupStatus.isEmpty())
    			{
    				break;
    			}
    			currentStatus = backupStatus.remove();
    		}
    	}
    	
    	if(currentStatus.position != terminators.length)
    	{
    		return false;
    	}
    	return true;
    }
    
    public class Status
    {
    	String rule;
    	int position;
    	
    	public Status(String rule, int pos)
    	{
    		this.rule = rule;
    		this.position = pos;
    	}
    }
}
