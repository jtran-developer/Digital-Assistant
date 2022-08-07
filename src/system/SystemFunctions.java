package system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import ai.CommandLine;
import ai.DataMap;

public class SystemFunctions {

	private DataCommands dc;
	
	private final String fileName = "commands.xml";
	private Scanner sc;

	public SystemFunctions() throws IOException
	{
		
	}
	public void setDataCommands(DataCommands dc)
	{
		this.dc = dc;
	}
	public void loadData() throws FileNotFoundException, MyParsingException
	{
		// Load all defaults
		loadDefaultItems(dc.getNounMasterHashMap());
		loadDefaultActions(dc.getVerbMasterHashMap());
		loadDefaultSystemCommands();
		loadCommandLines();
		loadGreetings();
		
		// Load from XML
		loadDataFromXML();

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
	
	private void loadCommandLines()
	{
		// Set up commands and link it to their 'code'
		dc.getCommandMasterHashMap().put("media player", new CommandLine("\"C:\\Program Files\\MPC-HC\\mpc-hc64.exe\"", "/play", true));
		dc.getCommandMasterHashMap().put("open", new CommandLine("", "", true));
		dc.getCommandMasterHashMap().put("shutdown", new CommandLine("shutdown /s", "", false));
		dc.getCommandMasterHashMap().put("restart", new CommandLine("shutdown /r", "", false));
		dc.getCommandMasterHashMap().put("shutdown abort", new CommandLine("shutdown /a", "", false));
		dc.getCommandMasterHashMap().put("logoff", new CommandLine("logoff", "", false));
	}
	private void loadDefaultSystemCommands()
	{
		dc.getSystemMasterHashMap().put("save commands", "saveToXML");
		dc.getSystemMasterHashMap().put("load commands", "loadFromXML");
		dc.getSystemMasterHashMap().put("print commands", "printData");
		dc.getSystemMasterHashMap().put("end program", "endProgram");
	}
	private void loadGreetings()
	{
		dc.getGreetingsMasterHashMap().put("hello", "hello to you too");
		dc.getGreetingsMasterHashMap().put("how are you", "i'm fine, thank you.");
		dc.getGreetingsMasterHashMap().put("whats up", "nothing much");
		dc.getGreetingsMasterHashMap().put("greeting", "greetings and salutations");
		dc.getGreetingsMasterHashMap().put("hi", "hi there");
		dc.getGreetingsMasterHashMap().put("how goes it", "it's going well");
		dc.getGreetingsMasterHashMap().put("howdy", "howdy partner");
		dc.getGreetingsMasterHashMap().put("good morning", "good morning to you too");
		dc.getGreetingsMasterHashMap().put("good evening", "good evening to you too");
		dc.getGreetingsMasterHashMap().put("good night", "good night and sleep well");
		dc.getGreetingsMasterHashMap().put("howdy", "howdy partner");
	}
	private boolean loadDataFromXML() throws FileNotFoundException, MyParsingException 
	{	
		FileReader fr;
		try
		{
			 fr = new FileReader(fileName);
		}
		catch (Exception e)
		{
			return false;
		}
		System.out.println(fr);
		BufferedReader br = new BufferedReader(fr); 
		this.sc = new Scanner(br);
		
		if (!sc.hasNext())
		{
			return false;
		}
		
		String s = null;
		if (sc.nextLine().equals("<root>"))
		{
			while (sc.hasNext())
			{
				s = sc.nextLine().trim();
				if (s.equals("<nounList>"))
				{
					dc.getNounMasterHashMap().putAll(setUpNounOrVerb("noun"));
				}
				if (s.equals("<verbList>"))
				{
					dc.getVerbMasterHashMap().putAll(setUpNounOrVerb("verb"));
				}
				if (s.equals("<commandList>"))
				{
					dc.getCommandMasterHashMap().putAll(setUpCommands());
					
				}
				if (s.equals("<systemcommandList>"))
				{
					dc.getSystemMasterHashMap().putAll(setUpSystemCommands());
				}
			}
		}
		else
		{
			throw new MyParsingException("root", s);
		}		
		sc.close();
		return true;
	}
	private HashMap<String, DataMap> setUpNounOrVerb(String type) throws MyParsingException
	{
		String s;
		String key;
		DataMap dm;
		HashMap<String, DataMap> temp = new HashMap<String, DataMap>();
		while (true)
		{
			key = null;
			dm = null;

			s = sc.nextLine().trim();
			
			if (type.equals(getAttribute(s)))
			{
				// All is right with the world
				s = sc.nextLine().trim();
				if (getAttribute(s).equals("key"))
				{
					key = getData(s);
				}else{throw new MyParsingException("setUp" + type + "-key", s);}
				
				s = sc.nextLine().trim(); 
				if (getAttribute(s).equals("datamap"))
				{
					// parse word
					dm = new DataMap(key);
					s = sc.nextLine().trim();
					if (getAttribute(s).equals("word"))
					{
						dm.setWord(getData(s));
					}else{throw new MyParsingException("setUp" + type + "-word", s);}

					s = sc.nextLine().trim();

					// parse Similar Words
					while (getAttribute(s).equals("similarWord"))
					{
						dm.addSimilarWords(getData(s));
						s = sc.nextLine().trim();
					}

					// parse Corresponding Words
					while (getAttribute(s).equals("correspondingWord"))
					{
						dm.addCorrespondingWords(getData(s));
						s = sc.nextLine().trim();
					}
					
					// parse blacklisted commands
					while (getAttribute(s).equals("blacklistedCommand"))
					{
						dm.addBlackListedCommands(getData(s));
						s = sc.nextLine().trim();
					}
					
					if (getAttribute(s).equals("commandCode"))
					{
						dm.setCommandCode(getData(s));
					}else{throw new MyParsingException("setUp\" + type + \"-commandCode", s);}
					
					// check end of datamap
					s = sc.nextLine().trim();
					if (!s.equals("</datamap>"))
					{
						throw new MyParsingException("setUp\" + type + \"-datamap", s);
					}
					
					// check end of section
					s = sc.nextLine().trim();
					if (!s.equals("</" + type + ">"))
					{
						throw new MyParsingException("setUp\" + type + \"-end of section", s);
					}
					temp.put(key, dm);
					
					
					
				}else{throw new MyParsingException("setUp\" + type + \"-datamap", s);}
				
			}
			else if (s.equals("</" + type + "List>"))
			{
				return temp;
			}	
		}
	}
	private HashMap<String, CommandLine> setUpCommands() throws MyParsingException
	{
		String s;
		String key;
		String prefix;
		String suffix;
		boolean useParameters = true;  // Default is true
		HashMap<String, CommandLine> temp = new HashMap<String, CommandLine>();
		
		while (true)
		{
			key = null;
			prefix = null;
			suffix = null;
			
			s = sc.nextLine().trim();
			if (getAttribute(s).equals("command"))
			{
				s = sc.nextLine().trim();
				if (getAttribute(s).equals("key"))
				{
					key = getData(s);	
				}else{throw new MyParsingException("setUpSystemCommands-key", s);}
				
				s = sc.nextLine().trim();
				if (getAttribute(s).equals("prefix"))
				{
					prefix = getData(s);	
				}else{throw new MyParsingException("setUpSystemCommands-prefix", s);}
			
				s = sc.nextLine().trim();
				if (getAttribute(s).equals("suffix"))
				{
					suffix = getData(s);	
				}else{throw new MyParsingException("setUpSystemCommands-suffix", s);}
				
				s = sc.nextLine().trim();
				if (getAttribute(s).equals("useParameters"))
				{
					useParameters = Boolean.parseBoolean(getData(s));	
				}else{throw new MyParsingException("setUpSystemCommands-suffix", s);}
				
				s = sc.nextLine().trim();
				if(!s.equals("</command>"))
				{
					throw new MyParsingException("end of section-value", s);
				}
				temp.put(key, new CommandLine(prefix, suffix, useParameters));
			}
			else if (s.equals("</commandList>"))
			{
				return temp;
			}
		}	
	}
	private HashMap<String, String> setUpSystemCommands() throws MyParsingException
	{
		String s;
		String key;
		String value;
		HashMap<String, String> temp = new HashMap<String, String>();
		
		while (true)
		{
			key = null;
			value = null;					
			s = sc.nextLine().trim();
			
			if (getAttribute(s).equals("systemcommand"))
			{
				s = sc.nextLine().trim();
				if (getAttribute(s).equals("key"))
				{
					key = getData(s);	
				}else{throw new MyParsingException("setUpSystemCommands-key", s);}
				
				s = sc.nextLine().trim();
				if (getAttribute(s).equals("value"))
				{
					value = getData(s);	
				}else{throw new MyParsingException("setUpSystemCommands-value", s);}
				
				s = sc.nextLine().trim();
				if(!s.equals("</systemcommand>"))
				{
					throw new MyParsingException("end of section-value", s);
				}
				temp.put(key, value);
			}
			else if (s.equals("</systemcommandList>"))
			{
				return temp;
			}
		}
	}
	private String getAttribute(String line)
	{
		return line.substring(line.indexOf("<")+1, line.indexOf(">")).replace("/", "");
	}
	private static String getData(String line)
	{
		return line.substring(line.indexOf(">")+1, line.indexOf("</"));
	}
	public void saveToXML(HashMap<String, DataMap> nounMasterHashMap, HashMap<String, DataMap> verbMasterHashMap,
			HashMap<String, CommandLine> commandMasterHashMap, HashMap<String, String> systemMasterHashMap) throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
	    
		String xml = getAllData(nounMasterHashMap, verbMasterHashMap, commandMasterHashMap, systemMasterHashMap);
		writer.write(xml);
	    writer.close();
	}
	public void printData(HashMap<String, DataMap> nounMasterHashMap, HashMap<String, DataMap> verbMasterHashMap,
			HashMap<String, CommandLine> commandMasterHashMap, HashMap<String, String> systemMasterHashMap)
	{
		System.out.println(getAllData(nounMasterHashMap, verbMasterHashMap, commandMasterHashMap, systemMasterHashMap));
	}
	public void endProgram()
	{
		VoiceResponse.say("Ending Program.  Goodbye.");
		System.exit(0);
	}
	
