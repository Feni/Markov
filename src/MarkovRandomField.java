import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;


public class MarkovRandomField<T> {
	public int DIMENSIONS;	// 1-D, 2-D, etc.
	public int[] size;
	
	HashMap<T, MarkovNode<T>> nodes = new HashMap<T, MarkovNode<T>>();
	
	//ArrayList<MarkovNode<T>> sequence = new LinkedList<MarkovNode<T>>();
	//@SuppressWarnings("rawtypes")
	//ArrayList<> sequence;// = new ArrayList();
	HashMap<IntBuffer, MarkovNode<T>> sequence = new HashMap<IntBuffer, MarkovNode<T>>();
	
	int totalElements = 0;
	int knownNodeCount = 0;
	int unknownNodeCount = 0;
	double totalConfidence = 0;	// maximize this. Only counted for unknown guesses
	
	int[] currentPos;
	
	// of a given Width and Height
	public MarkovRandomField(int... s){
		size = s;
		DIMENSIONS = s.length;
		currentPos = new int[DIMENSIONS];
		for(int i = 0; i < currentPos.length; i++)
			currentPos[i] = 0;
	}
	
	public MarkovNode<T> newNode(T val){
		totalElements += 1;
		MarkovNode<T> n = nodes.get(val);
		if(n == null){						// Node doesn't exist yet. Create it. 
			n = new KnownMarkovNode<T>(val);
			nodes.put(val, n);
			System.out.println("Creating new "+n);
		}
		return n;
	}
	
	
	public int[] diff(int[] a, int[] b){
		assert a.length == b.length: "Arrays have to be of the same length to difference";
		int[] difference = new int[a.length];
		for(int i = 0; i < a.length; i++)
			difference[i] = a[i] - b[i];
		return difference;
	}
	
	public void add(MarkovNode<T> newNode){
		// Connect it up with existing nodes
		if(newNode instanceof KnownMarkovNode){
			knownNodeCount++;
//			System.out.println(sequence.keySet());
			for(IntBuffer coord: sequence.keySet()){
				//MarkovNode n = ;
				//System.out.println("n is " + Arrays.toString(coord.array()));
				if(sequence.get(coord) instanceof KnownMarkovNode){
					KnownMarkovNode<T> n = (KnownMarkovNode<T>) sequence.get(coord);
					int[] distance = diff(currentPos, coord.array());
					//System.out.println("diff between " + newNode + " and " + n + " is " + Arrays.toString(distance));
					n.addConnection(newNode, 1.0f, distance);
				}
			}
		}else{
			unknownNodeCount++;
		}
		
		
		// THEN add it (so that it doesn't get connected with itself)
		sequence.put(IntBuffer.wrap(currentPos.clone()), newNode);
		totalElements += 1;		
		//System.out.println("Current pos is now " + Arrays.toString(currentPos));
		/*for(MarkovNode<T> existing: sequence){
			int[] fwdDistance = new int[DIMENSIONS];
			int[] backDistance = new int[DIMENSIONS];
			for(int i = 0; i < DIMENSIONS; i++){
				fwdDistance[i] = newNode.location[i] - existing.location[i];
				backDistance[i] = -1 * fwdDistance[i];
			}
			MarkovConnection<T> connectionToNew = new MarkovConnection<T>(existingNode, newNode, this, )
		} */
		
		for(int i = 0; i < DIMENSIONS; i++){
			if(currentPos[i] == size[i] - 1){
				currentPos[i] = 0;	// Carry
			}else{
				currentPos[i] = currentPos[i] + 1;
				break;
			}
		}		
	}
	
}
