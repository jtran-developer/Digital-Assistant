package system;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class VoiceResponse {
	private static Voice voice;
	private static VoiceManager vm;
	public VoiceResponse()
	{
	}
	public static void initiate()
	{
		vm = VoiceManager.getInstance();
		voice = vm.getVoice("kevin16");
		voice.allocate();
	}
	
	public static void say(String c)
	{
		voice.speak(c);
	}

	
	
	
}
