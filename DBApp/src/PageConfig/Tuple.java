package PageConfig;

import java.io.Serializable;
import java.util.Vector;

@SuppressWarnings("serial")
public class Tuple implements Serializable {

	Vector<Attribute> attributes;

	public Tuple(Vector<Attribute> at) {
		this.attributes = at;
	}

	public Vector<Attribute> getAttributes() {
		return attributes;
	}

}
