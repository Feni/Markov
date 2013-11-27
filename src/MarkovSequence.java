import java.nio.IntBuffer;
import java.util.Arrays;


public class MarkovSequence {
	// Define a 1 dimensional markov random field
	
	public static void main(String args[]){
		MarkovRandomField<String> mrf = new MarkovRandomField<String>(5);
		mrf.add(mrf.newNode("Hello"));
		mrf.add(mrf.newNode("World"));
		mrf.add(mrf.newNode("Hello"));
		mrf.add(mrf.newNode("Dude"));
		
		System.out.println("Hello is : " + mrf.nodes.get("Hello"));
		int[] one = {1};
		IntBuffer oneBuffer = IntBuffer.wrap(one);
		System.out.println(oneBuffer);
		System.out.println("onebuff is " + Arrays.toString(oneBuffer.array()));
		//System.out.println("Hello is : " + mrf.nodes.get("Hello").connections.get(oneBuffer));
		System.out.println("Hello is : " + mrf.nodes.get("Hello").connections.keySet());
		System.out.println("Hello at one : " + mrf.nodes.get("Hello").connections.get(oneBuffer).values());
		
		for(IntBuffer b : mrf.nodes.get("Hello").connections.keySet()){
			System.out.println("Distance " + Arrays.toString(b.array()) + " equals " + b.equals(oneBuffer));
			
		}
		
	}
	

}
