package ai;

import java.util.ArrayList;

public class CommandLine {
	String prefix;
	
	String suffix;
	
	public CommandLine(String p, String s)
	{
		this.prefix = p;
		this.suffix = s;
	}
	public String getCommand(ArrayList<String> param)
	{
		String c = this.prefix;
		for (String p : param)
		{
			c += " " + p; 
		}
		c += " " + suffix;
		return c.trim();
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
}
