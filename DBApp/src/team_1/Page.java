package team_1;

import java.io.*;
import java.util.Vector;

@SuppressWarnings({"serial", "resource"})
public class Page implements Serializable {

	Vector<Tuple> tuples;
	int count;
	int capacity;
	File file; // the file the page points to

	int id;

	public static String readFile() {
		try {
			String path = Table.getDirectoryPath();
			BufferedReader br = new BufferedReader(new FileReader(path + "/config/DBApp.properties"));
			return br.readLine().toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Page(int num) {
		this.tuples = new Vector<Tuple>();
		
		//stringify the DPApp.properties file and get the max number of tuples in a page
		String [] parts = readFile().split("= ");
		this.capacity = Integer.parseInt(parts[1]);
		
		this.count = 0;
		this.id = num;
		// every time I create a new page, I need to create a new file to reflect this
		this.file = new File("data/file " + id);
	}

	public boolean isFull() {
		return count >= capacity;
	}

	public void addContentToPage(Tuple t) {
		this.tuples.add(t);
		this.count++;

		// I also need to write the supplied tuple to the respective file
		writePageFile();
	}

	public Vector<Tuple> getTuples() {
		return tuples;
	}

	public void writePageFile() {
		try {
			FileOutputStream fileOut = new FileOutputStream(this.file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			System.out.println("writing page to: " + this.file.getName());
			out.writeObject(this);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteContentFromPage(int i) {
		getTuples().remove(i);
		this.count--;
		if (count == 0) {
			this.file.delete();
			System.out.println(".......................a file has been deleted");
		} else
			writePageFile();
	}

	public void deleteContentFromPageWithout(int i) {
		getTuples().remove(i);
		this.count--;
		System.out.println(".......................a file has NOT been deleted");
	}


	
//	public Tuple getMaxTill(Vector<Tuple> v, int limit, String key) {
//		Tuple maxSoFar = v.get(0);
//		int i = 0;
//		while (i < limit) {
//			for (int j = 0; j < v.get(i).getAttributes().size(); j++) {
//				Attribute a = v.get(i).getAttributes().get(j);
//				Attribute b = v.g
//			}
//		}
//	
//	}
//	
//	public Page sortPage(Page p, int id, String key) {
//		Vector<Tuple> v = new Vector<Tuple>();
//		for (Tuple t : p.getTuples()) {
//			int i;
//		}
//	
//	}

//	public void setID(int id) {
//		this.id = id;
//		this.file.renameTo(new File(Table.getName() + "/file " + id));
//	}
//
//	public void setTemp() {
//		this.file.renameTo(new File(Table.getName() + "/file temp"));
//	}
//
//	public void swapID(Page p) {
//		int this_id = this.id;
//		int p_id = p.id;
//		this.setTemp();
//		p.setID(this_id);
//		this.setID(p_id);
//	}

}
