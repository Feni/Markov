import java.util.HashMap;


public class MarkovMessage<T>{
	HashMap<KnownMarkovNode<T>, Float> suggestions;
	float confidence;
	
	public MarkovMessage(HashMap<KnownMarkovNode<T>, Float> sug, float c){
		suggestions = sug;
		confidence = c;
	}
}
