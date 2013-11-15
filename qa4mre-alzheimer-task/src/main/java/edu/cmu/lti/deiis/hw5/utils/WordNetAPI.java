package edu.cmu.lti.deiis.hw5.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class WordNetAPI {
	static{
		

		System.setProperty("wordnet.database.dir", "/usr/share/wordnet");


	}
public static Set<String>  getHyponyms(String word)
{Set<String> set=new HashSet<String>();
	List<String> hyponymList=new ArrayList<String>();
	//wordnet-sense-index
	NounSynset nounSynset; 
	NounSynset[] hyponyms; 

	WordNetDatabase database = WordNetDatabase.getFileInstance(); 
	Synset[] synsets = database.getSynsets(word, SynsetType.NOUN); 
	for (int i = 0; i < synsets.length; i++) { 
	    nounSynset = (NounSynset)(synsets[i]); 
	    hyponyms = nounSynset.getHyponyms(); 
	  //  System.err.println(nounSynset.getWordForms()[0] + 
	    //        ": " + nounSynset.getDefinition() + ") has " + hyponyms.length + " hyponyms"); 
	
	    nounSynset.getTopics();
	for(int j=0;j<hyponyms.length;j++)
	{NounSynset hpn = hyponyms[j];
	String hpnString=hpn.getWordForms()[0];
	//NounSynset[] o = hpn.getTopics();
	//System.out.println("hypo+"+j+"is "+hpn.getWordForms()[0]);
	hyponymList.add(hpnString);
	//	hpn.
	set.add(hpnString);
	}
	}
	return set;

}




public static void main(String args[])
{Set<String> hyponymList=getHyponyms("fly");
	
for(String hpm:hyponymList)
{System.out.println(hpm);
	
}
}
}
