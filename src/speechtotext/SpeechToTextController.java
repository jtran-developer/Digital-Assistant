package speechtotext;


import java.io.IOException;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

public class SpeechToTextController {
	private Configuration configuration;
	private LiveSpeechRecognizer recognizer;
	public SpeechToTextController() throws IOException
	{
		System.out.println("Starting: speech to text");
		initiate();
		System.out.println("Complete: speech to text");
		

	/*StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
	InputStream stream = new FileInputStream(new File("test.wav"));

        recognizer.startRecognition(stream);
	SpeechResult result;
        while ((result = recognizer.getResult()) != null) {
	    System.out.println("Hypothesis: %s\n" + result.getHypothesis());
	}
	recognizer.stopRecognition();*/
	}
	private void initiate() throws IOException
	{
		configuration = new Configuration();

        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
        
        recognizer = new LiveSpeechRecognizer(configuration);
        recognizer.startRecognition(true);
	    
	}
	public String start() throws IOException
	{
	    // Start recognition process pruning previously cached data.
		SpeechResult result = recognizer.getResult();
	    //String line;
	    //while ((result = recognizer.getResult()) != null) 
	    //{
	    //	line = result.getHypothesis();
	    //    System.out.println("speech to text: " + line);
	    //    postagger.PartOfSpeechController.parseLine(line);
	    //}
	    //recognizer.stopRecognition();
	    
	    return result.getHypothesis();
	    
	    // Pause recognition process. It can be resumed then with startRecognition(false).
	    
	}   
}
