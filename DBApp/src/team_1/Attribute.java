package team_1;

import java.io.Serializable;

@SuppressWarnings({ "rawtypes", "serial" })
public class Attribute implements Serializable {

	String name;
	Object value;
	Class type;

	public Attribute(String AttributeName, Object AttributeValue) {
		this.name = AttributeName;
		this.value = AttributeValue;
		this.type = AttributeValue.getClass();
	}

//	public boolean compare(Attribute a, String key) {
//		if (a.name.equals(key) && a.name.equals(key)) {
//			Integer x = (Integer) value;
//			Integer y = (Integer) a.value;
//			if (x > y)
//				return true;
//		}
//		return false;
//	}

}