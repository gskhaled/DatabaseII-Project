package team_1;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Hashtable;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DBApp {
	static Table table;

	public static void init() {
		// create a directory (folder) with the name "DATA" inside the current local
		// path
		new File(Table.getDirectoryPath() + "/data").mkdir();
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

	public static void createTable(String tableName, String key, Hashtable<String, String> ht) {
		table = new Table(tableName, key, ht);
	}

	public static void insertIntoTable(String tableName, Hashtable<String, Object> ht) {
		table.insert(tableName, ht);
	}

	public static void deleteFromTable(String tableName, Hashtable<String, Object> ht) {
		table.delete(tableName, ht);
	}

	public static void updateTable(String tableName, String key, Hashtable<String, Object> ht) {
		table.update(tableName, key, ht);
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
		htblColNameValue.put("name", new String("Noor Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(3));
		htblColNameValue.put("name", new String("Ahmed"));
		htblColNameValue.put("batates", new String("batates"));
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

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(800));
		htblColNameValue.put("name", new String("Z Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(700));
		htblColNameValue.put("name", new String("K Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(600));
		htblColNameValue.put("name", new String("A Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(800));
		deleteFromTable(strTableName, htblColNameValue);

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

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(123456789));
		htblColNameValue.put("name", new String("UPDATED"));
		updateTable(strTableName, "Ahmed", htblColNameValue);

	}
}
