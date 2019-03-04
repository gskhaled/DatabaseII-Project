package PageConfig;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Table {

	static String tableName;
	static String key;
	Vector<Page> pages;
	int numOfPages;

	public static String getName() {
		return tableName;
	}

	public static Vector<Attribute> getAttributeVector(Hashtable<String, Object> ht) {
		Set<String> keys = ht.keySet();
		// vector set of attributes
		Vector<Attribute> toFill = new Vector<Attribute>();
		for (String k : keys)
			toFill.addElement(new Attribute(k, ht.get(k)));

		return toFill;
	}

	public static boolean equals(Vector<Attribute> first, Vector<Attribute> second) {
		for (int i = 0; i < first.size(); i++) {
			for (int j = 0; j < second.size(); j++)
				if (first.get(i).name.equals(second.get(j).name))
					if (!first.get(i).value.equals(second.get(j).value))
						return false;
		}
		return true;
	}

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

	public int getKeyIndex() {
		for (Page p : pages)
			for (Tuple t : p.getTuples())
				for (int i = 0; i < t.getAttributes().size(); i++)
					if (t.getAttributes().get(i).name.equals(key))
						return i;
		return 0;
	}

	public Table(String name, String key, Hashtable<String, String> ht) {
		tableName = name;
		Table.key = key;
		this.pages = new Vector<Page>();

		// create a directory (folder) with the given name inside the current local path
		new File(getDirectoryPath() + "/" + name).mkdir();

		// create metadata.csv
		File file = new File(name + "/metadata.csv");

		// write into the metadata.csv file the required info
		try {
			PrintWriter writer = new PrintWriter(file);
			Set<String> keys = ht.keySet();
			for (String element : keys) {
				writer.write(name + ", " + element + ", " + ht.get(element) + ", ");
				writer.write((element == key ? "True" : "False") + ", ");
				writer.write('\n');
			}
			writer.close();
			System.out.println("Done!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// public void insert(String tableName, Hashtable<String, Object> ht) {
	// Vector<Attribute> toFill = getAttributeVector(ht);
	//
	// Page latestPage;
	// // initially, pages would be empty, so I add one page
	// if (pages.isEmpty()) {
	// this.numOfPages = 1;
	// this.pages.addElement(new Page(1));
	// System.out.println("added a page because pageS was empty! called it 1");
	// latestPage = this.pages.get(0);
	// }
	// // otherwise I get the latest page to see if it has space
	// else {
	// int size = pages.size();
	// // getting the last page in the pages vector
	// latestPage = pages.get(size - 1);
	// }
	//
	// // whether the last page still has space
	// if (!latestPage.isFull()) {
	// System.out.println("found space in page number: " + numOfPages + " so
	// inserting here");
	// Tuple tuple = new Tuple(toFill);
	// latestPage.addContentToPage(tuple);
	// }
	// // means last page is full, so we make a new one, then add to it the tuple
	// else {
	// this.numOfPages++;
	// System.out.println("last page was full so i made a new one and numbered it: "
	// + numOfPages
	// + " ..then added tuple to it");
	// Page page = new Page(numOfPages);
	// Tuple tuple = new Tuple(toFill);
	// page.addContentToPage(tuple);
	// pages.addElement(page);
	// }
	// }

	public void newInsert(String tableName, Hashtable<String, Object> ht) {
		Vector<Attribute> toFill = getAttributeVector(ht);
		Tuple tuple = new Tuple(toFill);

		// initially - first time to insert. create page and just insert into it
		if (pages.isEmpty()) {
			Page newPage = new Page(1);
			newPage.addContentToPage(tuple);
			this.numOfPages = 1;
			this.pages.addElement(newPage);
			System.out.println("added a page because pageS was empty! called it: " + numOfPages);
			System.out.println("inserted: " + tuple.getAttributes().get(1).value + '\n');
			return;
		}

		// i -> page counter
		int i;
		for (i = 0; i < pages.size(); i++) {
			System.out.println("inside pages loop.... " + numOfPages);
			Page currentPage = pages.get(i);
			Vector<Tuple> tuplesInPage = currentPage.getTuples();
			int j = 0; // represents which tuple IN the page
			for (; j < tuplesInPage.size(); j++) {
				int keyIndex = getKeyIndex();
				Integer valueInPage = (Integer) tuplesInPage.get(j).getAttributes().get(keyIndex).value;
				Integer insertionValue = (Integer) toFill.get(keyIndex).value;
				System.out.println(
						"valueInPage: " + valueInPage + " ...................insertionValue: " + insertionValue);
				if (valueInPage > insertionValue) { // this means the value is to be stored in this page...
					System.out.println("found the page to insert in!!!!!!!!!!!!!!");
					if (currentPage.isFull()) { // current page is full so I need to create a new one
						System.out.println("creating a new page because this one was full...." + '\n');
						numOfPages++;
						Page newPage = new Page(numOfPages);
						// add the tuple in a New Page
						System.out.print(" wrote this to the newly created page: " + tuple.attributes.get(1).value + " ");
						newPage.addContentToPage(tuple);
						int w = j;
						// add the rest of the tuples that came after it
						while (w < tuplesInPage.size() && !newPage.isFull()) {
							System.out.print(" wrote this to the page INSIDE WHILE LOOP: "
									+ tuplesInPage.get(w).attributes.get(1).value + " ");
							newPage.addContentToPage(tuplesInPage.get(w));
							currentPage.deleteContentFromPage(w);
							w++;
						}
						pages.add(i, newPage);
						System.out.println();
						return;
					}
					// the current page has space, so i'll insert into it
					else {
						System.out.println("found space so inserting in page: " + numOfPages);
						Vector<Tuple> temp = new Vector<Tuple>();
						int w = j;
						// remove the content so I can insert before it
						while (w < tuplesInPage.size()) {
							temp.add(tuplesInPage.get(w));
							if (tuplesInPage.size() == 1)
								currentPage.deleteContentFromPageWithout(w);
							else
								currentPage.deleteContentFromPage(w);
						}
						// add the inserted tuple
						currentPage.addContentToPage(tuple);
						// readd the removed tuples
						for (Tuple t : temp)
							currentPage.addContentToPage(t);
						System.out.println();
						return;
					}
				}
			}
		}

		// if the element to be inserted is the BIGGEST element, I have to add it at the
		// end
		if (i == pages.size()) {
			System.out.println("Added At the END!!" + '\n');
			Page lastPage = pages.get(i - 1);
			if (lastPage.isFull()) {
				System.out.println("creating a new page AT THE END because this one was full....");
				numOfPages++;
				Page newPage = new Page(numOfPages);
				newPage.addContentToPage(tuple);
				pages.add(i, newPage);
				System.out.println("addition at the end completed successfully@@@@@@");
			} else {
				lastPage.addContentToPage(tuple);
			}
		}
	}

	public void delete(String tableName, Hashtable<String, Object> ht) {
		Vector<Attribute> toDelete = getAttributeVector(ht);
		File dir = new File(getDirectoryPath() + "/" + getName());
		File[] directoryListing = dir.listFiles();
		for (File file : directoryListing)
			if (!file.getName().equals("metadata.csv")) {
				Page p = (Page) deSerialization(file);
				for (int i = 0; i < p.getTuples().size(); i++) {
					Tuple t = p.getTuples().get(i);
					if (equals(toDelete, t.getAttributes())) {
						p.deleteContentFromPage(i);
						System.out.println("delete accomplished");
					}
				}
			}
	}

	public void update(String name, String keyValue, Hashtable<String, Object> ht) {
		Vector<Attribute> updated = getAttributeVector(ht);
		File dir = new File(getDirectoryPath() + "/" + getName());
		File[] directoryListing = dir.listFiles();
		for (File file : directoryListing)
			if (!file.getName().equals("metadata.csv")) {
				Page p = (Page) deSerialization(file);
				// for loop over the vector of TUPLES in the page
				for (int i = 0; i < p.getTuples().size(); i++) {
					Tuple tuple = p.getTuples().get(i);
					Vector<Attribute> attributeVector = tuple.getAttributes();
					for (int j = 0; j < attributeVector.size(); j++)
						// FOUND the key of this tuple
						if (attributeVector.get(j).name.equals(key)) {
							String m = attributeVector.get(j).value.toString();
							if (m.equals(keyValue)) {
								System.out.println("you should update now");
								tuple.updateTuple(updated);
								p.writePageFile();
							}
						}
				}
			}
	}

}
