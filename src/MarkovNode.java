import java.nio.IntBuffer;
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
		System.out.println(this.toString() + " Adding connection to " + node.toString());
		HashMap<MarkovNode<T>, MarkovConnection<T>> atD = getConnectionsAtDistance(distance);
		MarkovConnection<T> conn = atD.get(node);
		
		if(conn == null){ // Create a new connection
			conn = new MarkovConnection<T>(this, node, 0);
			atD.put(node, conn);
			int[] dBack = distanceBack(distance);
			node.addConnection(this, conn, dBack);
			if(node instanceof KnownMarkovNode)
				node.addWeightAtDistance(weight, dBack);
		}
		// Increment the weight of an existing connection. 
		conn.weight += weight;
		if(node instanceof KnownMarkovNode)
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
		for(IntBuffer coord: connections.keySet()){
			HashMap<MarkovNode<T>, MarkovConnection<T>> coordConnections = connections.get(coord);
			
			// What is the probability that T is the next node at the given coordinate?
			HashMap<T, Float> coordProb = new HashMap<T, Float>();
			
			if(locationProbSum.containsKey(coord)){
				double probabilitiesSum = locationProbSum.get(coord);
				for(MarkovNode<T> node: coordConnections.keySet()){
					if(node instanceof KnownMarkovNode){
						coordProb.put( ( (KnownMarkovNode<T>) node).value, (float) (coordConnections.get(node).weight / probabilitiesSum));					
					}
				}
				locationProbabilities.put(coord, coordProb);
			}
		}
	}
	
}

class KnownMarkovNode<T> extends MarkovNode<T>{
	T value;
	
	public KnownMarkovNode(T val){
		value = val;
	}

	public String toString(){
		return "<" + value.toString() + ">"; 
	}
}

class UnknownMarkovNode<T> extends MarkovNode<T>{
	
	// First parameter is the guess for this node's state. 
	// Second parameter is the probability that this is 
	HashMap<T, HashMap<T, Float>> votes = new HashMap<T, HashMap<T, Float>>();
	
	int currentIteration = 0;
	
	public void guess(int iteration){
		
	}
	
	public String toString(){
		return "?"; 
	}	
	
	
	public void simpleBestGuess(){
		
		HashMap<T, Float> sums = new HashMap<T, Float>();
		
		// Just find the highest probability vote for it. 
		// assert compile for each node has already been called before this. 
		for(IntBuffer coord: connections.keySet()){
			IntBuffer revCoord = IntBuffer.wrap(distanceBack(coord.array()));
			HashMap<MarkovNode<T>, MarkovConnection<T>> coordConnections = connections.get(coord);
			
			for(MarkovNode<T> node: coordConnections.keySet()){
				//node.getConnectionsAtDistance(revCoord);
				System.out.println(node.toString() + " at " + Arrays.toString(revCoord.array()) + " suggests " + node.locationProbabilities.get(revCoord));
				
				
				if(node.locationProbabilities.containsKey(revCoord)){
					HashMap<T, Float> suggestionsList = node.locationProbabilities.get(revCoord);
					
					for (Entry<T, Float> entry : suggestionsList.entrySet()) {
						if(sums.containsKey(entry.getKey())){
							sums.put(entry.getKey(), sums.get(entry.getKey()) + entry.getValue());
						}else{			    
							sums.put(entry.getKey(), entry.getValue());
						}
					}
				}
			}
			//locationProbabilities.put(coord, coordProb);
		}
		
		System.out.println("Final sums are " );
		for (Entry<T, Float> entry : sums.entrySet()) {
			System.out.println("" + entry.getKey() + " : " + entry.getValue() );
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

