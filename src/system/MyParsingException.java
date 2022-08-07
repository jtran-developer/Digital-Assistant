package system;

public class MyParsingException extends Exception {
	private String errorLocation;
	private String errorInputLine;
	public MyParsingException(String l, String i)
	{
		this.errorLocation = l;
		this.errorInputLine = i;
	}
	public String toString()
	{
		return "SF: Incorrect formatting in " + this.errorLocation + ": " + this.errorInputLine;
	}
}
