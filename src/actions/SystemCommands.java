package actions;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import system.DataCommands;
import system.MyParsingException;
import system.SystemFunctions;
import system.VoiceResponse;

public class SystemCommands {
	private SystemFunctions sf;
	private DataCommands dc;
	public SystemCommands()
	{
	
	}
	public void setSystemFunctions(SystemFunctions sf)
	{
		this.sf = sf;
	}
	public void setDataCommands(DataCommands dc)
	{
		this.dc = dc;
	}
	public void run(String c) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException 
	{
		System.out.println("Executing SystemCommand: " + c);
		Method method = this.getClass().getMethod(dc.getSystemMasterHashMap().get(c), null);
		method.invoke(this, null);
	}
	
	public void saveToXML() throws IOException
	{
		VoiceResponse.say("Saving Commands to XML");
		sf.saveToXML(dc.getNounMasterHashMap(), dc.getVerbMasterHashMap(), dc.getCommandMasterHashMap(), dc.getSystemMasterHashMap());
	}
	public void loadFromXML() throws IOException, MyParsingException
	{
		//VoiceResponse.say("Loading commands from XML");
		sf.loadData();
	}
	public void printData() throws IOException
	{
		VoiceResponse.say("Printing Data");
		sf.printData(dc.getNounMasterHashMap(), dc.getVerbMasterHashMap(), dc.getCommandMasterHashMap(), dc.getSystemMasterHashMap());
	}
	public void endProgram()
	{
		sf.endProgram();
	}
}
