package edu.cmu.lti.f13.hw4.hw4_137175.collectionreaders;

import java.util.ArrayList;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.cleartk.classifier.CleartkSequenceAnnotator;
import org.cleartk.classifier.Feature;
import org.cleartk.classifier.feature.extractor.CleartkExtractor;
import org.cleartk.classifier.feature.extractor.CleartkExtractor.Context;
import org.cleartk.classifier.feature.extractor.simple.SimpleFeatureExtractor;
import org.cleartk.classifier.feature.function.FeatureFunctionExtractor;
import org.cleartk.classifier.feature.function.LowerCaseFeatureFunction;
import org.cleartk.classifier.feature.function.NumericTypeFeatureFunction;

import edu.cmu.lti.f13.hw4.hw4_137175.typesystems.Document;
import edu.cmu.lti.f13.hw4.hw4_137175.typesystems.Token;



public class DocumentReader 
extends JCasAnnotator_ImplBase  {

	private SimpleFeatureExtractor tokenFeatureExtractor;
	private CleartkExtractor contextFeatureExtractor;
	
	@Override
	public void process(JCas jcas) 
			throws AnalysisEngineProcessException {
		
		// reading sentence from the CAS 
		String sLine = jcas.getDocumentText();

		// TODO: make sure information from text collection are extracted correctly
		ArrayList<String> docInfo = parseDataLine(sLine);
		
		//This is to make sure that parsing done properly and 
		//minimal data for rel,qid,text are available to proceed 
		if(docInfo.size()<3){
			System.err.println("Not enough information in the line");
			return;
		}
		int rel = Integer.parseInt(docInfo.get(0));
		int qid = Integer.parseInt(docInfo.get(1));
		String txt = docInfo.get(2);
		
		Document doc = new Document(jcas);
		doc.setText(txt);
		doc.setQueryID(qid);
		//Setting relevance value
		doc.setRelevanceValue(rel);
		doc.addToIndexes();
		
		//Adding populated FeatureStructure to CAS
		jcas.addFsToIndexes(doc);
	}


	public static ArrayList<String> parseDataLine(String line) {
		ArrayList<String> docInfo;

		String [] rec  = line.split("[\\t]");
		String    sResQid = (rec[0]).replace("qid=", "");
		String    sResRel = (rec[1]).replace("rel=", "");
		

		StringBuffer sResTxt = new StringBuffer();
		for (int i=2; i<rec.length; i++) {
			sResTxt.append(rec[i]).append(" ");					
		}

		docInfo = new ArrayList<String>();
		docInfo.add(sResRel);
		docInfo.add(sResQid);
		docInfo.add(sResTxt.toString());
		return docInfo;
	}
	
	
	
	//features about words
    //kinds of features that are extracted from initialize method
	public void initialize(UimaContext context) throws ResourceInitializationException{
		
		super.initialize(context);
					
		Annotation token;
		this.tokenFeatureExtractor = new FeatureFunctionExtractor(
				new NumericTypeFeatureFunction(),
				new LowerCaseFeatureFunction(),
				new Feature("is first letter 'c'?", token.getCoveredText().charAt(0) == 'c'),
				new Feature("is first letter 'm'?", token.getCoveredText().charAt(0) == 'm'),
				new Feature("is first letter 't'?", token.getCoveredText().charAt(0) == 't'),
				new Feature("is first letter 'o'?", token.getCoveredText().charAt(0) == 'o'));
	
		        this.contextFeatureExtractor = new CleartkExtractor(
        		 Token.class,
        		 new Feature(),
        		 new Context (2));
	}
        		 
	}
	
