package edu.cmu.lti.deiis.hw5.utils;

import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class WordNetAPI {
	static{
		

		System.setProperty("wordnet.database.dir", "/usr/share/wordnet");


	}
public static void getHyponyms(String word)
{
	//wordnet-sense-index
	NounSynset nounSynset; 
	NounSynset[] hyponyms; 

	WordNetDatabase database = WordNetDatabase.getFileInstance(); 
	Synset[] synsets = database.getSynsets(word, SynsetType.NOUN); 
	for (int i = 0; i < synsets.length; i++) { 
	    nounSynset = (NounSynset)(synsets[i]); 
	    hyponyms = nounSynset.getHyponyms(); 
	    System.err.println(nounSynset.getWordForms()[0] + 
	            ": " + nounSynset.getDefinition() + ") has " + hyponyms.length + " hyponyms"); 
	
	
	for(int j=0;j<hyponyms.length;j++)
	{NounSynset hpn = hyponyms[j];
		//System.out.println("hypo+"+j+"is "+hpn.getTopics());
	//	hpn.
	}
	}

}




public static void main(String args[])
{getHyponyms("fly");
	
}
}
