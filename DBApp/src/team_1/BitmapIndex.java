package team_1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

@SuppressWarnings({ "serial", "static-access" })
public class BitmapIndex implements Serializable {

	transient Vector<BitmapPage> pages;
	String tableName;
	String colName;
	File file;

	public BitmapIndex(String tableName, String colName) {
		this.pages = new Vector<BitmapPage>();
		this.tableName = tableName;
		this.colName = colName;

		// this section of code is concerned of editing the metadata.csv file to show
		// changes of creating a bitmap index
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			String path = Table.getDirectoryPath();
			// initializes in to be the metadata original file
			in = new BufferedReader(new FileReader(path + "/data/metadata.csv"));
			// creates a new temporary file called metadata2 that i copy everything to,
			// except the part that i want to change
			File f = new File(path + "/data/metadata2.csv");
			// true means to append the file, not rewrite it
			out = new PrintWriter(new FileWriter(f, true));
			String line = in.readLine();
			while (line != null) {
				String[] parts = line.split(", ");
				// found the section i want to change
				if (parts[0].equals(tableName) && parts[1].equals(colName))
					out.write(parts[0] + ", " + parts[1] + ", " + parts[2] + ", " + parts[3] + ", True");
				else
					out.write(line);
				out.write('\n');
				line = in.readLine();
			}
			in.close();
			out.close();
			// delete the original metadata
			File metadata = new File(path + "/data/metadata.csv");
			metadata.delete();
			// rename metadata2 to metadata ;)
			f.renameTo(metadata);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// initialize the file this bitmap index points to INSIDE DATA Folder
		this.file = new File(Table.getDirectoryPath() + "/data/" + "BITMAP INDEX on " + colName + " on " + tableName);

		// i will already need a vector that has all the values in this table
		Vector<Object> vector = createDenseIndexArray(tableName, colName);
		System.out.println("vector of unique values: " + vector);

		// if this column is the primary key then i don't need to sort
		if (!isClusteringKey(tableName, colName)) {
			vector = sortTheValues(vector); // the vector is now sorted!
			System.out.println("vector of unique AND sorted values: " + vector);
		}

		Vector<Index> indexVector = setBitmap(tableName, colName, vector);

		// add an empty bitmap page element to the vector of pages
		this.pages.addElement(new BitmapPage(tableName, 1));
		fillThePages(indexVector);

		writeIndexFile();

		deleteTheRest(tableName, pages.size() + 1);
	}

	// this fills the pages vector of this class by creating many bitmap pages
	public void fillThePages(Vector<Index> indexVector) {
		int p = 0; // counter for the number of pages
		// this loop just keeps adding indices to the bitmap index using
		// the vector v which is already sorted
		for (int i = 0; i < indexVector.size(); i++) {
			BitmapPage bp = this.pages.get(p);
			Index index = indexVector.get(i);
			if (!bp.isFull())
				bp.addIndexToPage(index);
			else {
				this.pages.addElement(new BitmapPage(tableName, pages.size() + 1));
				p++;
				this.pages.get(p).addIndexToPage(index);
			}
		}
	}

	// returns an array of unique values in colName, in tableName
	public static Vector<Object> createDenseIndexArray(String tableName, String colName) {
		Vector<Object> uniqueValues = new Vector<Object>();
		File dir = new File(Table.getDirectoryPath() + "/data/");
		File[] directoryListing = dir.listFiles();
		// loop over all files in the directory
		for (File file : directoryListing)
			if (file.getName().length() >= tableName.length() + 5)
				if (file.getName().substring(0, tableName.length() + 5).equals(tableName + " page")) { // make sure to
																										// only check
																										// files
					Page p = (Page) Table.deSerialization(file);
					if (p.tableName.equals(tableName)) // if the page belongs to this table
						for (int i = 0; i < p.getTuples().size(); i++) { // loop over all tuples in page
							Tuple t = p.getTuples().get(i);
							for (int j = 0; j < t.getAttributes().size(); j++) { // loop over all attributes in THIS
																					// tuple
								Attribute x = t.getAttributes().get(j);
								if (x.name.equals(colName))
									// if the attribute name is the colName i want to create bitmap index on, add it
									// to the returned array of values
									// also check if this value was added before as we only need unique values!
									if (!uniqueValues.contains(x.value))
										uniqueValues.add(x.value);
							}
						}
				}
		return uniqueValues;
	}

