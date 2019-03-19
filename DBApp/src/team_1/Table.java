package team_1;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@SuppressWarnings("serial")
public class Table implements Serializable{

	static String tableName;
	static String key;
	transient Vector<Page> pages;
	File file; //the file the table points to
	
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

	public int getNumberOfPages() {
		return pages.size();
	}

	public static String getName() {
		return tableName;
	}

	//translates a hashtable to a vector of attributes
	public static Vector<Attribute> getAttributeVector(Hashtable<String, Object> ht) {
		Set<String> keys = ht.keySet();
		// vector set of attributes
		Vector<Attribute> toFill = new Vector<Attribute>();
		for (String k : keys)
			toFill.addElement(new Attribute(k, ht.get(k)));

		return toFill;
	}

	//takes 2 tuples (vector of attributes) and checks if they are equal
	public static boolean equals(Vector<Attribute> first, Vector<Attribute> second) {
		for (int i = 0; i < first.size(); i++) {
			for (int j = 0; j < second.size(); j++)
				if (first.get(i).name.equals(second.get(j).name))
					if (!first.get(i).value.equals(second.get(j).value))
						return false;
		}
		return true;
	}

	//Decodes the page from disk
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

	//returns an INT which represent which attribute in the tuple is the KEY
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

		// create a directory (folder) with the name "DATA" inside the current local path
		new File(getDirectoryPath() + "/data").mkdir();

		// create metadata.csv
		File file = new File("data/metadata.csv");
		
		//initialize the file the table points to INSIDE DATA Folder
		this.file = new File("data/" + name);

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

	public void insert(String tableName, Hashtable<String, Object> ht) {
		Vector<Attribute> toFill = getAttributeVector(ht);
		Tuple tuple = new Tuple(toFill);

		// initially - first time to insert. create page and just insert into it
		if (pages.isEmpty()) {
			Page newPage = new Page(getNumberOfPages() + 1);
			newPage.addContentToPage(tuple);
			this.pages.addElement(newPage);
			System.out.println("inserted: " + tuple.getAttributes().get(1).value + " to newly created page");
			return;
		}

		// i -> page counter
		int i;
		
		//loop over all pages in harddisk
		for (i = 0; i < pages.size(); i++) {
			Page currentPage = pages.get(i);
			Vector<Tuple> tuplesInPage = currentPage.getTuples();
			int j = 0; // represents which tuple IN the page
			
			//loop over all the tuples in the current page
			for (; j < tuplesInPage.size(); j++) {
				int keyIndex = getKeyIndex();
				Integer valueInPage = (Integer) tuplesInPage.get(j).getAttributes().get(keyIndex).value;
				Integer insertionValue = (Integer) toFill.get(keyIndex).value;
				System.out.println(
				"valueInPage: " + valueInPage + " ......&insertionValue is: " +
				insertionValue);
				if (valueInPage > insertionValue) { // this means the value is to be stored in this page...
					if (currentPage.isFull()) { // current page is full so I need to create a new one
						System.out.println("creating a new page because this one was full....");
						Page newPage = new Page(getNumberOfPages() + 1);
						 //add the tuple in a New Page
						System.out.println("wrote this to the newly created page: " + tuple.attributes.get(1).value);
						newPage.addContentToPage(tuple);
						int w = j;
						//shift the rest of the tuples in the page to the newly created page
						while (w < tuplesInPage.size() && !newPage.isFull()) {
							//System.out.print("wrote this to the page INSIDE WHILE LOOP: " + tuplesInPage.get(w).attributes.get(1).value + " ");
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
						System.out.println("----------------------------------------------------------------------------");
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
						System.out.println("----------------------------------------------------------------------------");
						return;
					}
				}
			}
		}

		// if the element to be inserted is the BIGGEST element, I have to add it at the end
		// this means i kept being incremented till the end of the pages vector
		if (i == pages.size()) {
			System.out.println("Added at the END!!");
			Page lastPage = pages.get(i - 1);
			//if last page was full, create a new one and insert into it
			if (lastPage.isFull()) {
				System.out.println("creating a new page AT THE END because last one was full....");
				Page newPage = new Page(getNumberOfPages() + 1);
				newPage.addContentToPage(tuple);
				// newPage.swapID(lastPage);
				pages.add(newPage);
			} else 
				//just add to the last page with no problems
				lastPage.addContentToPage(tuple);
			System.out.println("----------------------------------------------------------------------------");
		}
	}

	public void delete(String tableName, Hashtable<String, Object> ht) {
		//change the hashtable input to a vector of attributes (tuple)
		Vector<Attribute> toDelete = getAttributeVector(ht);
		File dir = new File(getDirectoryPath() + "/" + getName());
		File[] directoryListing = dir.listFiles();
		//loop over all files in the directory
		for (File file : directoryListing)
			if (!file.getName().equals("metadata.csv")) { //make sure not to touch metadata.csv
				Page p = (Page) deSerialization(file); //deserialise the page from disk
				for (int i = 0; i < p.getTuples().size(); i++) {
					Tuple t = p.getTuples().get(i);
					if (equals(toDelete, t.getAttributes())) { //if the 2 tuples are equal, delete it from page
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
		//loop over every file in the current directory
		for (File file : directoryListing)
			if (!file.getName().equals("metadata.csv")) { //make sure not to touch metadata.csv
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
								System.out.println("Found the tuple to update. Updating...");
								tuple.updateTuple(updated);
								p.writePageFile();
							}
						}
				}
			}
	}
}
