import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.LinkedList;


// a b c d e
// Node a
//  Connection to b c d e
// a[+1] = edge to b, with weight 1. 


public class MarkovNode<T> {
	T value;
	MarkovRandomField<T> mrf;
	
	// LinkedList / PriorityQueue
	//LinkedList<MarkovConnection<T>> connections = new LinkedList<MarkovConnection<T>>();
	//HashMap<MarkovNode<T>, MarkovConnection<T>> connections = new HashMap<MarkovNode<T>, MarkovConnection<T>>();
	
	HashMap<IntBuffer, HashMap<MarkovNode<T>, MarkovConnection<T>>> connections = new HashMap<IntBuffer, HashMap<MarkovNode<T>, MarkovConnection<T>>>(); 
	
	//LinkedList<MarkovConnection<T>> equalities = new LinkedList<MarkovConnection<T>>(); 
	
	public MarkovNode(T val, MarkovRandomField<T> f){
		value = val;
		mrf = f;
//		connections = new HashMap<MarkovNode<T>, MarkovConnection<T>>[3];
	}
	
	public int[] distanceBack(int... distance){
		int[] dBack = new int[distance.length];
		for(int i = 0; i < distance.length; i++)
			dBack[i] = -1 * distance[i];
		return dBack;
	}
	
	public void addConnection(MarkovNode<T> node, float weight, int... distance){
		HashMap<MarkovNode<T>, MarkovConnection<T>> atD = getConnectionsAtDistance(distance);
		MarkovConnection<T> conn = atD.get(node);
		
		if(conn == null){ // Create a new connection
			conn = new MarkovConnection<T>(this, node, mrf, 0);
			atD.put(node, conn);
			node.addConnection(this, conn, distanceBack(distance));
			
			//connections.put(node, conn);
/*			for(MarkovConnection<T> eq : equalities){
				assert eq.weight >= 0.0 && eq.weight <= 1.0 : "Equality weights should be bounded between 0 and 1";
				eq.destination.addConnection(node, weight * eq.weight);
			} */
		}
		// Increment the weight of an existing connection. 
		conn.weight += weight;
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
}