	// returns an array of indices that have their bitmaps set, which will be used
	// to fill bitmap pages
	public static Vector<Index> setBitmap(String tableName, String colName, Vector<Object> vector) {
		Vector<Index> indexVector = new Vector<Index>();
		Vector<Object> a = new Vector<Object>();
		File dir = new File(Table.getDirectoryPath() + "/data/");
		File[] directoryListing = dir.listFiles();
		// loop over all files in the directory
		// The whole purpose of this section is to create a vector of objects that has
		// ALL values of the respective column
		System.out.print("All values: ");
		for (File file : directoryListing)
			if (file.getName().length() >= tableName.length() + 5)
				if (file.getName().substring(0, tableName.length() + 5).equals(tableName + " page")) { // make sure to
																										// only check
																										// files
					Page p = (Page) Table.deSerialization(file);
					if (p.tableName.equals(tableName)) // if the page belongs to this table
						for (int i = 0; i < p.getTuples().size(); i++) { // loop over all tuples in page
							Tuple t = p.getTuples().get(i);
							for (int j = 0; j < t.getAttributes().size(); j++) {
								Attribute x = t.getAttributes().get(j);
								if (x.name.equals(colName)) {
									System.out.print(x.value + ".. ");
									a.add(x.value);
								}
							}
						}
				}

		System.out.println();
		// loops over every single unique value and builds its index by looping on all
		// values of this column each time and either adding a 0 or a 1
		for (Object o1 : vector) {
			System.out.println("the element: " + o1.toString());
			Index x;
			x = new Index(o1);
			// redundant...
			// if (o1.getClass().getName().equals("java.lang.Integer"))
			// x = new Index((Integer) o1);
			// else if (o1.getClass().getName().equals("java.lang.String"))
			// x = new Index((String) o1);
			// else if (o1.getClass().getName().equals("java.lang.Double"))
			// x = new Index((Double) o1);
			// else
			// x = new Index((Date) o1);

			for (Object o2 : a) {
				if (o1.equals(o2))
					x.addToBitmap('1');
				else
					x.addToBitmap('0');
			}
			System.out.println(x.bitmap);
			indexVector.add(x);
		}

		return indexVector;
	}

	// return an array of sorted objects that we will then create the index objects
	// on
	public static Vector<Object> sortTheValues(Vector<Object> a) {
		Vector<Object> sortedVector = new Vector<Object>();

		if (a.firstElement().getClass().getName().equals("java.lang.Integer")) { // if the elements are of type Integer
			Vector<Integer> vector = new Vector<Integer>();
			// loop to empty vector of objects into a vector of ints
			for (Object o : a)
				vector.add((Integer) o);
			// sort the vector
			Collections.sort(vector);
			// now return the ints to objects and return this vector of objects
			for (Integer i : vector)
				sortedVector.add(i);
			return sortedVector;
		} else if (a.firstElement().getClass().getName().equals("java.lang.String")) { // if the elements are of type
																						// String
			Vector<String> vector = new Vector<String>();
			// loop to empty vector of objects into a vector of strings
			for (Object o : a)
				vector.add((String) o);
			// sort the vector
			Collections.sort(vector);
			// now return the strings to objects and return this vector of objects
			for (String s : vector)
				sortedVector.add(s);
			return sortedVector;
		} else if (a.firstElement().getClass().getName().equals("java.lang.Double")) { // if the elements are of type
																						// Double
			Vector<Double> vector = new Vector<Double>();
			// loop to empty vector of objects into a vector of doubles
			for (Object o : a)
				vector.add((Double) o);
			// sort the vector
			Collections.sort(vector);
			// now return the doubles to objects and return this vector of objects
			for (Double d : vector)
				sortedVector.add(d);
			return sortedVector;
		} else { // else the elements are of type Date
			Vector<Date> vector = new Vector<Date>();
			// loop to empty vector of objects into a vector of dates
			for (Object o : a)
				vector.add((Date) o);
			// sort the vector
			Collections.sort(vector);
			// now return the dates to objects and return this vector of objects
			for (Date d : vector)
				sortedVector.add(d);
			return sortedVector;
		}
	}

