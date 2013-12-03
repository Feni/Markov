import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;


public class UnknownMarkovNode<T> extends MarkovNode<T>{
	
	// First parameter is the guess for this node's state. 
	// Second parameter is the probability that this is 
	//HashMap<T, HashMap<T, Float>> votes = new HashMap<T, HashMap<T, Float>>();
	
	HashMap<KnownMarkovNode<T>, Float> baseState = new HashMap<KnownMarkovNode<T>, Float>();
	
	HashMap<UnknownMarkovNode<T>, MarkovCoordinate> unkNeighbors = new HashMap<UnknownMarkovNode<T>, MarkovCoordinate>();
	
	int currentIteration = 0;
	
/*	public void guess(int iteration){
		
	} */
	
	public String toString(){
		return "?"; 
	}	
	
	KnownMarkovNode<T> currentState;
	float currentStateConfidence = 0.0f;
	
	public HashMap<KnownMarkovNode<T>, Float> aggregateSuggestions(ArrayList<HashMap<KnownMarkovNode<T>, Float>> suggestionsList){
		HashMap<KnownMarkovNode<T>, Float> sums = new HashMap<KnownMarkovNode<T>, Float>();
		
		for(HashMap<KnownMarkovNode<T>, Float> suggestion: suggestionsList){
			for (Entry<KnownMarkovNode<T>, Float> entry : suggestion.entrySet()) {
				if(sums.containsKey(entry.getKey())){
					sums.put((KnownMarkovNode<T>) entry.getKey(), sums.get(entry.getKey()) + entry.getValue());
				}else{			    
					sums.put((KnownMarkovNode<T>) entry.getKey(), entry.getValue());
				}
			}
		}
		
		System.out.println("Final sums are " );
		double maxPts = 0.0;
		double sumPts = 0.0;
		KnownMarkovNode<T> maxVal = null;
		
		for (Entry<KnownMarkovNode<T>, Float> entry : sums.entrySet()) {
			System.out.println("" + entry.getKey() + " : " + entry.getValue() );
			sumPts += entry.getValue();
			if(entry.getValue() > maxPts){
				maxPts = entry.getValue();
				maxVal = entry.getKey();
			}
		}
		
		currentState = maxVal;
		currentStateConfidence = (float) (maxPts / sumPts);

		System.out.println("Current state is " + currentState + " with confidence " + currentStateConfidence);
		
		return sums;
	}
	
	public void simpleBestGuess(){
		
		/*ArrayList<HashMap<KnownMarkovNode<T>, Float>> suggestionsList = new ArrayList<HashMap<KnownMarkovNode<T>, Float>>();
		
		// Just find the highest probability vote for it. 
		// assert compile for each node has already been called before this. 
		for(MarkovCoordinate coord: connections.keySet()){
			MarkovCoordinate revCoord = new MarkovCoordinate(distanceBack(coord.coords));
			HashMap<MarkovNode<T>, MarkovConnection<T>> coordConnections = connections.get(coord);
			
			for(MarkovNode<T> node: coordConnections.keySet()){
				//node.getConnectionsAtDistance(revCoord);
				if(node.atOffset.containsKey(revCoord)){
					System.out.println(node.toString() + " at " + revCoord + " suggests " + node.atOffset.get(revCoord));					
					suggestionsList.add(node.atOffset.get(revCoord));
				} else if(node instanceof UnknownMarkovNode){
					unkNeighbors.put((UnknownMarkovNode<T>) node, revCoord );
					System.out.println("rev coord is " + revCoord);
					System.out.println("Unk neighbor "+node + " at " + coord);
				}
			}
		}
		
		baseState = aggregateSuggestions(suggestionsList);
		
		if(currentState != null){
			for (Entry<UnknownMarkovNode<T>, MarkovCoordinate> entry : unkNeighbors.entrySet()) {
				
				HashMap<KnownMarkovNode<T>, Float> locProbs = currentState.atOffset.get(entry.getValue());
				System.out.println("Current state " + currentState + " at " + entry.getValue() + " is " + locProbs);
				currentState.locs();
				
				// We know what's going to be there at that unknown location (or think we do)
				// so tell em
				if(locProbs != null && locProbs.size() > 0){
					entry.getKey().inbox.add(new MarkovMessage<T>(locProbs, currentStateConfidence));
					System.out.println("Suggesting  to " + entry.getKey());
				}
			}
		} */
	}
	
	ArrayList<MarkovMessage<T>> inbox = new ArrayList<MarkovMessage<T>>();
	
	public void initializeState(){
		//for()
	}
	
	public void solve(){
		currentIteration++;
	}

}