	//help function for saveToXML and printData
	private String getAllData(HashMap<String, DataMap> nounMasterHashMap, HashMap<String, DataMap> verbMasterHashMap,
			HashMap<String, CommandLine> commandMasterHashMap, HashMap<String, String> systemMasterHashMap)
	{
		StringBuilder s = new StringBuilder();
		s.append("<root>\n");
		s.append(getXML("noun", nounMasterHashMap));
		s.append(getXML("verb", verbMasterHashMap));
		s.append(getCommandXML(commandMasterHashMap));
		s.append(getSystemCommandXML(systemMasterHashMap));
		s.append("</root>");
		return s.toString();
	}
	//helper function for getAllData, loading the nouns and verbs
	private String getXML(String n, HashMap<String, DataMap> hm) 
	{

		StringBuilder s = new StringBuilder();
		s.append("\t<" + n + "List>\n");
		for (String k : hm.keySet())
		{
			s.append("\t\t<" + n + ">\n");
			s.append("\t\t\t<key>" + k + "</key>\n");
			s.append("\t\t\t<datamap>\n");
			s.append("\t\t\t\t<word>" + hm.get(k).getWord() + "</word>\n");
			for (String simWord : hm.get(k).getSimilarWords())
			{
				s.append("\t\t\t\t<similarWord>" + simWord + "</similarWord>\n");
			}
			for (String corrWord : hm.get(k).getCorrespondingWords())
			{
				s.append("\t\t\t\t<correspondingWord>" + corrWord + "</correspondingWord>\n");
			}
			for (String blistWord : hm.get(k).getBlacklistedCommands())
			{
				s.append("\t\t\t\t<blacklistedCommand>" + blistWord + "</blacklistedCommand>\n");
			}
			s.append("\t\t\t\t<commandCode>" + hm.get(k).getCommandCode() + "</commandCode>\n");
			s.append("\t\t\t</datamap>\n");
			s.append("\t\t</" + n + ">\n");
		}
		s.append("\t</" + n + "List>\n");
		return s.toString();	
	}
	//helper function for getAllData, loading the commands
	private String getCommandXML(HashMap<String, CommandLine> hm) 
	{

		StringBuilder s = new StringBuilder();
		s.append("\t<commandList>\n");
		for (String k : hm.keySet())
		{
			s.append("\t\t<command>\n");
			s.append("\t\t\t<key>" + k + "</key>\n");
			s.append("\t\t\t<prefix>" + hm.get(k).getPrefix() + "</prefix>\n");
			s.append("\t\t\t<suffix>" + hm.get(k).getSuffix() + "</suffix>\n");
			s.append("\t\t\t<useParameters>" + hm.get(k).getUseParameters() + "</useParameters>\n");
			s.append("\t\t</command>\n");
		}
		s.append("\t</commandList>\n");
		return s.toString();	
	}
	//helper function for getAllData, loading the system commands
	private String getSystemCommandXML(HashMap<String, String> hm) 
	{

		StringBuilder s = new StringBuilder();
		s.append("\t<systemcommandList>\n");
		for (String k : hm.keySet())
		{
			s.append("\t\t<systemcommand>\n");
			s.append("\t\t\t<key>" + k + "</key>\n");
			s.append("\t\t\t<value>" + hm.get(k) + "</value>\n");
			s.append("\t\t</systemcommand>\n");
		}
		s.append("\t</systemcommandList>\n");
		return s.toString();	
	}	
}