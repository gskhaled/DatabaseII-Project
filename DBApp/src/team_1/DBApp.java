package team_1;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DBApp {

	public static void init() {
		try {
			deleteDirectoryRecursion(new File(Table.getDirectoryPath() + "/data"));
		} catch (IOException e) {

		}
		// create a directory (folder) with the name "DATA" inside the current local
		// path
		new File(Table.getDirectoryPath() + "/data").mkdir();
	}

	public static void deleteDirectoryRecursion(File file) throws IOException {
		if (file.isDirectory()) {
			File[] entries = file.listFiles();
			if (entries != null) {
				for (File entry : entries) {
					deleteDirectoryRecursion(entry);
				}
			}
		}
		if (!file.delete()) {
			throw new IOException("Failed to delete " + file);
		}
	}

	public static void createTable(String tableName, String key, Hashtable<String, String> ht) {
		new Table(tableName, key, ht);
	}

	public static void insertIntoTable(String tableName, Hashtable<String, Object> ht) {
		Table.insert(tableName, ht);
	}

	public static void deleteFromTable(String tableName, Hashtable<String, Object> ht) {
		Table.delete(tableName, ht);
	}

	public static void updateTable(String tableName, String key, Hashtable<String, Object> ht) {
		Table.update(tableName, key, ht);
	}

	public static void createBitmapIndex(String strTableName, String strColName) {
		System.out.println(".................................................");
		new BitmapIndex(strTableName, strColName);
	}

	public static void main(String[] args) {
		init();

		String key = "id";
		Hashtable<String, String> ht = new Hashtable<String, String>();
		ht.put("id", "java.lang.Integer");
		ht.put("name", "java.lang.String");
		String strTableName = "testing!!";

		createTable(strTableName, key, ht);

		Hashtable htblColNameValue = new Hashtable();

		htblColNameValue.put("id", new Integer(400));
		htblColNameValue.put("name", new String("G Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(300));
		htblColNameValue.put("name", new String("H Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(100));
		htblColNameValue.put("name", new String("J Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(500));
		htblColNameValue.put("name", new String("F Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(200));
		htblColNameValue.put("name", new String("I Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(800));
		htblColNameValue.put("name", new String("B Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(700));
		htblColNameValue.put("name", new String("C Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(10));
		htblColNameValue.put("name", new String("K Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(50000));
		htblColNameValue.put("name", new String("A Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(5));
		htblColNameValue.put("name", new String("K Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(600));
		htblColNameValue.put("name", new String("E Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(650));
		htblColNameValue.put("name", new String("D Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		ht.clear();
		key = "name";
		ht.put("id", "java.lang.Integer");
		ht.put("name", "java.lang.String");
		strTableName = "second testing!!";

		createTable(strTableName, key, ht);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(1));
		htblColNameValue.put("name", new String("Catnis"));
		insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(2));
		htblColNameValue.put("name", new String("Badr"));
		insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(3));
		htblColNameValue.put("name", new String("Ahmed"));
		insertIntoTable(strTableName, htblColNameValue);

		System.out.println(".................................................");
		createBitmapIndex("second testing!!", "name");

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(123456789));
		htblColNameValue.put("name", new String("UPDATED"));
		updateTable(strTableName, "Ahmed", htblColNameValue);

		System.out.println(".................................................");
		createBitmapIndex("testing!!", "name");

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(650));
		htblColNameValue.put("name", new String("D Noor"));
		deleteFromTable("testing!!", htblColNameValue);

		BitmapIndex.printIndex("testing!!");
	}
}
