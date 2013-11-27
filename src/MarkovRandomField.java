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
		if(n == null){	// Node doesn't exist yet. Create it. 
			n = new MarkovNode<T>(val, this);
			nodes.put(val, n);
		}
		return n;
	}
	
	public void add(MarkovNode<T> newNode){
		sequence.put(IntBuffer.wrap(currentPos), newNode);
		totalElements += 1;
		for(int i = 0; i < DIMENSIONS; i++){
			if(currentPos[i] == size[i] - 1){
				currentPos[i] = 0;	// Carry
			}else{
				currentPos[i] = currentPos[i] + 1;
				break;
			}
		}
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
	}
	
}
