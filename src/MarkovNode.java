import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

public abstract class MarkovNode<T> {
	// Each markov node is connected to all other markov nodes
	// TreeMap<MarkovCoordinate, HashMap<MarkovNode<T>, MarkovConnection<T>>> connections = new TreeMap<MarkovCoordinate, HashMap<MarkovNode<T>, MarkovConnection<T>>>();
	
	TreeMap<MarkovCoordinate, HashMap<KnownMarkovNode<T>, Float>> atOffset = new TreeMap<MarkovCoordinate, HashMap<KnownMarkovNode<T>, Float>>();
	TreeMap<MarkovCoordinate, Double> locationProbSum = new TreeMap<MarkovCoordinate, Double>();
	
	public static int[] distanceBack(int... distance){
		int[] dBack = new int[distance.length];
		for(int i = 0; i < distance.length; i++)
			dBack[i] = -1 * distance[i];
		return dBack;
	}	
	
	/*
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
	}*/
	
	public void addConnection(KnownMarkovNode<T> node, float weight, int... distance){
		MarkovCoordinate iDistance = new MarkovCoordinate(distance);
		
		HashMap<KnownMarkovNode<T>, Float> atOffD = atOffset.get(iDistance);
		if(atOffD == null){	// Nothing till now has been observed at that offset. 
			atOffD = new HashMap<KnownMarkovNode<T>, Float>();
		}
		
		if(atOffD.containsKey(node)){
			atOffD.put(node, atOffD.get(node) + weight);
		}else{
			atOffD.put(node, weight);
		}
		
		atOffset.put(iDistance, atOffD);
		
		// Add it to sum
		if(locationProbSum.containsKey(iDistance)){	
			locationProbSum.put(iDistance, locationProbSum.get(iDistance) + weight);
		}else{
			locationProbSum.put(iDistance, (double) weight);
		}
		
	}
	
	// calculate the weights
	public abstract void initializeState();
	
		
}
