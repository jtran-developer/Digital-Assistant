package ai;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import actions.Commands;
import actions.Greetings;
import actions.SystemCommands;
import system.DataCommands;
import system.MyParsingException;


public class AIController {
	private DataCommands dc;
	private Greetings g;
	private SystemCommands sc;
	private Commands comm;
	
	public AIController() throws IOException, MyParsingException
	{
	}	
	public void setDataCommands(DataCommands dc)
	{
		this.dc = dc;
	}
	public void setAllCommandTypes(Greetings g, Commands comm, SystemCommands sc)
	{
		this.g = g;
		this.comm = comm;
		this.sc = sc;
	}
	
	public void newCommand(ArrayList<String> input)
	{
		ArrayList<String> inputList = input;
		
		String n = inputList.remove(0);
		String v = null;;
		try	{
			v = inputList.remove(0);}catch (Exception e){}

		if (n.equals("system"))
		{
			this.execute(n, v, inputList);
			return;
		}
		else if (n.equals("greetings"))
		{
			this.execute("greetings", null, input);
			return;
		}
		else if (v == null)
		{
			this.execute(n, null, input);
			return;
		}
		//////////////////////////
		//  Update Map Information
		DataMap noun = getDataMapFromHashMap(dc.getNounMasterHashMap(), n);
		DataMap verb = getDataMapFromHashMap(dc.getVerbMasterHashMap(), v);
		
		if (verb == null)
		{
			dc.getVerbMasterHashMap().put(v, new DataMap(v));
		}
		else if (noun == null)
		{
			dc.getNounMasterHashMap().put(n, new DataMap(n));	
		}
		noun.addCorrespondingWords(v);
		verb.addCorrespondingWords(n);	
		
		for  (String corrWord : dc.getNounMasterHashMap().get(n).getCorrespondingWords())
		{
			dc.getVerbMasterHashMap().get(v).addSimilarWords(corrWord);
			if (dc.getVerbMasterHashMap().get(corrWord) == null)
			{
				dc.getVerbMasterHashMap().put(corrWord, new DataMap(corrWord));
			}
			dc.getVerbMasterHashMap().get(corrWord).addSimilarWords(v);
		}
		
		for  (String corrWord : dc.getVerbMasterHashMap().get(v).getCorrespondingWords())
		{
			dc.getNounMasterHashMap().get(n).addSimilarWords(corrWord);
			if (dc.getNounMasterHashMap().get(corrWord) == null)
			{
				dc.getNounMasterHashMap().put(corrWord, new DataMap(corrWord));
			}
			dc.getNounMasterHashMap().get(corrWord).addSimilarWords(n);
		}		
		//////////////////////////
		
		
		////////////////////
		// find verb command
		String command = verb.getCommandCode();
		ArrayList<String> possibleCommands = new ArrayList<String>();
		if (command == null)
		{
			for (String similiarWord : verb.getSimilarWords())
			{
				String tempCommand = dc.getVerbMasterHashMap().get(similiarWord).getCommandCode();
				if (tempCommand != null)
				{
					if (!possibleCommands.contains(tempCommand))
					{
						possibleCommands.add(tempCommand);
					}
				}
			}
			for (String corrWord : noun.getCorrespondingWords())
			{			
				String tempCommand = dc.getVerbMasterHashMap().get(corrWord).getCommandCode();
				if (tempCommand != null)
				{
					if (!possibleCommands.contains(tempCommand))
					{
						possibleCommands.add(tempCommand);
					}
				}
			}		
		}
		if (possibleCommands.size() > 0)
		{
			// If there is more than one command
			boolean b;
			Scanner in;
			String inputString;
			int i = possibleCommands.size();
			for (String s : possibleCommands)
			{
				i--;
				b = true;
				while (b)
				{
					
					System.out.println("AI: Should this command be associated with: " + s + "? (" + i + " commands left)");
					in = new Scanner(System.in);
			        inputString = in.nextLine();
			        if (inputString.equalsIgnoreCase("y") || inputString.equalsIgnoreCase("yes"))
			        {
			        	verb.setCommandCode(s);
			        	break;
			        	//b = false;
			        	
			        }
			        else if (inputString.equalsIgnoreCase("n") || inputString.equalsIgnoreCase("no"))
			        {
			        	
			        	b = false;
			        }
			        else
			        {
			        	System.out.println("AI: Unknown command, please enter");
			        }
				}
			}
		}
		this.execute(n, v, inputList);
	}	
	private void execute(String n, String v, ArrayList<String> p)
	{
		String line = (n + " " + (new CommandLine(v, "", true).getCommand(p))).trim();
		if (line.startsWith("system"))
		{
			try {
				// Send to the SystemCommands class and run it there
				sc.run(new CommandLine(v, "", true).getCommand(p));
				
			} catch (Exception e) {e.printStackTrace();}
		}
		else if (dc.getGreetingsMasterHashMap().containsKey(line))
		{
			g.run(line);
		}
		else
		{
			if (v != null)
			{
				DataMap verbDataMap = dc.getVerbMasterHashMap().get(v);
				String commandCode = verbDataMap.getCommandCode();
				if (commandCode != null)
				{
					String commandLine = dc.getCommandMasterHashMap().get(commandCode).getCommand(p).trim();
					//Commands.run(commandLine);
					comm.run(commandLine);
				}
				else{System.out.println("AI: I don't know what to do with this command");}
			}
			else{System.out.println("AI: No command detected.");}
		}
	}

	private DataMap getDataMapFromHashMap(HashMap<String, DataMap> map, String s) {
		DataMap toReturn = map.get(s);
		if (toReturn == null)
		{
			map.put(s, new DataMap(s));
			toReturn = map.get(s);
		}
		return toReturn;
	}	
}
