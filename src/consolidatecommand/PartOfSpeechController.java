package consolidatecommand;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
 
public class PartOfSpeechController {
	private MaxentTagger tagger;
	
	private ArrayList<String> temp;
	private Scanner sc;
	private ArrayList<String> nounLabels;
	private ArrayList<String> verbLabels;
	
	public PartOfSpeechController()
	{
		System.out.println("Starting: POS tagging");
		initiate();
		System.out.println("Complete: POS tagging");
	}
	private void initiate()
	{
		
		/*  Nouns
		NN Noun, singular or mass
		NNS Noun, plural
		NNP Proper noun, singular
		NNPS Proper noun, plural 
		 */
		nounLabels = new ArrayList<String>();
		nounLabels.add("NN");
		nounLabels.add("NNS");
		nounLabels.add("NNP");
		nounLabels.add("NNPS");

		/*  Verbs
		VB Verb, base form
		VBD Verb, past tense
		VBG Verb, gerund or present participle
		VBN Verb, past participle
		VBP Verb, non­3rd person singular present
		VBZ Verb, 3rd person singular */
		verbLabels = new ArrayList<String>();
		verbLabels.add("VB");
		verbLabels.add("VBD");
		verbLabels.add("VBG");
		verbLabels.add("VBN");
		verbLabels.add("VBP");
		verbLabels.add("VBZ");
		
		tagger = new MaxentTagger(
                "consolidatecommand/models/english-left3words-distsim.tagger");
	}
 
    public ArrayList<String> parseLine(String st)
    {
    	ArrayList<String> nouns;
    	ArrayList<String> verbs;
    	String s = tagger.tagString(st);
    	
    	System.out.println("In PartOfSpeech tagger: " + s);
    	temp = new ArrayList<String>();
    	try
    	{
	    	nouns = getPOS(nounLabels, s);
	    	verbs = getPOS(verbLabels, s);
	    	temp.add(nouns.remove(0));
	    	temp.add(verbs.remove(0));
	    	temp.addAll(nouns);
    	}
    	catch (IndexOutOfBoundsException e)
    	{
    		// Not a standard noun/verb command
    		temp = new ArrayList<String>(Arrays.asList(st.split(" ")));
    	}
    	
    	return temp;
    }
    private ArrayList<String> getPOS(ArrayList<String> labels, String st)
    {
    	temp = new ArrayList<String>();
    	sc = new Scanner(st);
    	while (sc.hasNext())
    	{
    		String word = sc.next();
    		for (String l : labels)
    		{
    			if (word.endsWith(l)){
    				{
    					temp.add(word.replace("_" + l, ""));
    				}
    			}
    		}
    		
    	}
    	return temp;
    }
}