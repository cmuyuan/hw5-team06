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
			
			//check for question words (i.e. wh-words, how many, how much, etc.)
			String qText = question.getText();
			
			Pattern quantPattern = Pattern.compile("[Hh]ow many");
      Matcher quantMatcher = quantPattern.matcher(qText);
      
      Pattern timePattern = Pattern.compile("[Ww]hen");
      Matcher timeMatcher = timePattern.matcher(qText);
      
      //set category if the question begins with the appropriate wh/how expression
      if (quantMatcher.lookingAt()){question.setCategory("quantity");}
      else if (timeMatcher.lookingAt()){question.setCategory("time");}
      else {question.setCategory("other");}

			question.addToIndexes();
			questionList.set(i, question);
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
