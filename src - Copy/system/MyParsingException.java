package system;

public class MyParsingException extends Exception {
	String errorLocation;
	String errorInputLine;
	public MyParsingException(String l, String i)
	{
		this.errorLocation = l;
		this.errorInputLine = i;
	}
	public String toString()
	{
		return "Incorrect formatting in " + this.errorLocation + ": " + this.errorInputLine;
	}
}
