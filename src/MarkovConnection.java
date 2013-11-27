
public class MarkovConnection<T> implements Comparable<MarkovConnection<T>>{
	MarkovNode<T> source;
	MarkovNode<T> destination;
	MarkovRandomField<T> mrf;
	// The difference between source and destination's locations.
	// ex. If src = 3 and dst = 4. offset = 1.
	int[] offset; //= new int[MarkovRandomField.DIMENSIONS];
	float weight = 0.0f;
	
	public MarkovConnection(MarkovNode<T> src, MarkovNode<T> dst, MarkovRandomField<T> markov, float w, int... off){
		source = src;
		destination = dst;
		mrf = markov;
		offset = off;
		weight = w;
	}
	
	public int compareTo(MarkovConnection<T> other){
		// Compare the weights
		if(this.weight < other.weight){ 
			return -1;
		}else if(this.weight > other.weight){
			return 1;
		}else {	// Then compare the objects. 
			return 0;
		}
	}
	
}
