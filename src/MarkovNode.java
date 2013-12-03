import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

public abstract class MarkovNode<T> {
	
	TreeMap<MarkovCoordinate, HashMap<MarkovNode<T>, MarkovConnection<T>>> connections = new TreeMap<MarkovCoordinate, HashMap<MarkovNode<T>, MarkovConnection<T>>>();
	TreeMap<MarkovCoordinate, HashMap<KnownMarkovNode<T>, Float>> locationProbabilities = new TreeMap<MarkovCoordinate, HashMap<KnownMarkovNode<T>, Float>>();
	TreeMap<MarkovCoordinate, Double> locationProbSum = new TreeMap<MarkovCoordinate, Double>();
	TreeMap<MarkovCoordinate, KnownMarkovNode<T>> locationMaxVal = new TreeMap<MarkovCoordinate, KnownMarkovNode<T>>();
	TreeMap<MarkovCoordinate, Float> locationMaxProb = new TreeMap<MarkovCoordinate, Float>();
	
	public int[] distanceBack(int... distance){
		int[] dBack = new int[distance.length];
		for(int i = 0; i < distance.length; i++)
			dBack[i] = -1 * distance[i];
		return dBack;
	}	
	
	public void addConnection(MarkovNode<T> node, float weight, int... distance){
		System.out.println(this.toString() + " Adding connection to " + node.toString() + " ("+weight+")");
		HashMap<MarkovNode<T>, MarkovConnection<T>> atD = getConnectionsAtDistance(distance);
		MarkovConnection<T> conn = atD.get(node);
		
		if(conn == null){ // Create a new connection
			conn = new MarkovConnection<T>(this, node, 0);
			atD.put(node, conn);
			int[] dBack = distanceBack(distance);
			node.addConnection(this, conn, dBack);
			node.addWeightAtDistance(weight, dBack);
		}
		// Increment the weight of an existing connection. 
		conn.weight += weight;
		addWeightAtDistance(weight, distance);
		
		MarkovCoordinate iDist = new MarkovCoordinate(distance);
		
		if(!locationMaxProb.containsKey(iDist))
			locationMaxProb.put(iDist, 0.0f);
		
		if(locationMaxProb.get(iDist) < conn.weight && node instanceof KnownMarkovNode){
			locationMaxProb.put(iDist, (float) conn.weight);
			locationMaxVal.put(iDist, (KnownMarkovNode<T>) node);
		}
	}
	
	public void addConnection(MarkovNode<T> node, MarkovConnection<T> conn, int... distance){
		HashMap<MarkovNode<T>, MarkovConnection<T>> atD = getConnectionsAtDistance(distance);
		atD.put(node, conn);
	}
	
	public HashMap<MarkovNode<T>, MarkovConnection<T>> getConnectionsAtDistance(int... distance){
		MarkovCoordinate iDistance = new MarkovCoordinate(distance);
		HashMap<MarkovNode<T>, MarkovConnection<T>> atDist = connections.get(iDistance);
		if(atDist == null){
			atDist = new HashMap<MarkovNode<T>, MarkovConnection<T>>();
			connections.put(iDistance, atDist);
		}
		return atDist; 
	}
	
	public void addWeightAtDistance(float w, int... distance){
		MarkovCoordinate iDistance = new MarkovCoordinate(distance);
		if(locationProbSum.containsKey(iDistance)){	 
			locationProbSum.put(iDistance, locationProbSum.get(iDistance) + w);
		}else{
			locationProbSum.put(iDistance, (double) w);
		}
		
	} 
	
	// calculate the weights
	public void compileProbabilities(){
		//HashMap<IntBuffer, ArrayList<UnknownMarkovNode<T>>> unks = new HashMap<IntBuffer, ArrayList<UnknownMarkovNode<T>>>();
		for(MarkovCoordinate coord: connections.keySet()){
			HashMap<MarkovNode<T>, MarkovConnection<T>> coordConnections = connections.get(coord);
			
			// What is the probability that T is the next node at the given coordinate?
			HashMap<KnownMarkovNode<T>, Float> coordProb = new HashMap<KnownMarkovNode<T>, Float>();
			
			if(locationProbSum.containsKey(coord)){
				double probabilitiesSum = locationProbSum.get(coord);
				if(probabilitiesSum > 0){
					boolean unkAtLoc = false;
					for(MarkovNode<T> node: coordConnections.keySet()){
						if(node instanceof KnownMarkovNode){
							coordProb.put( ( (KnownMarkovNode<T>) node), (float) (coordConnections.get(node).weight / probabilitiesSum));					
						}else{
							unkAtLoc = true;
/*							if(unks.containsKey(coord)){
								unks.get(coord).add((UnknownMarkovNode<T>) node);
							}else{
								ArrayList<UnknownMarkovNode<T>> subUnk = new ArrayList<UnknownMarkovNode<T>>();
								subUnk.add((UnknownMarkovNode<T>) node);
								unks.put(coord, subUnk);
							} */
						}
					}
					// Save it only if there's an unknown at this offset and we know what else should go there... 
					if(unkAtLoc && coordProb.size() > 0)
						locationProbabilities.put(new MarkovCoordinate(coord.coords), coordProb);
				}
			}
		}
	}
	
