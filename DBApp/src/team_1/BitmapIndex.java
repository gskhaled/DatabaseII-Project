package team_1;

import java.io.File;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

@SuppressWarnings("serial")
public class BitmapIndex implements Serializable {
	transient Vector<BitmapPage> pages;
	String colName;

	public BitmapIndex(String tableName, String colName) {
		this.pages = new Vector<BitmapPage>();
		this.colName = colName;

		Vector<Object> vector = createDenseIndexArray(tableName, colName);
		System.out.println(vector);
		vector = sortTheValues(vector); // the vector is now sorted!
		System.out.println(vector);
		Vector<Index> indexVector = setBitmap(tableName, colName, vector);

		this.pages.addElement(new BitmapPage(1));

		int p = 0; // counter for the number of pages
		// this loop just keeps adding indices to the bitmap index using
		// the vector v which is already sorted
		for (int i = 0; i < indexVector.size(); i++) {
			BitmapPage bp = this.pages.get(p);
			Index index = indexVector.get(i);
			if (!bp.isFull())
				bp.addIndexToPage(index);
			else {
				this.pages.addElement(new BitmapPage(getNumberOfFiles() + 1));
				p++;
				this.pages.get(p).addIndexToPage(index);
			}
		}
	}

	public Vector<Object> createDenseIndexArray(String tableName, String colName) {
		Vector<Object> a = new Vector<Object>();
		File dir = new File(Table.getDirectoryPath() + "/data/");
		File[] directoryListing = dir.listFiles();
		// loop over all files in the directory
		for (File file : directoryListing)
			if (file.getName().substring(0, 4).equals("file")) {
				// make sure to only check files!
				Page p = (Page) Table.deSerialization(file);
				if (p.tableName.equals(tableName)) // if the page belongs to this table
					for (int i = 0; i < p.getTuples().size(); i++) { // loop over all tuples in page
						Tuple t = p.getTuples().get(i);
						for (int j = 0; j < t.getAttributes().size(); j++) { // loop over all attributes in THIS tuple
							Attribute x = t.getAttributes().get(j);
							if (x.name.equals(colName))
								// if the attribute name is the colName i want to create bitmap index on, add it
								// to the returned array of values
								// also check if this value was added before as we only need unique values!
								if (!a.contains(x.value))
									a.add(x.value);
						}
					}
			}
		return a;
	}

	public Vector<Index> setBitmap(String tableName, String colName, Vector<Object> vector) {
		Vector<Index> indexVector = new Vector<Index>();
		Vector<Object> a = new Vector<Object>();
		File dir = new File(Table.getDirectoryPath() + "/data/");
		File[] directoryListing = dir.listFiles();
		// loop over all files in the directory
		// The whole purpose of this is to create a vector of objects that has ALL
		// values of the respective column
		for (File file : directoryListing)
			if (file.getName().substring(0, 4).equals("file")) {
				// make sure to only check files!
				Page p = (Page) Table.deSerialization(file);
				if (p.tableName.equals(tableName)) // if the page belongs to this table
					for (int i = 0; i < p.getTuples().size(); i++) { // loop over all tuples in page
						Tuple t = p.getTuples().get(i);
						for (int j = 0; j < t.getAttributes().size(); j++) {
							Attribute x = t.getAttributes().get(j);
							if (x.name.equals(colName))
								a.add(x.value);
						}
					}
			}

		for (Object o1 : vector) {
			Index x;
			if (o1.getClass().getName().equals("java.lang.Integer"))
				x = new Index(o1);
			else if (o1.getClass().getName().equals("java.lang.String"))
				x = new Index(o1);
			else if (o1.getClass().getName().equals("java.lang.Double"))
				x = new Index(o1);
			else
				x = new Index(o1);
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

	public Vector<Object> sortTheValues(Vector<Object> a) {
		Vector<Object> result = new Vector<Object>();

		if (a.firstElement().getClass().getName().equals("java.lang.Integer")) { // if the elements are of type Integer
			Vector<Integer> vector = new Vector<Integer>();
			// loop to empty vector of objects into a vector of ints
			for (Object o : a)
				vector.add((Integer) o);
			// sort the vector
			Collections.sort(vector);
			// now return the ints to objects and return this vector of objects
			for (Integer i : vector)
				result.add(i);
			return result;
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
				result.add(s);
			return result;
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
				result.add(d);
			return result;
		} else { // else the elements are of type Date
			Vector<Date> vector = new Vector<Date>();
			// loop to empty vector of objects into a vector of dates
			for (Object o : a)
				vector.add((Date) o);
			// sort the vector
			Collections.sort(vector);
			// now return the dates to objects and return this vector of objects
			for (Date d : vector)
				result.add(d);
			return result;
		}
	}

	// counts the number of files in the directory /data
	public int getNumberOfFiles() {
		File dir = new File(Table.getDirectoryPath() + "/data");
		File[] directoryListing = dir.listFiles();
		int count = 0;
		for (File file : directoryListing)
			if (file.getName().substring(0, 10).equals("bitmapFile"))
				count++;
		return count;
	}

}
