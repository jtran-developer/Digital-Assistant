import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import ai.AIController;
import postagger.PartOfSpeechController;
import speechtotext.SpeechToTextController;
import system.MyParsingException;
import system.VoiceResponse;

public class PersonalAssistant {
	SpeechToTextController stt;
	PartOfSpeechController pos;
	AIController ai;	
	String speechString;
	ArrayList<String> partsOfSpeech;
	
	public PersonalAssistant() throws IOException, MyParsingException
	{
		this.pos = new PartOfSpeechController();
		this.ai = new AIController();
		VoiceResponse.initiate();
	}
	
	// Load program using speech
	public void loadSpeech() throws IOException
	{
		System.out.println("Enter command.  Must be noun, verb, parameters (optional)");
		this.stt = new SpeechToTextController();
		this.speechString = this.stt.start();
		execute();
	}
	
	// Load program using text/console
	public void loadConsole()
	{
		Scanner consoleInputScanner = new Scanner(System.in);
		boolean run = true;
		while (run)
		{
	        System.out.println("Enter command.  Must be noun, verb, parameters (optional)");
			this.speechString = consoleInputScanner.nextLine();
			if (this.speechString.equals("break"))
			{
				run = false;
			}
			else 
			{
				execute();
			}
		}
        consoleInputScanner.close();
	}
	
	// execute is called from both loadSpeech() and loadConsole()
	// and it initiates the next step of the application
	public void execute()
	{
		System.out.println(speechString);
		this.partsOfSpeech = this.pos.parseLine(speechString);
		this.ai.newCommand(this.partsOfSpeech);
	}
}
