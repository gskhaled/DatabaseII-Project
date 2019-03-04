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

	public void updateTuple(Vector<Attribute> update) {
		for (int i = 0; i < update.size(); i++)
			for (int j = 0; j < attributes.size(); j++)
				if (update.get(i).name.equals(attributes.get(j).name))
					attributes.get(j).value = update.get(i).value;
	}

	// public static void main(String [] args) {
	// Vector<Attribute> v = new Vector<Attribute>();
	// v.add(new Attribute("id", 123));
	// v.add(new Attribute("name", "gs"));
	// v.add(new Attribute("age", 1));
	// Tuple t = new Tuple(v);
	// Vector<Attribute> w = new Vector<Attribute>();
	// w.add(new Attribute("age", 99));
	// w.add(new Attribute("name", "vcwgwgdsag"));
	// t.updateTuple(w);
	// System.out.println(t.attributes.get(2).value + " age");
	// System.out.println(t.attributes.get(1).value + " name");
	// }

}
