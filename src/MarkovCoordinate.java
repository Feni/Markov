import java.util.Arrays;

// An n-dimensional coordinate generally used as a relative offset between two MarkovNodes. 
public class MarkovCoordinate implements Comparable<MarkovCoordinate>{
	int[] coords;
	public MarkovCoordinate(int... c){
		coords = Arrays.copyOf(c, c.length);
	}
	
	public boolean equals(Object o){
		if(o instanceof MarkovCoordinate){
			MarkovCoordinate m = (MarkovCoordinate) o;
			return Arrays.equals(m.coords, coords);
		}
		return false;
	}

	@Override
	public int compareTo(MarkovCoordinate m) {
		if(m.coords.length == coords.length){
			for(int i = 0; i < coords.length; i++){
				if(m.coords[i] < coords[i])
					return -1;
				else if(m.coords[i] > coords[i])
					return 1;
			}
			return 0;
		}
		return -1;
	}
	
	public String toString(){
		return Arrays.toString(coords);
	}
}

