import java.io.IOException;

import system.MyParsingException;

public class master {
	public static void main(String[] args) throws IOException, MyParsingException {

		PersonalAssistant pa = new PersonalAssistant();
		
		// initiate the program using speech inputs
		//pa.loadSpeech();
		
		// initiates the program using text/console inputs
		pa.loadConsole();
	}
}
