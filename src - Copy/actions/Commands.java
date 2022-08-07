package actions;

import java.io.IOException;

public class Commands extends ExecutableActions {
	String command;
	public Commands(String c)
	{
		this.command = c;
	}
	
	public void run()
	{
		Runtime rt = Runtime.getRuntime();
		try {
			//Process pr = rt.exec(commandLine.strip());
			Process pr = rt.exec(new String[] {"cmd.exe", "/c", command});
		} catch (IOException e) {e.printStackTrace();}	
	}


}
