package team_1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

@SuppressWarnings({ "serial", "resource" })
public class Page implements Serializable {

	Vector<Tuple> tuples;
	int count;
	int capacity;
	File file; // the file the page points to

	int id;
	String tableName;

	public Page(String tableName, int id) {
		this.tableName = tableName;
		this.tuples = new Vector<Tuple>();

		// stringify the DPApp.properties file and get the max number of tuples in a
		// page
		String[] parts = readFile().split("= ");
		this.capacity = Integer.parseInt(parts[1]);

		this.count = 0;
		this.id = id;
		// every time I create a new page, I need to create a new file to reflect this
		this.file = new File("data/file " + this.id);
	}

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
			System.out.println("Writing a file to represent a page: " + this.file.getName());
			out.writeObject(this);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteContentFromPage(int i) {
		tuples.remove(i);

		// System.out.println("TRYING TO DELETE!!!!!!!");
		// for (int r = 0; r < tuples.size(); r++) {
		// Tuple t = tuples.get(r);
		// System.out.println("ELEMENTS REMAINING: " +
		// t.getAttributes().get(1).value.toString());
		// }

		this.count--;
		if (count == 0) {
			this.file.delete();
		} else
			writePageFile();
	}

	public void deleteContentFromPageWithout(int i) {
		tuples.remove(i);
		this.count--;
		writePageFile();
	}

	public void renamePage(int num) {
		this.id = num;
		this.file.renameTo(new File("data/file " + this.id));
		writePageFile();
	}

}
