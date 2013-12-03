import java.util.ArrayList;
import java.util.HashMap;

public class MarkovRandomField<T> {
	public int DIMENSIONS;	// 1-D, 2-D, etc.
	public int[] size;
	
	// We have one specific MarkovNode for each discrete value of T that can appear. 
	// i.e. only one MarkovNode to represent all "Hello" nodes. 
	HashMap<T, MarkovNode<T>> nodes = new HashMap<T, MarkovNode<T>>();
	// Keeps track of all of the unknown variables
	ArrayList<MarkovCoordinate> unknowns = new ArrayList<MarkovCoordinate>();
	// Keeps track of where each node is located in the image/sequence
	HashMap<MarkovCoordinate, MarkovNode<T>> sequence = new HashMap<MarkovCoordinate, MarkovNode<T>>();
	
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
		MarkovNode<T> n = nodes.get(val);
		if(n == null){						// Node doesn't exist yet. Create it. 
			n = new KnownMarkovNode<T>(val);
			nodes.put(val, n);
		}
		return n;
	}
	
	public UnknownMarkovNode<T> newUnknown(){
		return new UnknownMarkovNode<T>();
	}
	
	public int[] diff(int[] a, int[] b){
		assert a.length == b.length: "Arrays have to be of the same length to difference";
		int[] difference = new int[a.length];
		for(int i = 0; i < a.length; i++)
			difference[i] = a[i] - b[i];
		return difference;
	}
	
	public void add(MarkovNode<T> newNode){
		MarkovCoordinate newNodeCoord = new MarkovCoordinate(currentPos.clone());
		
		// Keep track of how many nodes we have to solve for
		if(newNode instanceof UnknownMarkovNode){
			unknowns.add(newNodeCoord);
		}
		
		// Relate it to all existing nodes
		// TODO: Only do this for nodes at a certain distance from currentNode
			
			for(MarkovCoordinate coord: sequence.keySet()){
				//sequence.get(coord).addConnection(newNode, newNode instanceof KnownMarkovNode ? 1.0f : 0.0f, );
				MarkovNode<T> n = sequence.get(coord);
				int[] distance = diff(currentPos, coord.coords);
				int[] revDistance = MarkovNode.distanceBack(distance);
				n.addConnection(newNode, 1.0f, distance);
				newNode.addConnection(n, 1.0f, revDistance);
			}
		
		// THEN add it (so that it doesn't get connected with itself)
		sequence.put(newNodeCoord, newNode);
		
		incPos();
	}
	
	// Increment position, carrying over based on size. 
	public void incPos(){
		for(int i = 0; i < DIMENSIONS; i++){
			if(currentPos[i] == size[i] - 1){
				currentPos[i] = 0;	// Carry
			}else{
				currentPos[i] = currentPos[i] + 1;
				break;
			}
		}		
	}
	
	public void compile(){
		//for(MarkovNode<T> node: nodes.values()){
			//node.compileProbabilities();
		//}
	}
	
	public void solve(){
		for(MarkovCoordinate coord: unknowns){
			UnknownMarkovNode<T> unk = (UnknownMarkovNode<T>) sequence.get(coord);
			unk.initializeIdentity();
		}
		
		for(int i = 0; i < 2; i++){
			for(MarkovCoordinate coord: unknowns){
				UnknownMarkovNode<T> unk = (UnknownMarkovNode<T>) sequence.get(coord);
				unk.decideIdentity();
			}
			for(MarkovCoordinate coord: unknowns){
				UnknownMarkovNode<T> unk = (UnknownMarkovNode<T>) sequence.get(coord);
				unk.notifyNeighbors();
			}
		}
	}
	
}
