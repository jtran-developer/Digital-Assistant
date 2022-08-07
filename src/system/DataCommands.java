package system;

import java.io.FileNotFoundException;
import java.util.HashMap;

import ai.CommandLine;
import ai.DataMap;

public class DataCommands {
	private HashMap<String, String> systemMasterHashMap;
	private HashMap<String, DataMap> nounMasterHashMap;
	private HashMap<String, DataMap> verbMasterHashMap;
	private HashMap<String, CommandLine> commandMasterHashMap;
	private HashMap<String, String> greetingsMasterHashMap;
	
	public DataCommands() throws FileNotFoundException, MyParsingException
	{
		this.systemMasterHashMap = new HashMap<String, String>();
		this.nounMasterHashMap = new HashMap<String, DataMap>();
		this.verbMasterHashMap = new HashMap<String, DataMap>();
		this.commandMasterHashMap = new HashMap<String, CommandLine>();
		this.greetingsMasterHashMap = new HashMap<String, String>();
		
		// load Data must be called separately
	}

	
	public HashMap<String, String> getSystemMasterHashMap() {
		return systemMasterHashMap;
	}

	public void setSystemMasterHashMap(HashMap<String, String> systemMasterHashMap) {
		this.systemMasterHashMap = systemMasterHashMap;
	}

	public HashMap<String, DataMap> getNounMasterHashMap() {
		return nounMasterHashMap;
	}

	public void setNounMasterHashMap(HashMap<String, DataMap> nounMasterHashMap) {
		this.nounMasterHashMap = nounMasterHashMap;
	}

	public HashMap<String, DataMap> getVerbMasterHashMap() {
		return verbMasterHashMap;
	}

	public void setVerbMasterHashMap(HashMap<String, DataMap> verbMasterHashMap) {
		this.verbMasterHashMap = verbMasterHashMap;
	}

	public HashMap<String, CommandLine> getCommandMasterHashMap() {
		return commandMasterHashMap;
	}

	public void setCommandMasterHashMap(HashMap<String, CommandLine> commandMasterHashMap) {
		this.commandMasterHashMap = commandMasterHashMap;
	}

	public HashMap<String, String> getGreetingsMasterHashMap() {
		return greetingsMasterHashMap;
	}

	public void setGreetingsMasterHashMap(HashMap<String, String> greetingsHashMap) {
		this.greetingsMasterHashMap = greetingsHashMap;
	}
}
