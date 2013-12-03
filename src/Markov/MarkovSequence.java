package Markov;

// Step 1: Add in the sequence
// Step 2: initializeState using global suggestions (each patch gets what's suggested to it as the optimal state for it)
// Step 3: Identity crisis - Each patch looks at all messages and figures out what it should be. 
// Step 4: Proclamation - The node tells all other nodes of it's current state.
// Compute statistics for each patch as they're added. (Can be computed from shifting the statistics from the previous nodes). 

public class MarkovSequence {
	// Define a 1 dimensional markov random field
	
	public static void main(String args[]){
		MarkovRandomField<String> mrf = new MarkovRandomField<String>(2, 5);
//		mrf.add(mrf.newNode("?"));
		mrf.add(mrf.newUnknown());
		mrf.add(mrf.newNode("B"));
		
		mrf.add(mrf.newNode("C"));
		mrf.add(mrf.newNode("B"));
		
		mrf.add(mrf.newNode("A"));
		mrf.add(mrf.newNode("B"));
//		mrf.add(mrf.newNode("B"));
//		mrf.add(mrf.newNode("A"));
//		mrf.add(mrf.newNode("C"));
//		mrf.add(mrf.newNode("A"));
		mrf.add(mrf.newUnknown());
		mrf.add(mrf.newUnknown());
		//mrf.add(mrf.newUnknown());
		mrf.solve();
		
		//((KnownMarkovNode) mrf.nodes.get("B")).printOffsets();
		
	}
	

}

