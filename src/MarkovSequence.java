import java.util.LinkedList;


public class MarkovSequence<T> {
	// Define a 1 dimensional markov random field
	MarkovRandomField<T> mrf = new MarkovRandomField<T>(1);
	
	
	
	int sequenceIndex = 0;
	
	public void add(T element){
		MarkovNode<T> elementNode = new MarkovNode<T>(element, sequenceIndex++);
		mrf.add(elementNode);
		
		//sequence.add(element);
		
	}
	
	/** 
	 * @return next predicted object in sequence or null. 
	 */
	public Object next(){
		return null;
	}
}