	// counts the number of files in the directory /data
	public static int getNumberOfFiles(String tableName) {
		File dir = new File(Table.getDirectoryPath() + "/data");
		File[] directoryListing = dir.listFiles();
		int count = 0;
		for (File file : directoryListing)
			if (file.getName().length() > tableName.length() + 12)
				if (file.getName().substring(0, tableName.length() + 12).equals(tableName + " bitmap page"))
					count++;
		return count;
	}

	// writes the bitmapindex on disk
	public void writeIndexFile() {
		try {
			FileOutputStream fileOut = new FileOutputStream(this.file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			System.out.println("Writing a file to represent INDEX: " + this.file.getName());
			out.writeObject(this);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// this method re-creates ALL indices of TableName on each alteration, whenever
	// it's called.
	public static void updateBitmapIndex(String tableName) {
		File dir = new File(Table.getDirectoryPath() + "/data/");
		File[] directoryListing = dir.listFiles();
		// loop over every file in the current directory
		for (File file : directoryListing)
			if (file.getName().length() > 12)
				if (file.getName().substring(0, 12).equals("BITMAP INDEX")) {
					String[] parts = file.getName().split(" on ");
					String colName = parts[1];
					String tableNameOfFile = parts[2];
					if (tableNameOfFile.equals(tableName)) {
						System.out.println(
								"**********************Updating a bitmap Index: " + colName + "**********************");
						// delete the old one, and recreate a new one
						file.delete();
						new BitmapIndex(tableName, colName);
					}

				}
	}

	// this method take a table name, and an int, on which it will delete the
	// remaining bitmap index files on disk that come after this int value. very
	// useful when updating the bitmap index after a delete was made
	public static void deleteTheRest(String tableName, int c) {
		File dir = new File(Table.getDirectoryPath() + "/data");
		File[] directoryListing = dir.listFiles();
		for (File file : directoryListing)
			if (file.getName().length() > tableName.length() + 12)
				if (file.getName().substring(0, tableName.length() + 12).equals(tableName + " bitmap page")) {
					String[] parts = file.getName().split("page ");
					Integer n = Integer.parseInt(parts[1]);
					if (n >= c)
						if (file.delete())
							System.out.println("Sorry, deleting bitmap file of page: " + n);
				}

	}

	// basically the toString method of this class
	public static void printIndex(String tableName) {
		System.out.println(
				"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		File dir = new File(Table.getDirectoryPath() + "/data");
		File[] directoryListing = dir.listFiles();
		for (File file : directoryListing)
			if (file.getName().length() > tableName.length() + 12)
				if (file.getName().substring(0, tableName.length() + 12).equals(tableName + " bitmap page")) {
					BitmapPage bp = (BitmapPage) BitmapPage.deSerialization(file);
					if (bp.tableName.equals(tableName))
						for (Index i : bp.indices)
							System.out.println(i.value + " = " + i.bitmap);

				}
		System.out.println(
				"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
	}

	public static boolean isClusteringKey(String tableName, String colName) {
		File dir = new File(Table.getDirectoryPath() + "/data");
		File[] directoryListing = dir.listFiles();
		for (File file : directoryListing)
			if (file.getName().equals(tableName)) {
				Table t = (Table) Table.deSerialization(file);
				if (t.key.equals(colName))
					return true;
			}
		return false;
	}
}
