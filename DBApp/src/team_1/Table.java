package team_1;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

@SuppressWarnings({ "serial" })
public class Table implements Serializable {

	String tableName;
	static String key;
	transient static Vector<Page> pages;
	File file; // the file the table points to

	public Table(String name, String key, Hashtable<String, String> ht) {
		tableName = name;
		Table.key = key;
		pages = new Vector<Page>();

		// initialize the file the table points to INSIDE DATA Folder
		this.file = new File(getDirectoryPath() + "/data/" + tableName);

		// create a directory (folder) with the name of the table, which contains the
		// pages
		// new File(getDirectoryPath() + "/data/" + tableName).mkdir();

		// create metadata.csv if there isn't one already in the directory
		if (!exists())
			new File("data/metadata.csv");

		// write into the metadata.csv file the required info
		try {
			// second parameter of FileWriter tells java to APPEND to metadata.csv, not
			// REWRITE it completely
			PrintWriter writer = new PrintWriter(new FileWriter("data/metadata.csv", true));
			Set<String> keys = ht.keySet();
			for (String element : keys) {
				writer.write(name + ", " + element + ", " + ht.get(element) + ", ");
				writer.write((element == key ? "True" : "False") + ", False");
				writer.write('\n');
			}
			writer.close();

			writeTableFile();

			System.out.println("Done creating a table named: " + name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// writes the file belonging to table, happens only once when creating table
	public void writeTableFile() {
		try {
			FileOutputStream fileOut = new FileOutputStream(this.file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			System.out.println("Writing a file to represent TABLE: " + this.file.getName());
			out.writeObject(this);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// counts the number of files in the directory /data
	public static int getNumberOfFiles(String tableName) {
		File dir = new File(getDirectoryPath() + "/data");
		File[] directoryListing = dir.listFiles();
		int count = 0;
		for (File file : directoryListing)
			if (file.getName().length() > tableName.length() + 5)
				if (file.getName().substring(0, tableName.length() + 5).equals(tableName + " page"))
					count++;
		return count;
	}

	public String getName() {
		return tableName;
	}

	// translates a hashtable to a vector of attributes
	public static Vector<Attribute> getAttributeVector(Hashtable<String, Object> ht) {
		Set<String> keys = ht.keySet();
		// vector set of attributes
		Vector<Attribute> toFill = new Vector<Attribute>();
		for (String k : keys)
			toFill.addElement(new Attribute(k, ht.get(k)));

		return toFill;
	}

	// takes 2 tuples (vector of attributes) and checks if they are equal
	public static boolean equals(Vector<Attribute> first, Vector<Attribute> second) {
		for (int i = 0; i < first.size(); i++) {
			for (int j = 0; j < second.size(); j++)
				if (first.get(i).name.equals(second.get(j).name))
					if (!first.get(i).value.equals(second.get(j).value))
						return false;
		}
		return true;
	}

	// Decodes the page from disk
	public static Object deSerialization(File file) {
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
			ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);
			Object object = objectInputStream.readObject();
			objectInputStream.close();
			return object;
		} catch (ClassNotFoundException | IOException e) {
			System.out.println("Error in deSerialization......");
		}
		return null;
	}

	public static String getDirectoryPath() {
		Path currentRelativePath = Paths.get("");
		return currentRelativePath.toAbsolutePath().toString();
	}

	// returns an INT which represent which attribute in the tuple is the KEY
	public static int getKeyIndex() {
		for (Page p : pages)
			for (Tuple t : p.getTuples())
				for (int i = 0; i < t.getAttributes().size(); i++)
					if (t.getAttributes().get(i).name.equals(key))
						return i;
		return 0;
	}

	// checks to see if metadata.csv was created before (it gets created once)
	public boolean exists() {
		File dir = new File(getDirectoryPath() + "/data");
		File[] directoryListing = dir.listFiles();
		for (File file : directoryListing)
			if (file.getName().equals("metadata.csv"))
				return true;
		return false;
	}

	public static void insert(String tableName, Hashtable<String, Object> ht) {
		Vector<Attribute> toFill = getAttributeVector(ht);
		Tuple tuple = new Tuple(toFill);
		int keyIndex = getKeyIndex();

		// verify that this tuple can be inserted by checking it's format and data
		// structures
		if (isVerified(tableName, tuple))
			System.out.println("Tuple verification passed!");
		else {
			System.out.println("Cannot be added. Error in the tuple");
			System.out.println("----------------------------------------------------------------------------");
			return;
		}

		// initially - first time to insert. create page and just insert into it
		if (pages.isEmpty()) {
			Page newPage = new Page(tableName, getNumberOfFiles(tableName) + 1);
			newPage.addContentToPage(tuple);
			pages.addElement(newPage);
			System.out.println("inserted: " + tuple.getAttributes().get(1).value + " to newly created page");
			System.out.println("----------------------------------------------------------------------------");
			return;
		}

		// i -> page counter
		int i;

		// loop over all pages in harddisk
		for (i = 0; i < pages.size(); i++) {
			Page currentPage = pages.get(i);
			Vector<Tuple> tuplesInPage = currentPage.getTuples();
			int j = 0; // represents which tuple IN the page

			// loop over all the tuples in the current page
			for (; j < tuplesInPage.size(); j++) {

				String valueClass = tuplesInPage.get(j).getAttributes().get(keyIndex).type.getName();

				// if the data type is an int
				if (valueClass.equals("java.lang.Integer")) {
					Integer valueInPage = (Integer) tuplesInPage.get(j).getAttributes().get(keyIndex).value;
					Integer insertionValue = (Integer) toFill.get(keyIndex).value;
					System.out.println("ValueInPage: " + valueInPage + "...... & InsertionValue is: " + insertionValue);
					if (valueInPage > insertionValue) { // this means the value is to be stored in this page...
						if (currentPage.isFull()) { // current page is full so I need to create a new one
							System.out.println("creating a new page because this one was full....");
							Page newPage = new Page(tableName, getNumberOfFiles(tableName) + 1);

							// add the tuple in a New Page
							newPage.addContentToPage(tuple);
							System.out
									.println("wrote this to the newly created page: " + tuple.attributes.get(1).value);
							int w = j;
							// shift the rest of the tuples in the page to the newly created page
							while (w < tuplesInPage.size() && !newPage.isFull()) {
								// System.out.print("wrote this to the page INSIDE WHILE LOOP: " +
								// tuplesInPage.get(w).attributes.get(1).value + " ");
								newPage.addContentToPage(tuplesInPage.get(w));
								currentPage.deleteContentFromPage(w);
							}
							// if j was 0, this means I needed to add the new page in the index BEFORE the
							// current page
							// otherwise I will add the new page in the next index relative to the current
							// page
							if (j == 0)
								pages.add(i, newPage);
							else
								pages.add(i + 1, newPage);
							System.out.println(
									"----------------------------------------------------------------------------");
							renameFiles(tableName);
							if (hasBitmapIndexBuilt(tableName))
								BitmapIndex.updateBitmapIndex(tableName);

							return;
						}
						// the current page has space, so I'll insert into it
						else {
							Vector<Tuple> temp = new Vector<Tuple>();
							int w = j;
							// remove the contents so I can insert the given tuple before them
							while (w < tuplesInPage.size()) {
								temp.add(tuplesInPage.get(w));
								if (tuplesInPage.size() == 1)
									currentPage.deleteContentFromPageWithout(w);
								else
									currentPage.deleteContentFromPage(w);
							}
							// add the inserted tuple
							currentPage.addContentToPage(tuple);
							// re-add the removed tuples
							for (Tuple t : temp)
								currentPage.addContentToPage(t);
							System.out.println(
									"----------------------------------------------------------------------------");
							if (hasBitmapIndexBuilt(tableName))
								BitmapIndex.updateBitmapIndex(tableName);

							return;
						}
					}
				}
				// if the data type is a double
				else if (valueClass.equals("java.lang.Double")) {
					Double valueInPage = (Double) tuplesInPage.get(j).getAttributes().get(keyIndex).value;
					Double insertionValue = (Double) toFill.get(keyIndex).value;
					System.out.println("ValueInPage: " + valueInPage + "...... & InsertionValue is: " + insertionValue);
					if (valueInPage > insertionValue) { // this means the value is to be stored in this page...
						if (currentPage.isFull()) { // current page is full so I need to create a new one
							System.out.println("creating a new page because this one was full....");
							Page newPage = new Page(tableName, getNumberOfFiles(tableName) + 1);
							// add the tuple in a New Page
							System.out
									.println("wrote this to the newly created page: " + tuple.attributes.get(1).value);
							newPage.addContentToPage(tuple);
							int w = j;
							// shift the rest of the tuples in the page to the newly created page
							while (w < tuplesInPage.size() && !newPage.isFull()) {
								// System.out.print("wrote this to the page INSIDE WHILE LOOP: " +
								// tuplesInPage.get(w).attributes.get(1).value + " ");
								newPage.addContentToPage(tuplesInPage.get(w));
								currentPage.deleteContentFromPage(w);
							}
							// if j was 0, this means I needed to add the new page in the index BEFORE the
							// current page
							// otherwise I will add the new page in the next index relative to the current
							// page
							if (j == 0)
								pages.add(i, newPage);
							else
								pages.add(i + 1, newPage);
							System.out.println(
									"----------------------------------------------------------------------------");
							renameFiles(tableName);
							if (hasBitmapIndexBuilt(tableName))
								BitmapIndex.updateBitmapIndex(tableName);

							return;
						}
						// the current page has space, so I'll insert into it
						else {
							Vector<Tuple> temp = new Vector<Tuple>();
							int w = j;
							// remove the contents so I can insert the given tuple before them
							while (w < tuplesInPage.size()) {
								temp.add(tuplesInPage.get(w));
								if (tuplesInPage.size() == 1)
									currentPage.deleteContentFromPageWithout(w);
								else
									currentPage.deleteContentFromPage(w);
							}
							// add the inserted tuple
							currentPage.addContentToPage(tuple);
							// re-add the removed tuples
							for (Tuple t : temp)
								currentPage.addContentToPage(t);
							System.out.println(
									"----------------------------------------------------------------------------");
							if (hasBitmapIndexBuilt(tableName))
								BitmapIndex.updateBitmapIndex(tableName);

							return;
						}
					}
				}
				// if the data type is a string
				else if (valueClass.equals("java.lang.String")) {
					String valueInPage = (String) tuplesInPage.get(j).getAttributes().get(keyIndex).value;
					String insertionValue = (String) toFill.get(keyIndex).value;
					System.out.println("ValueInPage: " + valueInPage + "...... & InsertionValue is: " + insertionValue);
					if (valueInPage.compareTo(insertionValue) > 0) { // this means the value is to be stored in this
																		// page...
						if (currentPage.isFull()) { // current page is full so I need to create a new one
							System.out.println("creating a new page because this one was full....");
							Page newPage = new Page(tableName, getNumberOfFiles(tableName) + 1);
							// add the tuple in a New Page
							System.out
									.println("wrote this to the newly created page: " + tuple.attributes.get(1).value);
							newPage.addContentToPage(tuple);
							int w = j;
							// shift the rest of the tuples in the page to the newly created page
							while (w < tuplesInPage.size() && !newPage.isFull()) {
								// System.out.print("wrote this to the page INSIDE WHILE LOOP: " +
								// tuplesInPage.get(w).attributes.get(1).value + " ");
								newPage.addContentToPage(tuplesInPage.get(w));
								currentPage.deleteContentFromPage(w);
							}
							// if j was 0, this means I needed to add the new page in the index BEFORE the
							// current page
							// otherwise I will add the new page in the next index relative to the current
							// page
							if (j == 0)
								pages.add(i, newPage);
							else
								pages.add(i + 1, newPage);
							System.out.println(
									"----------------------------------------------------------------------------");
							renameFiles(tableName);
							if (hasBitmapIndexBuilt(tableName))
								BitmapIndex.updateBitmapIndex(tableName);

							return;
						}
						// the current page has space, so I'll insert into it
						else {
							Vector<Tuple> temp = new Vector<Tuple>();
							int w = j;
							// remove the contents so I can insert the given tuple before them
							while (w < tuplesInPage.size()) {
								temp.add(tuplesInPage.get(w));
								if (tuplesInPage.size() == 1)
									currentPage.deleteContentFromPageWithout(w);
								else
									currentPage.deleteContentFromPage(w);
							}
							// add the inserted tuple
							currentPage.addContentToPage(tuple);
							// re-add the removed tuples
							for (Tuple t : temp)
								currentPage.addContentToPage(t);
							System.out.println(
									"----------------------------------------------------------------------------");
							if (hasBitmapIndexBuilt(tableName))
								BitmapIndex.updateBitmapIndex(tableName);

							return;
						}
					}
				}
				// if the data typs is a date
				else if (valueClass.equals("java.lang.Date")) {
					Date valueInPage = (Date) tuplesInPage.get(j).getAttributes().get(keyIndex).value;
					Date insertionValue = (Date) toFill.get(keyIndex).value;
					System.out.println("ValueInPage: " + valueInPage + "...... & InsertionValue is: " + insertionValue);
					if (valueInPage.compareTo(insertionValue) > 0) { // this means the value is to be stored in this
																		// page...
						if (currentPage.isFull()) { // current page is full so I need to create a new one
							System.out.println("creating a new page because this one was full....");
							Page newPage = new Page(tableName, getNumberOfFiles(tableName) + 1);
							// add the tuple in a New Page
							System.out
									.println("wrote this to the newly created page: " + tuple.attributes.get(1).value);
							newPage.addContentToPage(tuple);
							int w = j;
							// shift the rest of the tuples in the page to the newly created page
							while (w < tuplesInPage.size() && !newPage.isFull()) {
								// System.out.print("wrote this to the page INSIDE WHILE LOOP: " +
								// tuplesInPage.get(w).attributes.get(1).value + " ");
								newPage.addContentToPage(tuplesInPage.get(w));
								currentPage.deleteContentFromPage(w);
							}
							// if j was 0, this means I needed to add the new page in the index BEFORE the
							// current page
							// otherwise I will add the new page in the next index relative to the current
							// page
							if (j == 0)
								pages.add(i, newPage);
							else
								pages.add(i + 1, newPage);
							System.out.println(
									"----------------------------------------------------------------------------");
							renameFiles(tableName);

							if (hasBitmapIndexBuilt(tableName))
								BitmapIndex.updateBitmapIndex(tableName);

							return;
						}
						// the current page has space, so I'll insert into it
						else {
							Vector<Tuple> temp = new Vector<Tuple>();
							int w = j;
							// remove the contents so I can insert the given tuple before them
							while (w < tuplesInPage.size()) {
								temp.add(tuplesInPage.get(w));
								if (tuplesInPage.size() == 1)
									currentPage.deleteContentFromPageWithout(w);
								else
									currentPage.deleteContentFromPage(w);
							}
							// add the inserted tuple
							currentPage.addContentToPage(tuple);
							// re-add the removed tuples
							for (Tuple t : temp)
								currentPage.addContentToPage(t);
							System.out.println(
									"----------------------------------------------------------------------------");
							if (hasBitmapIndexBuilt(tableName))
								BitmapIndex.updateBitmapIndex(tableName);

							return;
						}
					}
				}

			}
		}

		// if the element to be inserted is the BIGGEST element, I have to add it at the
		// end
		// this means "i" kept being incremented till the end of the pages vector
		if (i == pages.size()) {
			System.out.println("Added at the END!!");
			Page lastPage = pages.get(i - 1);
			// if last page was full, create a new one and insert into it
			if (lastPage.isFull()) {
				System.out.println("creating a new page AT THE END because last one was full....");
				Page newPage = new Page(tableName, getNumberOfFiles(tableName) + 1);
				newPage.addContentToPage(tuple);
				// newPage.swapID(lastPage);
				pages.add(newPage);
			} else
				// just add to the last page with no problems
				lastPage.addContentToPage(tuple);
			System.out.println("----------------------------------------------------------------------------");
		}

		if (hasBitmapIndexBuilt(tableName))
			BitmapIndex.updateBitmapIndex(tableName);
	}

	public static void delete(String tableName, Hashtable<String, Object> ht) {
		// change the hashtable input to a vector of attributes (tuple)
		Vector<Attribute> toDelete = getAttributeVector(ht);
		File dir = new File(getDirectoryPath() + "/data/");
		File[] directoryListing = dir.listFiles();
		// loop over all files in the directory
		for (File file : directoryListing)
			if (file.getName().length() > tableName.length() + 5)
				if (file.getName().substring(0, tableName.length() + 5).equals(tableName + " page")) {
					Page p = (Page) deSerialization(file);
					if (p.tableName.equals(tableName)) {
						for (int i = 0; i < p.getTuples().size(); i++) { // loop over all tuples in page
							Tuple t = p.getTuples().get(i);
							if (equals(toDelete, t.getAttributes())) { // if the 2 tuples are equal, delete it from page
								System.out.print("Deleting........ ");
								p.deleteContentFromPage(i);
							}
						}
					}
				}

		if (hasBitmapIndexBuilt(tableName))
			BitmapIndex.updateBitmapIndex(tableName);
		// System.out.print("Delete was called, so... ");
		// writeTableFile();
	}

	public static void update(String tableName, String keyValue, Hashtable<String, Object> ht) {
		Vector<Attribute> updated = getAttributeVector(ht);

		if (isVerified(tableName, new Tuple(updated)))
			System.out.println("Tuple verification passed!");
		else {
			System.out.println("Cannot be added. Error in the tuple");
			System.out.println("----------------------------------------------------------------------------");
			return;
		}

		File dir = new File(getDirectoryPath() + "/data/");
		File[] directoryListing = dir.listFiles();
		// loop over every file in the current directory
		for (File file : directoryListing)
			if (file.getName().length() > tableName.length() + 5)
				if (file.getName().substring(0, tableName.length() + 5).equals(tableName + " page")) { // make sure
																										// to only
					// check files!
					Page p = (Page) deSerialization(file);
					if (p.tableName.equals(tableName))
						// for loop over the vector of TUPLES in the page
						for (int j = 0; j < p.getTuples().size(); j++) {
							Tuple tuple = p.getTuples().get(j);
							Vector<Attribute> attributeVector = tuple.getAttributes();
							for (int k = 0; k < attributeVector.size(); k++)
								// FOUND the key of this tuple
								if (attributeVector.get(k).name.equals(key)) {
									String m = attributeVector.get(k).value.toString();
									if (m.equals(keyValue)) {
										System.out.println("Found the tuple to update. Updating...");
										tuple.updateTuple(updated);
										p.writePageFile();
									}
								}
						}
				}

		if (hasBitmapIndexBuilt(tableName))
			BitmapIndex.updateBitmapIndex(tableName);

		// System.out.print("Update was called, so... ");
		// writeTableFile();
	}

	public static boolean isVerified(String tableName, Tuple t) {
		Vector<String> colName = new Vector<String>();
		Vector<String> colType = new Vector<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(getDirectoryPath() + "/data/metadata.csv"));
			String line = br.readLine().toString();
			while (line != null) {
				String[] s = line.split(", ");
				if (s[0].equals(tableName)) {
					colName.add(s[1]);
					colType.add(s[2]);
				}
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < t.getAttributes().size(); i++) {
			if (t.getAttributes().size() != colName.size() || t.getAttributes().size() != colType.size())
				return false;
			if (!t.getAttributes().get(i).name.equals(colName.get(i)))
				return false;
			if (!t.getAttributes().get(i).type.getName().equals(colType.get(i)))
				return false;
		}
		return true;
	}

	public static void renameFiles(String tableName) {
		for (int i = pages.size() - 1; i >= 0; i--) {
			Page p = pages.get(i);
			p.renamePage(i + 1);
		}
	}

	public static boolean hasBitmapIndexBuilt(String tableName) {
		File dir = new File(getDirectoryPath() + "/data/");
		File[] directoryListing = dir.listFiles();
		// loop over every file in the current directory
		for (File file : directoryListing) {
			String[] s = file.getName().split("on ");
			if (s.length > 1)
				if (s[2].equals(tableName))
					return true;
		}
		return false;

	}

	public static void printTable(String tableName) {
		System.out.println(
				"############################################################################################");
		File dir = new File(Table.getDirectoryPath() + "/data");
		File[] directoryListing = dir.listFiles();
		for (File file : directoryListing)
			if (file.getName().length() > tableName.length() + 5)
				if (file.getName().substring(0, tableName.length() + 5).equals(tableName + " page")) {
					Page p = (Page) Table.deSerialization(file);
					if (p.tableName.equals(tableName))
						for (Tuple t : p.getTuples()) {
							for (Attribute a : t.getAttributes())
								System.out.print("Attribute name: " + a.name + "& it's value: " + a.value + " ");
							System.out.println();
						}

				}
		System.out.println(
				"############################################################################################");
	}

	// public void addBitmapIndex(String tableName, String colName) {
	// File dir = new File(Table.getDirectoryPath() + "/data/");
	// File[] directoryListing = dir.listFiles();
	// // loop over all files in the directory
	// for (File file : directoryListing)
	// if (!file.getName().substring(0, 4).equals("file")) {
	// // make sure to only check tables!
	// Table t = (Table) Table.deSerialization(file);
	// if (t.getName().equals(tableName)) // if the file belongs to the requested
	// table
	// t.hasBitmapIndex.addElement(colName);
	// }
	// writeTableFile();
	// }

}
