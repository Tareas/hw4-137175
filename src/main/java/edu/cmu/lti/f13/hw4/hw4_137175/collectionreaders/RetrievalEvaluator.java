package edu.cmu.lti.f13.hw4.hw4_137175.collectionreaders;

import java.util.HashMap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


//cosine similarity
import Jama.Matrix;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.examples.tokenizer.Token;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.util.ProcessTrace;
import org.cleartk.classifier.Feature;
import org.cleartk.classifier.feature.extractor.CleartkExtractor;
import org.cleartk.classifier.feature.function.FeatureFunctionExtractor;
import org.cleartk.token.type.Token_Type;

import edu.cmu.lti.f13.hw4.hw4_137175.typesystems.Document;
import edu.cmu.lti.f13.hw4.hw4_137175.utils.Utils;


//class RetrievalEvaluator
public class RetrievalEvaluator<AbstractSimilarity> extends CasConsumer_ImplBase {

	//class RetrievalEvaluatorResults
	public class RetrievalEvaluatorResults{
		
		public String docName;
		public double num;
		
		//Constructor RetrievalEvaluatorResults
		//receives String docName, double num
		public RetrievalEvaluatorResults(String docName, double num){
			this.docName = docName;
			this.num = num;
		}
	  };
		
	
	//Map receives Strings and Double
	final Map<String,Double> similarityMap = 
		      new HashMap<String,Double>();
	
	//private static final ArrayList<Integer> similarityMap = null;

	/** query id number **/
	public ArrayList<Integer> qIdList;

	/** query and text relevant values **/
	public ArrayList<Integer> relList;
    //feature extraction 
	private FeatureFunctionExtractor tokenFeatureExtractor;
    //similarity
	private AbstractSimilarity similarity;

	
	public void initialize() throws ResourceInitializationException {

		qIdList = new ArrayList<Integer>();

		relList = new ArrayList<Integer>();

	}

   //SetSimilarity
	public void setSimilarity(AbstractSimilarity similarity) {
	    this.similarity = similarity;
	  }
	
	/**
	 * TODO :: 1. construct the global word dictionary 2. keep the word
	 * frequency for each sentence
	 */
	@Override
	public void processCas(CAS aCas) throws ResourceProcessException {

		JCas jcas;
		try {
			jcas =aCas.getJCas();
		} catch (CASException e) {
			throw new ResourceProcessException(e);
		}

		FSIterator<Annotation> it = jcas.getAnnotationIndex(Document.type).iterator();
	
		if (it.hasNext()) {
			Document doc = (Document) it.next();

			//Make sure that your previous annotators have populated this in CAS
			FSList fsTokenList = doc.getTokenList();
		    ArrayList<Token>tokenList=Utils.fromFSListToCollection(fsTokenList, Token.class);

			qIdList.add(doc.getQueryID());
			relList.add(doc.getRelevanceValue());
		}
	}
	
	//Do something useful here
	
	//Features Extractor
	//new Feature("is first letter 'c'?", token.getCoveredText().charAt(0) == 'c')
	
	
	//Bag of words
	
		//CleartkExtractor<Token,Token> extractor = new CleartkExtractor <Token,Token>(
			//	Token.class,
				//new CoveredTextExtractor<Token>(),
				//new Bag_Words(new Following (2,5));
				//)
		    

	/**
	 * TODO 1. Compute Cosine Similarity and rank the retrieved sentences 2.
	 * Compute the MRR metric
	 * @return 
	 * @return 
	 */
	@Override
	public void collectionProcessComplete(ProcessTrace arg0)
			throws ResourceProcessException, IOException {

		super.collectionProcessComplete(arg0);

	}
	
	// TODO :: compute the cosine similarity measure
	//cosineSimilarityMeasure receives Matrix	
	public final double cosineSimilarityMeasure (Matrix inputDocu, Matrix outputDocu){
			
		double dotProduct =  inputDocu.arrayTimes(outputDocu).norm1();
	    double euclidianDist =  inputDocu.normF()* outputDocu.normF();
	    return dotProduct / euclidianDist;
	}
	/*
	 * @return cosine_similarity
	 */
	 private List<RetrievalEvaluator> computeCosineSimilarity(
			//final Map<String, Integer> queryVector)
			final Map<String, Integer> docVector) {
		    //double cosine_similarity=0.0;
		    List<String> docNames = new ArrayList<String>();
		    List <RetrievalEvaluator> result = new ArrayList <RetrievalEvaluator>();

		 // TODO :: compute cosine similarity between two sentences
		    Collections.sort(docNames, new Comparator<String>(){
		 //comparing two strings
		  public int compare(String s1, String s2) {
		        return similarityMap.get(s2).compareTo(similarityMap.get(s1));
		  }
		  });
		    for(String docName : docNames){
		      double num = similarityMap.get(docName);
		      if(num < 0.0001D){
		    	continue;  
		      }
		      result.addAll((Collection<? extends RetrievalEvaluator>) new RetrievalEvaluatorResults(docName,num));
		      }
		    return result;
		    }
		    		  
	/**
	 * 
	 * @return mrr
	 */
	@SuppressWarnings("unused")
	private double compute_mrr() {
		double metric_mrr=0.0;

		// TODO :: compute Mean Reciprocal Rank (MRR) of the text collection
		
		return metric_mrr;
	 }
    
}
  