import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import actions.Commands;
import actions.Greetings;
import actions.SystemCommands;
import ai.AIController;
import consolidatecommand.CommandMatchingController;
import consolidatecommand.PartOfSpeechController;
import speechtotext.SpeechToTextController;
import system.DataCommands;
import system.MyParsingException;
import system.SystemFunctions;
import system.VoiceResponse;

public class PersonalAssistant {
	private SpeechToTextController stt;
	private CommandMatchingController cm;
	private PartOfSpeechController pos;
	
	private AIController ai;
	private DataCommands dc;
	private SystemFunctions sf;
	
	private Greetings g;
	private Commands comm;
	private SystemCommands sc;
	
	public PersonalAssistant() throws IOException, MyParsingException
	{
		
		this.pos = new PartOfSpeechController();
		this.cm = new CommandMatchingController();
		this.ai = new AIController();
		
		this.comm = new Commands();
		this.sc = new SystemCommands();
		this.g = new Greetings();
		
		this.dc = new DataCommands();
		this.sf = new SystemFunctions();
		
		cm.setDataCommands(dc); // Command matching needs to know the data from DataCommands
		ai.setDataCommands(dc); // AI needs to know the data from DataCommands (
		sf.setDataCommands(dc);  // SystemFunctions needs to know the DataCommands (to identify the right command)
		ai.setAllCommandTypes(g, comm, sc);  // AI needs to know the executable actions (to execute the commands)
		
		g.setDataCommands(dc);
		sc.setSystemFunctions(sf); // SystemCommands needs to know SystemFunctions (to call the right commands)
		sc.setDataCommands(dc); // SystemCommands needs to know the DataCommands (to identify the right methods)
		
		
		sf.loadData();
		VoiceResponse.initiate();
	}
	public void loadSpeech() throws IOException
	{
		this.stt = new SpeechToTextController();
		
		boolean run = true;
		while (run){
			System.out.println("Speak a command.");
			execute(this.stt.start());
		}
	}
	public void loadConsole()
	{
		Scanner consoleInputScanner = new Scanner(System.in);
		boolean run = true;
		while (run)
		{
	        System.out.println("Type in a command.");
			execute(consoleInputScanner.nextLine());
		}
        consoleInputScanner.close();
	}
	private void execute(String s)
	{
		String line = s.replace("please", "");
		// Command match
		ArrayList<String> commands = cm.findCommand(line);
		if (commands == null)
		{
			commands = this.pos.parseLine(line);
		}
		
		
		this.ai.newCommand(commands);
	}
}
