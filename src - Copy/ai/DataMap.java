package ai;

import java.util.ArrayList;

public class DataMap {
	private String word; //not used
	
	private ArrayList<String> similarWords;
	private ArrayList<String> correspondingWords;
	private ArrayList<String> blacklistedCommands;
	private String commandCode;
	public DataMap(String s)
	{
		this.word = s;
		this.similarWords = new ArrayList<String>();
		this.correspondingWords = new ArrayList<String>();
		this.blacklistedCommands = new ArrayList<String>();
		this.commandCode = null;
	}
	
	public void print()
	{
		// Print word
		System.out.println(" word:" + this.word);
		
		// Print similar words
		System.out.println(" similar words:");
		for (String s : this.similarWords)
		{
			System.out.println("  " + s);
		}
		
		// Print corresponding words
		System.out.println(" corresponding words:");
		for (String s : this.correspondingWords)
		{
			System.out.println("  " + s);
		}
		
		// Print blacklisted commands
		System.out.println(" blacklisted commands:");
		for (String s : this.blacklistedCommands)
		{
			System.out.println("  " + s);
		}
		
		// Print command code
		System.out.println(" command code: " + this.commandCode);
		
	}
	public void setCommandCode(String s)
	{
		this.commandCode = s;
	}
	public String getCommandCode()
	{
		return this.commandCode;
	}
	public void addSimilarWords(String word)
	{
		if (!this.word.equalsIgnoreCase(word) && !this.similarWords.contains(word))
		{
			this.similarWords.add(word);
		}
	}
	public void addCorrespondingWords(String word)
	{
		if (!this.word.equalsIgnoreCase(word) && !this.correspondingWords.contains(word))
		{
			this.correspondingWords.add(word);
		}
		
	}
	public void addBlackListedCommands(String word)
	{
		this.blacklistedCommands.add(word);
	}
	public ArrayList<String> getSimilarWords() {
		return similarWords;
	}

	public ArrayList<String> getCorrespondingWords() {
		return correspondingWords;
	}
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}
	public ArrayList<String> getBlacklistedCommands() {
		return blacklistedCommands;
	}

	public void setBlacklistedCommands(ArrayList<String> blacklistedCommands) {
		this.blacklistedCommands = blacklistedCommands;
	}

	

}
