package actions;

import system.VoiceResponse;

public class Greetings extends ExecutableActions{
	String line;
	public Greetings(String l)
	{
		this.line = l;
	}
	public void run()
	{
		if (line.equals("greetings"))
		{
			VoiceResponse.say("Greetings to too");
		}
	}
}