	public abstract void initializeState();
		
}

class KnownMarkovNode<T> extends MarkovNode<T>{
	T value;
	
	public KnownMarkovNode(T val){
		value = val;
	}

	public String toString(){
		return "<" + value.toString() + ">"; 
	}
	
	public void locs(){
		for(MarkovCoordinate b : connections.keySet()){
			System.out.println("Distance " + Arrays.toString(b.coords));
		} 
	}
	
	// If known node, lets all of it's neighbors know 
	public void initializeState(){
	/*	for (Entry<IntBuffer, HashMap<T, Float>> entry : locationProbabilities.entrySet()) {
			
		} */
	}
	
	
}

class UnknownMarkovNode<T> extends MarkovNode<T>{
	
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
		ArrayList<HashMap<KnownMarkovNode<T>, Float>> suggestionsList = new ArrayList<HashMap<KnownMarkovNode<T>, Float>>();
		
		
		// Just find the highest probability vote for it. 
		// assert compile for each node has already been called before this. 
		for(MarkovCoordinate coord: connections.keySet()){
			MarkovCoordinate revCoord = new MarkovCoordinate(distanceBack(coord.coords));
			HashMap<MarkovNode<T>, MarkovConnection<T>> coordConnections = connections.get(coord);
			
			for(MarkovNode<T> node: coordConnections.keySet()){
				//node.getConnectionsAtDistance(revCoord);
				if(node.locationProbabilities.containsKey(revCoord)){
					System.out.println(node.toString() + " at " + revCoord + " suggests " + node.locationProbabilities.get(revCoord));					
					suggestionsList.add(node.locationProbabilities.get(revCoord));
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
				
				HashMap<KnownMarkovNode<T>, Float> locProbs = currentState.locationProbabilities.get(entry.getValue());
				System.out.println("Current state " + currentState + " at " + entry.getValue() + " is " + locProbs);
				currentState.locs();
				
				// We know what's going to be there at that unknown location (or think we do)
				// so tell em
				if(locProbs != null && locProbs.size() > 0){
					entry.getKey().inbox.add(new MarkovMessage<T>(locProbs, currentStateConfidence));
					System.out.println("Suggesting  to " + entry.getKey());
				}
			}
		}
	}
	
	ArrayList<MarkovMessage<T>> inbox = new ArrayList<MarkovMessage<T>>();
	
	public void initializeState(){
		//for()
	}
	
	public void solve(){
		currentIteration++;
	}

}

class MarkovMessage<T>{
	//KnownMarkovNode<T> val;
	HashMap<KnownMarkovNode<T>, Float> suggestions;
	float confidence;
	
	public MarkovMessage(HashMap<KnownMarkovNode<T>, Float> sug, float c){
		suggestions = sug;
		confidence = c;
	}
}

/*
System.out.println("Coord is " + Arrays.toString(coord.array()));

for(IntBuffer b : locationProbSum.keySet()){
	System.out.println("Locatino prob sum key " + Arrays.toString(b.array()) + " is " + locationProbSum.get(b));				
}
*/

class MarkovCoordinate implements Comparable<MarkovCoordinate>{
	int[] coords;
	public MarkovCoordinate(int... c){
		coords = Arrays.copyOf(c, c.length);
	}
	
	public boolean equals(Object o){
		if(o instanceof MarkovCoordinate){
			MarkovCoordinate m = (MarkovCoordinate) o;
			return Arrays.equals(m.coords, coords);
		}
		return false;
	}

	@Override
	public int compareTo(MarkovCoordinate m) {
		if(m.coords.length == coords.length){
			for(int i = 0; i < coords.length; i++){
				if(m.coords[i] < coords[i])
					return -1;
				else if(m.coords[i] > coords[i])
					return 1;
			}
			return 0;
		}
		return -1;
	}
	
	public String toString(){
		return Arrays.toString(coords);
	}
}

