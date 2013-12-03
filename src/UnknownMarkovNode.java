import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;


public class UnknownMarkovNode<T> extends MarkovNode<T>{
	
	// Number of 'votes' that this node is to be some particular value
	//HashMap<KnownMarkovNode<T>, Float> isNode = new HashMap<KnownMarkovNode<T>, Float>();
	
	HashMap<MarkovCoordinate, KnownMarkovNode<T>> knownNeighbors = new HashMap<MarkovCoordinate, KnownMarkovNode<T>>();
	HashMap<MarkovCoordinate, UnknownMarkovNode<T>> unkNeighbors = new HashMap<MarkovCoordinate, UnknownMarkovNode<T>>();	
	
	//HashMap<UnknownMarkovNode<T>, MarkovCoordinate> unkNeighbors = new HashMap<UnknownMarkovNode<T>, MarkovCoordinate>();
	int currentIteration = 0;
		
	HashMap<KnownMarkovNode<T>, Float> baseVotes;
	double baseTotal = 0.0;
	
	HashMap<KnownMarkovNode<T>, Float> votes = new HashMap<KnownMarkovNode<T>, Float>();
	double totalVotes = 0.0;
	
	KnownMarkovNode<T> currentIdentity;
	double currentWeight;
	
	int id;
	static int MAX_ID = 0;
	
	public UnknownMarkovNode(){
		id = MAX_ID++;
	}
	
	public String toString(){
		return "?<"+id+">"; 
	}
	
	KnownMarkovNode<T> currentState;
	float currentStateConfidence = 0.0f;
	
	
	public void addSuggestions(HashMap<KnownMarkovNode<T>, Float> possibilities, float weight){
		for(Entry<KnownMarkovNode<T>, Float> possibleEntry : possibilities.entrySet() ){
			float possibilityWeight = (possibleEntry.getValue() / weight);
			totalVotes += possibilityWeight;
			if(votes.containsKey(possibleEntry.getKey())){	// Add to existing votes
				votes.put(possibleEntry.getKey(), votes.get(possibleEntry.getKey()) + possibilityWeight );
			}else{	// Cast the first vote
				votes.put(possibleEntry.getKey(), possibilityWeight );
			}
		}
	}
	
	public void decideIdentity(){
		float maxWeight = 0.0f;
		KnownMarkovNode<T> maxIdentity = null;
		// Find the possibility with the highest weight
		for(Entry<KnownMarkovNode<T>, Float> possibility : votes.entrySet() ){
			if(possibility.getValue() > maxWeight){
				maxWeight = possibility.getValue();
				maxIdentity = possibility.getKey();
			}
		}
		
		currentIdentity = maxIdentity;
		currentWeight = maxWeight / totalVotes;
		
		System.out.println(this +" decided to be " + currentIdentity + " with probability " + currentWeight);
		
		// Reset votes to get ready for next iteration
		votes = baseVotes;
		totalVotes = baseTotal;
	}
	
	public void notifyNeighbors(){
		// Given our current tentative identity, what do we think each of our unknown neighbors should be?
		if(currentIdentity != null){
			for(Entry<MarkovCoordinate, UnknownMarkovNode<T>> uNeighbor : unkNeighbors.entrySet() ){
				if(currentIdentity.atOffset.containsKey(uNeighbor.getKey())){
					uNeighbor.getValue().addSuggestions(currentIdentity.atOffset.get(uNeighbor.getKey()), (float) currentWeight);
				}
			}
		}
	}
	
	
	public void initializeIdentity(){
		// What does the known neighbors think this node should be?
		for(Entry<MarkovCoordinate, KnownMarkovNode<T>> kNeighbor : knownNeighbors.entrySet() )
		{
			MarkovCoordinate dBack = new MarkovCoordinate(distanceBack(kNeighbor.getKey().coords));
			HashMap<KnownMarkovNode<T>, Float> kSuggestion =kNeighbor.getValue().atOffset.get( dBack );
			System.out.println(""+kNeighbor.getValue() + " suggestion at " + dBack + " is " + kSuggestion);
			if(kSuggestion != null)
				addSuggestions(kSuggestion, 1.0f  );
		}		
		
		baseVotes = new HashMap<KnownMarkovNode<T>, Float>(votes);
		baseTotal = totalVotes;
	}
	
	public void addConnection(MarkovNode<T> node, float weight, int... distance){
		if(node instanceof KnownMarkovNode){
			knownNeighbors.put(new MarkovCoordinate(distance), (KnownMarkovNode<T>)node);
		}else{
			unkNeighbors.put(new MarkovCoordinate(distance), (UnknownMarkovNode<T>) node);
		}
	}

}
