package team_1;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DBApp {
	static Table table;

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
	
	public static void createTable(String tableName, String key, Hashtable<String, String> ht) {
		table = new Table(tableName, key, ht);
	}

	public static void insertIntoTable(String tableName, Hashtable<String, Object> ht) {
		table.insert(tableName, ht);
		table.writePageFile();
	}

	public static void deleteFromTable(String tableName, Hashtable<String, Object> ht) {
		table.delete(tableName, ht);
		table.writePageFile();
	}

	public static void updateTable(String tableName, String key, Hashtable<String, Object> ht) {
		table.update(tableName, key, ht);
		table.writePageFile();
	}
	
	public static void printTable() {
		for (int e = 0; e<table.pages.size(); e++) {
			Page p = table.pages.get(e);
			System.out.println("I am now in page #: " + p.id);
			for (int r = 0; r<p.tuples.size(); r++) {
				Tuple t = p.tuples.get(r);
				System.out.println(t.getAttributes().get(1).value);
			}
		}
	}

	public static void main(String[] args) {
		String key = "id";
		Hashtable<String, String> ht = new Hashtable<String, String>();
		ht.put("id", "java.lang.Integer");
		ht.put("name", "java.lang.String");
		String strTableName = "testingg";

		createTable(strTableName, key, ht);

		Hashtable htblColNameValue = new Hashtable();

		htblColNameValue.put("id", new Integer(400));
		htblColNameValue.put("name", new String("Noor Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(300));
		htblColNameValue.put("name", new String("Ahmed Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(500));
		htblColNameValue.put("name", new String("Dalia Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(100));
		htblColNameValue.put("name", new String("John Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(200));
		htblColNameValue.put("name", new String("Zaky Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		// htblColNameValue.clear();
		// htblColNameValue.put("id", new Integer(23498));
		// deleteFromTable(strTableName, htblColNameValue);
		 
		// htblColNameValue.clear();
		// htblColNameValue.put("id", new Integer(5674567));
		// deleteFromTable(strTableName, htblColNameValue);

		// htblColNameValue.clear();
		// htblColNameValue.put("id", new Integer(5674567));
		// htblColNameValue.put("name", new String("UPDATED NOOR"));
		// updateTable(strTableName, "5674567", htblColNameValue);
		
		printTable();
		
		Page.readFile();

	}
}
