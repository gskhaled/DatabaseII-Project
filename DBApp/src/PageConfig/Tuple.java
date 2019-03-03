package PageConfig;
import java.io.Serializable;
import java.util.Vector;

public class Tuple implements Serializable {
	
	Vector<Attribute> attributes;
	
	public Tuple(Vector<Attribute> at) {
		this.attributes = at;
	}

}
