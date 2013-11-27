
public class MarkovConnection<T> implements Comparable<MarkovConnection<T>>{
	MarkovNode<T> source;
	MarkovNode<T> destination;
	float weight = 0.0f;
	
	public MarkovConnection(MarkovNode<T> src, MarkovNode<T> dst, float w){//, int... off){
		source = src;
		destination = dst;
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
	
	public String toString(){
		return ""+source + " - " + weight + " - " + destination;
	}
	
}
