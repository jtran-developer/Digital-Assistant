package consolidatecommand;

import java.util.ArrayList;
import java.util.Scanner;

import system.DataCommands;

public class CommandMatchingController {
	private DataCommands dc;
	
	public CommandMatchingController()
	{
		
	}
	public void setDataCommands(DataCommands dc)
	{
		this.dc = dc;
	}
	public ArrayList<String> findCommand(String s)
	{
		ArrayList<String> temp;
		ArrayList<String> parameters;
		Scanner sc = new Scanner(s);
		String noun = "";
		String verb = "";
		parameters = new ArrayList<String>();
		if (sc.hasNext())
		{
			noun = sc.next();
		}
		if (sc.hasNext())
		{
			verb = sc.next();
		}
		while (sc.hasNext())
		{
			parameters.add(sc.next());
		}
		
		if (dc.getNounMasterHashMap().containsKey(noun) || 
			dc.getVerbMasterHashMap().containsKey(verb) ||
			dc.getSystemMasterHashMap().containsKey(s.replace("system ", "")) ||
			dc.getGreetingsMasterHashMap().containsKey(s) )
		{	
			temp = new ArrayList<String>();
			temp.add(noun);
			temp.add(verb);
			temp.addAll(parameters);
			System.out.println("Command Match: "  + s);
			sc.close();
			return temp;
		}
		sc.close();
		return null;
	}
}
