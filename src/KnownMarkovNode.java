import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map.Entry;


public class KnownMarkovNode<T> extends MarkovNode<T>{
	T value;
	
	// Keep a count of how many times we see a particular node at a particular distance 
	TreeMap<MarkovCoordinate, HashMap<KnownMarkovNode<T>, Float>> atOffset = new TreeMap<MarkovCoordinate, HashMap<KnownMarkovNode<T>, Float>>();
	
	// Keep track of the sum at each location for convenience when calculating normalized probability from atOffset
	TreeMap<MarkovCoordinate, Double> locationProbSum = new TreeMap<MarkovCoordinate, Double>();	
	
	public KnownMarkovNode(T val){
		value = val;
	}
	
	// Observe that node is distance from 'this' with given weight (generally 1)
	public void addConnection(MarkovNode<T> node, float weight, int... distance){
		if(node instanceof KnownMarkovNode){
			KnownMarkovNode<T> knownNode = (KnownMarkovNode<T>)  node;
			
			MarkovCoordinate iDistance = new MarkovCoordinate(distance);
			
			HashMap<KnownMarkovNode<T>, Float> atOffD = atOffset.get(iDistance);
			if(atOffD == null){	// Nothing till now has been observed at that offset. 
				atOffD = new HashMap<KnownMarkovNode<T>, Float>();
			}
			
			if(atOffD.containsKey(node)){
				atOffD.put(knownNode, atOffD.get(node) + weight);
			}else{
				atOffD.put(knownNode, weight);
			}
			
			atOffset.put(iDistance, atOffD);
			
			// Add it to sum
			if(locationProbSum.containsKey(iDistance)){	
				locationProbSum.put(iDistance, locationProbSum.get(iDistance) + weight);
			}else{
				locationProbSum.put(iDistance, (double) weight);
			}
		}
		
	}
		
	public void printOffsets(){
		for(Entry<MarkovCoordinate, HashMap<KnownMarkovNode<T>, Float>> c : atOffset.entrySet()){
			System.out.println(this + " at " + c.getKey() + " : " + c.getValue());
		}
	}
	
	public String toString(){
		return "<" + value.toString() + ">"; 
	}
	
	
}
