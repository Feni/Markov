
// Step 1: Add in the sequence
// Step 2: initializeState using global suggestions (each patch gets what's suggested to it as the optimal state for it)
// Step 3: Identity crisis - Each patch looks at all messages and figures out what it should be. 
// Step 4: Proclamation - The node tells all other nodes of it's current state.

// Compute statistics for each patch as they're added. (Can be computed from shifting the statistics from the previous nodes). 

public class MarkovSequence {
	// Define a 1 dimensional markov random field
	
	public static void main(String args[]){
		MarkovRandomField<String> mrf = new MarkovRandomField<String>(50);
		mrf.add(mrf.newNode("A"));
		mrf.add(mrf.newNode("B"));
		mrf.add(mrf.newNode("A"));
		mrf.add(mrf.newNode("B"));
		mrf.add(mrf.newNode("A"));
		mrf.add(mrf.newNode("C"));
		mrf.add(mrf.newNode("A"));
		//mrf.add(mrf.newNode("Hello"));
		//mrf.add(mrf.newNode("World"));
		//mrf.add(mrf.newNode("Sup"));
		
		UnknownMarkovNode<String> unk = mrf.newUnknown();
		mrf.add(unk);
		mrf.add(mrf.newUnknown());
		/*System.out.println("Unk connections is " + unk.connections);
		
		
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

		
		//((KnownMarkovNode) mrf.nodes.get("A")).printOffsets();
		//System.out.println("A at 1 is " + mrf.nodes.get("A").atOffset.get(new MarkovCoordinate(1)));		
		//unk.simpleBestGuess();
		//System.out.println("After compile Hello probs at one : " + mrf.nodes.get("Hello").locationProbabilities.get(oneBuffer).values());
		
		
/*		for(IntBuffer b : mrf.nodes.get("Hello").connections.keySet()){
			System.out.println("Distance " + Arrays.toString(b.array()) + " equals " + b.equals(oneBuffer));
		} */
		
	}
	

}
