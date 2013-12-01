import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;


public abstract class MarkovNode<T> {
	
	TreeMap<IntBuffer, HashMap<MarkovNode<T>, MarkovConnection<T>>> connections = new TreeMap<IntBuffer, HashMap<MarkovNode<T>, MarkovConnection<T>>>();
	
	HashMap<IntBuffer, HashMap<T, Float>> locationProbabilities = new HashMap<IntBuffer, HashMap<T, Float>>();
	
	HashMap<IntBuffer, Double> locationProbSum = new HashMap<IntBuffer, Double>();
	

	
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
	}
	
	public void addConnection(MarkovNode<T> node, MarkovConnection<T> conn, int... distance){
		HashMap<MarkovNode<T>, MarkovConnection<T>> atD = getConnectionsAtDistance(distance);
		atD.put(node, conn);
	}
	
	public HashMap<MarkovNode<T>, MarkovConnection<T>> getConnectionsAtDistance(int... distance){
		IntBuffer iDistance = IntBuffer.wrap(distance);
		HashMap<MarkovNode<T>, MarkovConnection<T>> atDist = connections.get(iDistance);
		if(atDist == null){
			atDist = new HashMap<MarkovNode<T>, MarkovConnection<T>>();
			connections.put(iDistance, atDist);
		}
		return atDist; 
	}
	
	public void addWeightAtDistance(float w, int... distance){
		IntBuffer iDistance = IntBuffer.wrap(distance);
		if(locationProbSum.containsKey(iDistance)){	 
			locationProbSum.put(iDistance, locationProbSum.get(iDistance) + w);
		}else{
			locationProbSum.put(iDistance, (double) w);
		}
	}
	
	// calculate the weights
	public void compileProbabilities(){
		//HashMap<IntBuffer, ArrayList<UnknownMarkovNode<T>>> unks = new HashMap<IntBuffer, ArrayList<UnknownMarkovNode<T>>>();
		for(IntBuffer coord: connections.keySet()){
			HashMap<MarkovNode<T>, MarkovConnection<T>> coordConnections = connections.get(coord);
			
			// What is the probability that T is the next node at the given coordinate?
			HashMap<T, Float> coordProb = new HashMap<T, Float>();
			
			if(locationProbSum.containsKey(coord)){
				double probabilitiesSum = locationProbSum.get(coord);
				if(probabilitiesSum > 0){
					boolean unkAtLoc = false;
					for(MarkovNode<T> node: coordConnections.keySet()){
						if(node instanceof KnownMarkovNode){
							coordProb.put( ( (KnownMarkovNode<T>) node).value, (float) (coordConnections.get(node).weight / probabilitiesSum));					
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
						locationProbabilities.put(coord, coordProb);
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
	
	HashMap<T, Float> baseState = new HashMap<T, Float>();
	
	ArrayList<UnknownMarkovNode<T>> unkNeighbors = new ArrayList<UnknownMarkovNode<T>>();
	
	int currentIteration = 0;
	
/*	public void guess(int iteration){
		
	} */
	
	public String toString(){
		return "?"; 
	}	
	
	T currentState;
	float currentStateConfidence = 0.0f;
	
	public void simpleBestGuess(){
		HashMap<T, Float> sums = new HashMap<T, Float>();
		
		// Just find the highest probability vote for it. 
		// assert compile for each node has already been called before this. 
		for(IntBuffer coord: connections.keySet()){
			IntBuffer revCoord = IntBuffer.wrap(distanceBack(coord.array()));
			HashMap<MarkovNode<T>, MarkovConnection<T>> coordConnections = connections.get(coord);
			
			for(MarkovNode<T> node: coordConnections.keySet()){
				//node.getConnectionsAtDistance(revCoord);
				if(node.locationProbabilities.containsKey(revCoord)){
					System.out.println(node.toString() + " at " + Arrays.toString(revCoord.array()) + " suggests " + node.locationProbabilities.get(revCoord));					
					HashMap<T, Float> suggestionsList = node.locationProbabilities.get(revCoord);
					
					for (Entry<T, Float> entry : suggestionsList.entrySet()) {
						if(sums.containsKey(entry.getKey())){
							sums.put(entry.getKey(), sums.get(entry.getKey()) + entry.getValue());
						}else{			    
							sums.put(entry.getKey(), entry.getValue());
						}
					}
				} else if(node instanceof UnknownMarkovNode){
					unkNeighbors.add((UnknownMarkovNode<T>) node);
					System.out.println("Unk neighbor "+node);
				}
			}
			//locationProbabilities.put(coord, coordProb);
		}
		
		System.out.println("Final sums are " );
		double maxPts = 0.0;
		double sumPts = 0.0;
		T maxVal = null;
		
		for (Entry<T, Float> entry : sums.entrySet()) {
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
		baseState = sums;
	}
	
	ArrayList<MarkovMessage<T>> inbox = new ArrayList<MarkovMessage<T>>();
	
	public void initializeState(){
		//for()
	}
	
	public void solve(){
		currentIteration++;
		for(UnknownMarkovNode<T> u: unkNeighbors){
			if(u.currentState != null){
	//			u.currentState
			}
		}
	}
		
	
}

class MarkovMessage<T>{
	T val;
	float confidence;
	
	public MarkovMessage(MarkovNode<T> t){
		
	}
}

/*
System.out.println("Coord is " + Arrays.toString(coord.array()));

for(IntBuffer b : locationProbSum.keySet()){
	System.out.println("Locatino prob sum key " + Arrays.toString(b.array()) + " is " + locationProbSum.get(b));				
}


*/

