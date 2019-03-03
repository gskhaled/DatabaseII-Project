package PageConfig;

public class Attribute {
	
	String AttributeName;
	
	Object Type;  // Int, String, date
	
	Object AttributeValue;
	
	Class AttributeType = AttributeValue.getClass();
	
	
	public Attribute(String AttributeName, Class Type, Object AttributeValue ) {
		
		this.AttributeName = AttributeName;
		
		this.Type = Type;
		
		this.AttributeValue = AttributeValue;
		
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
