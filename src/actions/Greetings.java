package actions;

import system.DataCommands;
import system.VoiceResponse;

public class Greetings{
	private DataCommands dc;
	public Greetings()
	{
		
	}
	public void setDataCommands(DataCommands dc)
	{
		this.dc = dc;
	}
	public void run(String s)
	{
		System.out.println("Greeting received: " + s);
		VoiceResponse.say(dc.getGreetingsMasterHashMap().get(s).trim());
	}
}
