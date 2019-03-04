package PageConfig;

import java.io.*;
import java.util.Vector;

@SuppressWarnings("serial")
public class Page implements Serializable {

	Vector<Tuple> tuples;
	int capacity;
	File file; // the file the page points to

	public Page(int num) {
		this.tuples = new Vector<Tuple>();
		this.capacity = 0;

		// every time I create a new page, I need to create a new file to reflect this
		this.file = new File(Table.getName() + "/file " + num);
	}

	public boolean isFull() {
		return capacity >= 2;
	}

	public void addContentToPage(Tuple t) {
		this.tuples.addElement(t);
		this.capacity++;

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
			System.out.println("adding page to: " + this.file.getName() + '\n');
			out.writeObject(this);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteContentFromPage(int i) {
		getTuples().remove(i);
		this.capacity--;
		if (capacity == 0)
			this.file.delete();
		else
			writePageFile();
	}

	public void UpdateContentInPage() {

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
