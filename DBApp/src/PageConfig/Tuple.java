package PageConfig;
import java.util.ArrayList;

public class Tuple {
	
	ArrayList<Attribute> TupleArray;
	
	public Tuple() {
		
		TupleArray = new ArrayList<Attribute>();
		
	}
	
	public void AddContent(Attribute Attribute) {
		
		TupleArray.add(Attribute);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
