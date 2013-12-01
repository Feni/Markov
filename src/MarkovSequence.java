import java.nio.IntBuffer;
import java.util.Arrays;


// Step 1: Add in the sequence
// Step 2: initializeState using global suggestions (each patch gets what's suggested to it as the optimal state for it)
// Step 3: Identity crisis - Each patch looks at all messages and figures out what it should be. 
// Step 4: Proclamation - The node tells all other nodes of it's current state. 

public class MarkovSequence {
	// Define a 1 dimensional markov random field
	
	public static void main(String args[]){
		MarkovRandomField<String> mrf = new MarkovRandomField<String>(12);
		mrf.add(mrf.newNode("Hello"));
		mrf.add(mrf.newNode("World"));
		mrf.add(mrf.newNode("Hello"));
		mrf.add(mrf.newNode("Dude"));
		//mrf.add(mrf.newNode("Hello"));
		//mrf.add(mrf.newNode("World"));
		//mrf.add(mrf.newNode("Sup"));
		
		UnknownMarkovNode<String> unk = mrf.newUnknown();
		mrf.add(unk);
		mrf.add(mrf.newUnknown());
		System.out.println("Unk connections is " + unk.connections);
		
		/*
		System.out.println("Hello is : " + mrf.nodes.get("Hello"));
		int[] one = {1};
		IntBuffer oneBuffer = IntBuffer.wrap(one);
		System.out.println(oneBuffer);
		System.out.println("onebuff is " + Arrays.toString(oneBuffer.array()));
		//System.out.println("Hello is : " + mrf.nodes.get("Hello").connections.get(oneBuffer));
		System.out.println("Hello is : " + mrf.nodes.get("Hello").connections.keySet());
		System.out.println("Hello at one : " + mrf.nodes.get("Hello").connections.get(oneBuffer).values());
		
		System.out.println("Hello sum at one : " + mrf.nodes.get("Hello").locationProbSum.get(oneBuffer));
		
		System.out.println("Hello probs at one : " + mrf.nodes.get("Hello").locationProbabilities.get(oneBuffer));
		
		*/
		
		
		//mrf.nodes.get("Hello").compileProbabilities();
		mrf.compile();
		mrf.solve();
		
		mrf.initialize();
		
		//unk.simpleBestGuess();
		//System.out.println("After compile Hello probs at one : " + mrf.nodes.get("Hello").locationProbabilities.get(oneBuffer).values());
		
		
/*		for(IntBuffer b : mrf.nodes.get("Hello").connections.keySet()){
			System.out.println("Distance " + Arrays.toString(b.array()) + " equals " + b.equals(oneBuffer));
		} */
		
		
		
	}
	

}
