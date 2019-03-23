package team_1;

import java.io.File;
import java.util.Hashtable;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DBApp {

	public static void init() {
		// create a directory (folder) with the name "DATA" inside the current local
		// path
		new File(Table.getDirectoryPath() + "/data").mkdir();
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
		htblColNameValue.put("id", new Integer(100));
		htblColNameValue.put("name", new String("John Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(450));
		htblColNameValue.put("name", new String("Dalia Noor"));
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
		htblColNameValue.put("id", new Integer(10));
		htblColNameValue.put("name", new String("hgdghfg Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(600));
		htblColNameValue.put("name", new String("A Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		// htblColNameValue.clear();
		// htblColNameValue.put("id", new Integer(800));
		// htblColNameValue.put("name", new String("Z Noor"));
		// deleteFromTable(strTableName, htblColNameValue);

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

		System.out.println(".................................................");
		createBitmapIndex(strTableName, "name");

		System.out.println(".................................................");
		createBitmapIndex("testing!!", "id");
	}
}
