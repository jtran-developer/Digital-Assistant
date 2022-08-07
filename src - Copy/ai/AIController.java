package ai;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import actions.Commands;
import actions.Greetings;
import actions.SystemCommands;
import system.MyParsingException;
import system.SystemFunctions;


public class AIController {
	private HashMap<String, String> systemMasterHashMap;
	private HashMap<String, DataMap> nounMasterHashMap;
	private HashMap<String, DataMap> verbMasterHashMap;
	private HashMap<String, CommandLine> commandMasterHashMap;
	
	private SystemFunctions sf;
	private SystemCommands sc;
	public AIController() throws IOException, MyParsingException
	{
		initiate();
	}	
	public void aiConsole()
	{
		Scanner consoleInputScanner = new Scanner(System.in);
		boolean continueInputs = true;
        while (continueInputs)
        {
			System.out.println("Enter command.  Must be noun, verb, parameters (optional)");
			String command = consoleInputScanner.nextLine();
			
			if (command.equalsIgnoreCase("break") || (command.equalsIgnoreCase("stop")))
				{continueInputs = false;}
			
			ArrayList<String> consoleInput = new ArrayList<String>();
			
			Scanner inputtedItemsScanner = new Scanner(command);
			while (inputtedItemsScanner.hasNext())
			{
				consoleInput.add(inputtedItemsScanner.next());
			}
			inputtedItemsScanner.close();
			this.newCommand(consoleInput);
        }
        consoleInputScanner.close();
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
		//////////////////////////
		//  Update Map Information
		DataMap noun = getDataMapFromHashMap(nounMasterHashMap, n);
		DataMap verb = getDataMapFromHashMap(verbMasterHashMap, v);
		
		if (verb == null)
		{
			verbMasterHashMap.put(v, new DataMap(v));
		}
		else if (noun == null)
		{
			nounMasterHashMap.put(n, new DataMap(n));	
		}
		noun.addCorrespondingWords(v);
		verb.addCorrespondingWords(n);	
		
		for  (String corrWord : nounMasterHashMap.get(n).getCorrespondingWords())
		{
			verbMasterHashMap.get(v).addSimilarWords(corrWord);
			if (verbMasterHashMap.get(corrWord) == null)
			{
				verbMasterHashMap.put(corrWord, new DataMap(corrWord));
			}
			verbMasterHashMap.get(corrWord).addSimilarWords(v);
		}
		
		for  (String corrWord : verbMasterHashMap.get(v).getCorrespondingWords())
		{
			nounMasterHashMap.get(n).addSimilarWords(corrWord);
			if (nounMasterHashMap.get(corrWord) == null)
			{
				nounMasterHashMap.put(corrWord, new DataMap(corrWord));
			}
			nounMasterHashMap.get(corrWord).addSimilarWords(n);
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
				String tempCommand = verbMasterHashMap.get(similiarWord).getCommandCode();
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
				String tempCommand = verbMasterHashMap.get(corrWord).getCommandCode();
				if (tempCommand != null)
				{
					if (!possibleCommands.contains(tempCommand))
					{
						possibleCommands.add(tempCommand);
					}
				}
			}		
		}
		if (possibleCommands.size() > 1)
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
					
					System.out.println("Should this command be associated with: " + s + "? (" + i + " commands left)");
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
			        	System.out.println("Unknown command, please enter");
			        }
				}
			}
		}
		else
		{
			// If there is zero or one command
			verb.setCommandCode(command);
		}
		
		this.execute(n, v, inputList);
	}	
	private void execute(String noun, String verb, ArrayList<String> parameters)
	{
		if (noun.equals("system"))
		{
			try {
				// Send to the SystemCommands class and run it there
				//sc.run();
				sc.run(new CommandLine(verb, "").getCommand(parameters));
			} catch (Exception e) {e.printStackTrace();}
		}
		else if (noun.equals("greetings"))
		{
			new Greetings("greetings").run();
		}
		else
		{
			DataMap verbDataMap = verbMasterHashMap.get(verb);
			String commandCode = verbDataMap.getCommandCode();
			if (commandCode != null)
			{
				String commandLine = commandMasterHashMap.get(commandCode).getCommand(parameters).trim();
				//Commands.run(commandLine);
				new Commands(commandLine).run();
			}
			else{System.out.println("I don't know what to do with this command");}
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
	private void initiate() throws IOException, MyParsingException
	{
		
		this.nounMasterHashMap = new HashMap<String, DataMap>();
		this.verbMasterHashMap = new HashMap<String, DataMap>();
		this.commandMasterHashMap = new HashMap<String, CommandLine>();
		this.systemMasterHashMap = new HashMap<String, String>();
		this.sf = new SystemFunctions();
		this.sc = new SystemCommands(sf, nounMasterHashMap, nounMasterHashMap, commandMasterHashMap, systemMasterHashMap);
		
		loadDefaultItems(nounMasterHashMap);
		loadDefaultActions(verbMasterHashMap);
		loadDefaultSystemCommands();
		loadCommandLines();
		
		if (sf.loadDataFromXML())
		{
			systemMasterHashMap.putAll(sf.getSystemMasterHashMap());
			nounMasterHashMap.putAll(sf.getNounMasterHashMap());
			verbMasterHashMap.putAll(sf.getVerbMasterHashMap());
			commandMasterHashMap.putAll(sf.getCommandMasterHashMap());
		}
	}
	public void loadCommandLines()
	{
		// Set up commands and link it to their 'code'
		commandMasterHashMap.put("media player", new CommandLine("\"D:\\Program Files\\MPC-HC\\mpc-hc64.exe\"", "/play"));
		commandMasterHashMap.put("open", new CommandLine("", ""));
		commandMasterHashMap.put("shutdown", new CommandLine("shutdown /s", ""));
		commandMasterHashMap.put("restart", new CommandLine("shutdown /r", ""));
		commandMasterHashMap.put("shutdown abort", new CommandLine("shutdown /a", ""));
		commandMasterHashMap.put("logoff", new CommandLine("logoff", ""));
	}
	public void loadDefaultSystemCommands()
	{
		systemMasterHashMap.put("save commands", "saveToXML");
		systemMasterHashMap.put("load commands", "loadFromXML");
		systemMasterHashMap.put("print commands", "printData");
		systemMasterHashMap.put("end program", "endProgram");
	}
	private void loadDefaultItems(HashMap<String, DataMap> dm)
	{
		// Adding the default "song" noun
		dm.put("song", new DataMap("song"));
		dm.get("song").addCorrespondingWords("play");
	}
	
	private void loadDefaultActions(HashMap<String, DataMap> dm)
	{
		// Adding the default "play" verb
		DataMap temp = new DataMap("play");
		temp.addCorrespondingWords("song");
		temp.setCommandCode("media player");
		dm.put("play", temp);
		
		//Adding the default "open" verb
		temp = new DataMap("open");
		temp.addCorrespondingWords("file");
		temp.setCommandCode("open");
		dm.put("open", temp);
		
		// Adding the default "shutdown" actions
		temp = new DataMap("shutdown");
		temp.addCorrespondingWords("computer");
		temp.setCommandCode("shutdown");
		dm.put("shutdown", temp);
		
		// Adding the default "shutdown" actions
		temp = new DataMap("restart");
		temp.addCorrespondingWords("computer");
		temp.setCommandCode("restart");
		dm.put("restart", temp);
		
		// Adding the default "shutdown abort" actions
		temp = new DataMap("abort");
		temp.addCorrespondingWords("computer");
		temp.setCommandCode("shutdown abort");
		dm.put("abort", temp);		
	}
}
