package system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import ai.CommandLine;
import ai.DataMap;

public class SystemFunctions {
	private HashMap<String, String> systemMasterHashMap;
	private HashMap<String, DataMap> nounMasterHashMap;
	private HashMap<String, DataMap> verbMasterHashMap;
	private HashMap<String, CommandLine> commandMasterHashMap;
	
	private final String fileName = "commands.xml";
	private Scanner sc;
	
	public SystemFunctions() throws IOException
	{
		
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
	
	public String getAllData(HashMap<String, DataMap> nounMasterHashMap, HashMap<String, DataMap> verbMasterHashMap,
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
	
	public String getXML(String n, HashMap<String, DataMap> hm) 
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
	public String getCommandXML(HashMap<String, CommandLine> hm) 
	{

		StringBuilder s = new StringBuilder();
		s.append("\t<commandList>\n");
		for (String k : hm.keySet())
		{
			s.append("\t\t<command>\n");
			s.append("\t\t\t<key>" + k + "</key>\n");
			s.append("\t\t\t<prefix>" + hm.get(k).getPrefix() + "</prefix>\n");
			s.append("\t\t\t<suffix>" + hm.get(k).getSuffix() + "</suffix>\n");
			s.append("\t\t</command>\n");
		}
		s.append("\t</commandList>\n");
		return s.toString();	
	}
	
	public String getSystemCommandXML(HashMap<String, String> hm) 
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
	
	
	public boolean loadDataFromXML() throws FileNotFoundException, MyParsingException 
	{
		systemMasterHashMap = new HashMap<String, String>();
		nounMasterHashMap = new HashMap<String, DataMap>();
		verbMasterHashMap = new HashMap<String, DataMap>();
		commandMasterHashMap = new HashMap<String, CommandLine>();
		
		BufferedReader br = new BufferedReader(new FileReader(fileName)); 
		
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
					setUpNounOrVerb("noun");
				}
				if (s.equals("<verbList>"))
				{
					setUpNounOrVerb("verb");
				}
				if (s.equals("<commandList>"))
				{
					setUpCommands();
				}
				if (s.equals("<systemcommandList>"))
				{
					setUpSystemCommands();
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
	
	public void setUpNounOrVerb(String type) throws MyParsingException
	{
		String s;
		String key;
		DataMap dm;
		
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
					
					if (type.equals("noun"))
					{
						nounMasterHashMap.put(key, dm);
					}
					else if (type.equals("verb"))
					{
						verbMasterHashMap.put(key, dm);
					}
					
				}else{throw new MyParsingException("setUp\" + type + \"-datamap", s);}
				
			}
			else if (s.equals("</" + type + "List>"))
			{
				break;
			}	
		}
	}
	public void setUpCommands() throws MyParsingException
	{
		String s;
		String key;
		String prefix;
		String suffix;
		
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
				if(!s.equals("</command>"))
				{
					throw new MyParsingException("end of section-value", s);
				}
				commandMasterHashMap.put(key, new CommandLine(prefix, suffix));
			}
			else if (s.equals("</commandList>"))
			{
				break;
			}
		}	
	}
	public void setUpSystemCommands() throws MyParsingException
	{
		String s;
		String key;
		String value;
		
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
				systemMasterHashMap.put(key, value);
			}
			else if (s.equals("</systemcommandList>"))
			{
				break;
			}
		}
	}
	public String getAttribute(String line)
	{
		return line.substring(line.indexOf("<")+1, line.indexOf(">")).replace("/", "");
	}
	public static String getData(String line)
	{
		return line.substring(line.indexOf(">")+1, line.indexOf("</"));
	}
	public HashMap<String, DataMap> getNounMasterHashMap()
	{
		return nounMasterHashMap;
	}
	public HashMap<String, DataMap> getVerbMasterHashMap()
	{
		return verbMasterHashMap;
	}
	public HashMap<String, CommandLine> getCommandMasterHashMap()
	{
		return commandMasterHashMap;
	}
	public HashMap<String, String> getSystemMasterHashMap()
	{
		return systemMasterHashMap;
	}		
}