import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;


public class KnownMarkovNode<T> extends MarkovNode<T>{
	T value;
	
	public KnownMarkovNode(T val){
		value = val;
	}

	public String toString(){
		return "<" + value.toString() + ">"; 
	}
	
	public void locs(){
		//for(MarkovCoordinate b : connections.keySet()){
//			System.out.println("Distance " + Arrays.toString(b.coords));
		//} 
	}
	
	// If known node, lets all of it's neighbors know 
	public void initializeState(){
	/*	for (Entry<IntBuffer, HashMap<T, Float>> entry : locationProbabilities.entrySet()) {
			
		} */
	}

	
	public void printOffsets(){
		for(Entry<MarkovCoordinate, HashMap<KnownMarkovNode<T>, Float>> c : atOffset.entrySet()){
			System.out.println(this + " at " + c.getKey() + " : " + c.getValue());
		}
	}
	
	
}
