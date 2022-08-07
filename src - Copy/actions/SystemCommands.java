package actions;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import ai.CommandLine;
import ai.DataMap;
import system.MyParsingException;
import system.SystemFunctions;
import system.VoiceResponse;

public class SystemCommands {
	SystemFunctions sf;
	private HashMap<String, String> systemMasterHashMap;
	private HashMap<String, DataMap> nounMasterHashMap;
	private HashMap<String, DataMap> verbMasterHashMap;
	private HashMap<String, CommandLine> commandMasterHashMap;
	public SystemCommands(SystemFunctions sf, HashMap<String, DataMap> n, HashMap<String, DataMap> v, HashMap<String, CommandLine> c, HashMap<String, String> s)
	{
		this.sf = sf;
		this.nounMasterHashMap = n;
		this.verbMasterHashMap = v;
		this.commandMasterHashMap = c;
		this.systemMasterHashMap = s;
	}
	public void run(String c) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException 
	{
		Method method = this.getClass().getMethod(systemMasterHashMap.get(c), null);
		method.invoke(this, null);
	}
	
	public void saveToXML() throws IOException
	{
		VoiceResponse.say("Saving Commands to XML");
		sf.saveToXML(nounMasterHashMap, verbMasterHashMap, commandMasterHashMap, systemMasterHashMap);
	}
	public void loadFromXML() throws IOException
	{
		//VoiceResponse.say("Loading commands from XML");
		try
		{
			sf.loadDataFromXML();
		}
		catch (MyParsingException e)
		{
			System.out.println(e);
		}
	}
	public void printData() throws IOException
	{
		VoiceResponse.say("Printing Data");
		sf.printData(nounMasterHashMap, verbMasterHashMap, commandMasterHashMap, systemMasterHashMap);
	}
	public void endProgram()
	{
		VoiceResponse.say("Ending Program.  Goodbye.");
		System.exit(0);
	}
}
