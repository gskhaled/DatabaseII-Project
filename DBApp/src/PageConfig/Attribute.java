package PageConfig;

public class Attribute {

	String AttributeName;

	Object AttributeValue;

	Class AttributeType;

	public Attribute(String AttributeName, Object AttributeValue) {

		this.AttributeName = AttributeName;

		this.AttributeType = AttributeValue.getClass();

		this.AttributeValue = AttributeValue;

	}

	public void UpdateAttribute(String AttributeName, Object AttributeValue) {

		if (AttributeName != null) {

			this.AttributeName = AttributeName;
		}

		if (AttributeValue != null) {

			this.AttributeValue = AttributeValue;
		}
		
		this.AttributeType = AttributeValue.getClass();

	}

	public void EmptyAttribute() {

		this.AttributeValue = null;

	}

}