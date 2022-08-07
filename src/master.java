import java.io.IOException;

import system.MyParsingException;

public class master {
	public static void main(String[] args) throws IOException, MyParsingException {

		PersonalAssistant pa = new PersonalAssistant();
		//pa.loadSpeech();
		pa.loadConsole();
		System.out.println(1);
	}
}
