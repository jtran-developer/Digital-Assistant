package actions;

import java.io.IOException;

public class Commands{
	public Commands()
	{
	}
	
	public void run(String c)
	{
		System.out.println("Executing Command: " + c);
		Runtime rt = Runtime.getRuntime();
		try {
			rt.exec(new String[] {"cmd.exe", "/c", c});
		} catch (IOException e) {e.printStackTrace();}	
	}


}
