import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.LinkedList;


public class MarkovNode<T> {
	T value;
	int[] location;
	MarkovRandomField<T> mrf;
	
	// LinkedList / PriorityQueue
	//LinkedList<MarkovConnection<T>> connections = new LinkedList<MarkovConnection<T>>();
	HashMap<MarkovNode<T>, MarkovConnection<T>> connections = new HashMap<MarkovNode<T>, MarkovConnection<T>>();
	LinkedList<MarkovConnection<T>> equalities = new LinkedList<MarkovConnection<T>>(); 
	
	public MarkovNode(T val, MarkovRandomField<T> f, int... loc){
		value = val;
		location = loc;
		mrf = f;
	}
	
	public void addConnection(MarkovNode<T> node, float weight){
		MarkovConnection<T> conn = connections.get(node);
		
		if(conn == null){ // Create a new connection
			int[] distance = new int[mrf.DIMENSIONS];
			for(int i = 0; i < mrf.DIMENSIONS; i++){
				distance[i] = node.location[i] - location[i];
			}
			
			conn = new MarkovConnection<T>(this, node, mrf, 0, distance);
			connections.put(node, conn);
			
			for(MarkovConnection<T> eq : equalities){
				assert eq.weight >= 0.0 && eq.weight <= 1.0 : "Equality weights should be bounded between 0 and 1";
				eq.destination.addConnection(node, weight * eq.weight);
			}
		}
		conn.weight += weight;
	}
	
	public void addEquality(MarkovNode<T> node){
		for(MarkovConnection<T> eq: equalities){
			
		}
	}
}
