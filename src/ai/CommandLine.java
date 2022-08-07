package ai;

import java.util.ArrayList;

public class CommandLine {
	private String prefix;
	private String suffix;

	private boolean useParameters;
	public CommandLine(String p, String s, boolean u)
	{
		this.prefix = p;
		this.suffix = s;
		this.useParameters = u;
	}
	public String getCommand(ArrayList<String> param)
	{
		String c = this.prefix;
		if (!this.useParameters)
		{
			return c.trim();
		}
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
	public boolean getUseParameters() {
		return useParameters;
	}
	public void setUseParameters(boolean useParameters) {
		this.useParameters = useParameters;
	}
}
