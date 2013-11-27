
public class MarkovSequence {
	// Define a 1 dimensional markov random field
	
	public static void main(String args[]){
		MarkovRandomField<String> mrf = new MarkovRandomField<String>(2, 3);
		mrf.add(mrf.newNode("Hello"));
		mrf.add(mrf.newNode("World"));
		mrf.add(mrf.newNode("Super"));
		mrf.add(mrf.newNode("Man"));
	}
	

}
