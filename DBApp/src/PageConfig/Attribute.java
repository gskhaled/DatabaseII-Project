package PageConfig;

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

	public void UpdateAttribute(String AttributeName, Object AttributeValue) {
		if (AttributeName != null) {
			this.name = AttributeName;
		}

		if (AttributeValue != null) {
			this.value = AttributeValue;
		}
	
		this.type = AttributeValue.getClass();
	}

	public void EmptyAttribute() {
		this.value = null;
	}

}