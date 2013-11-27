import java.util.HashMap;
import java.util.LinkedList;


public class MarkovRandomField<T> {
	public int DIMENSIONS;
	HashMap<T, MarkovNode<T>> nodes = new HashMap<T, MarkovNode<T>>();
	LinkedList<MarkovNode<T>> sequence = new LinkedList<MarkovNode<T>>();
	
	public MarkovRandomField(int dimm){
		DIMENSIONS = dimm;
	}
	
	public MarkovNode newNode(T val){
		MarkovNode<T> n = nodes.get(val);
		if(n == null){	// Node doesn't exist yet. Create it. 
			n = new MarkovNode(n);
		}
		return n;
	}
	
	public void add(MarkovNode<T> newNode){
		for(MarkovNode<T> existing: sequence){
			int[] fwdDistance = new int[DIMENSIONS];
			int[] backDistance = new int[DIMENSIONS];
			for(int i = 0; i < DIMENSIONS; i++){
				fwdDistance[i] = newNode.location[i] - existing.location[i];
				backDistance[i] = -1 * fwdDistance[i];
			}
			
			MarkovConnection<T> connectionToNew = new MarkovConnection<T>(existingNode, newNode, this, )
			
		}
	}
	
}
