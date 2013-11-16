package edu.cmu.lti.deiis.hw5.annotators;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.resource.ResourceInitializationException;

import edu.cmu.lti.qalab.types.Question;
import edu.cmu.lti.qalab.types.QuestionAnswerSet;
import edu.cmu.lti.qalab.types.TestDocument;
import edu.cmu.lti.qalab.types.Dependency;
import edu.cmu.lti.qalab.types.NounPhrase;
import edu.cmu.lti.qalab.types.Token;
import edu.cmu.lti.qalab.utils.Utils;

public class QuestionCategoryAnnotator extends JCasAnnotator_ImplBase{

	
	@Override
	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);
		
	}

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		
		TestDocument testDoc=Utils.getTestDocumentFromCAS(aJCas);
				
		ArrayList<Question>questionList=Utils.getQuestionListFromTestDocCAS(aJCas);
		
		for(int i=0;i<questionList.size();i++){
			
			Question question=questionList.get(i);
			
			//get dependencies and noun phrases and convert to ArrayList
			FSList depFSList = question.getDependencies();
      ArrayList<Dependency> depList = new ArrayList<Dependency>();
      depList = Utils.fromFSListToCollection(depFSList,Dependency.class);
      
      FSList nounFSList = question.getNounList();
      ArrayList<NounPhrase> nounList = new ArrayList<NounPhrase>();
      nounList = Utils.fromFSListToCollection(depFSList,NounPhrase.class);
      if (nounList.size()<1){System.out.println("Why is this empty?");}
			
      //get question text
			String qText = question.getText();
			
			//order who, what, when, where, why, how, which, how many, how much
			ArrayList<Boolean> whWords = new ArrayList<Boolean>();
		  
			//create regex matchers for different wh-words
      Matcher whoMatch = Pattern.compile("[Ww]ho").matcher(qText);
      Matcher whatMatch = Pattern.compile("[Ww]hat").matcher(qText);
      Matcher whenMatch = Pattern.compile("[Ww]hen").matcher(qText);
      Matcher whereMatch = Pattern.compile("[Ww]here").matcher(qText);
      Matcher whyMatch = Pattern.compile("[Ww]hy").matcher(qText);
      Matcher howMatch = Pattern.compile("[Hh]ow(?! (many|much))").matcher(qText);
      Matcher whichMatch = Pattern.compile("[Ww]hich").matcher(qText);
      Matcher howmanyMatch = Pattern.compile("[Hh]ow many").matcher(qText);
      Matcher howmuchMatch = Pattern.compile("[Hh]ow much").matcher(qText);
      
    //check for question words (i.e. wh-words, how many, how much, etc.)
      whWords.add(whoMatch.find());
      whWords.add(whatMatch.find());
      whWords.add(whenMatch.find());
      whWords.add(whereMatch.find());
      whWords.add(whyMatch.find());
      whWords.add(howMatch.find());
      whWords.add(whichMatch.find());
      whWords.add(howmanyMatch.find());
      whWords.add(howmuchMatch.find());
      
      //check if there are multiple wh-expressions in the question
      int numMatch = 0;
      for (int j=0;j<whWords.size();j++){numMatch+= whWords.get(j)? 1 : 0;}
      
      //set category if the question begins with the appropriate wh/how expression
      if (numMatch < 2){
        if (whWords.get(0)){question.setCategory("who");}
        else if (whWords.get(1)){question.setCategory("what");}
        else if (whWords.get(2)){question.setCategory("when");}
        else if (whWords.get(3)){question.setCategory("where");}
        else if (whWords.get(4)){question.setCategory("why");}
        else if (whWords.get(5)){question.setCategory("how");}
        else if (whWords.get(6)){question.setCategory("which");}
        else if (whWords.get(7)){question.setCategory("howmany");}
        else if (whWords.get(8)){question.setCategory("howmuch");}
        else {question.setCategory("other");}
      } else {question.setCategory("conflict");}
      //resolve category conflict for questions with multiple wh-words
      //determine if one is merely introducing a subordinate clause
      //check if numMatch != 1 then do stuff

			//question.addToIndexes();
			questionList.set(i, question);
			
			/*System.out.println("Question " + (i+1) + ": " + question.getText());
			System.out.print("Category: " + question.getCategory());
			if (numMatch>1) {
			  System.out.println("\t\t   who    what  when   where   why    how    which howmany howmuch");
	      System.out.println("\t\t\t" + numMatch + " " + whWords);  
			} else {System.out.println();}
			System.out.println();*/
			if (question.getCategory()=="which"){
			  System.out.println("Question " + (i+1) + ": " + question.getText());
			  
			  int whPos = whichMatch.start();
			  int minDist = qText.length();
			  for(int j=0;j<nounList.size();j++){
			    NounPhrase np = nounList.get(j);
			    System.out.println(np);
			    Matcher npMatch = Pattern.compile(np.getText()).matcher(qText);
			    if (npMatch.find(whPos)){
			      int nounPos = npMatch.start();
			      int dist = nounPos - whPos;
	          if (dist <=  minDist){
	            minDist = dist;
	            question.setAskingFor(np.getText());
	          }
			    }
			  }
			  
			  for(int j=0;j<depList.size();j++){
			    Dependency depRel = depList.get(j);
			    Token dep = depRel.getDependent();
			    Token gov = depRel.getGovernor();
			    if (dep.getText().equalsIgnoreCase("which")){
			      if (question.getAskingFor() == null && gov.getPos().startsWith("NN")){
			        question.setAskingFor(gov.getText());
			      }
			    }
			  }
			  System.out.println("Category: " + question.getCategory() + " \t" + "Asking for " + question.getAskingFor());
			  System.out.println();
			  }
		}
		
		//FSList fsQuestionList=Utils.createQuestionList(aJCas, questionList);
		//testDoc.setQuestionList(fsQuestionList);
		
		ArrayList<QuestionAnswerSet>qaSet=Utils.getQuestionAnswerSetFromTestDocCAS(aJCas);
		for(int i=0;i<qaSet.size();i++){
			questionList.get(i).addToIndexes();
			qaSet.get(i).setQuestion(questionList.get(i));
		}
		FSList fsQASet=Utils.createQuestionAnswerSet(aJCas, qaSet);
		
		testDoc.setQaList(fsQASet);
		testDoc.addToIndexes();
		
	}

}
