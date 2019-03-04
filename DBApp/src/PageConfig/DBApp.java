package PageConfig;

import java.util.*;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DBApp {
	static Table table;

	public static void createTable(String tableName, String key, Hashtable<String, String> ht) {
		table = new Table(tableName, key, ht);
	}

	public static void insertIntoTable(String tableName, Hashtable<String, Object> ht) {
		table.insert(tableName, ht);
	}

	public static void deleteFromTable(String tableName, Hashtable<String, Object> ht) {
		table.delete(tableName, ht);
	}

	public static void main(String[] args) {

		String key = "id";
		Hashtable<String, String> ht = new Hashtable<String, String>();
		ht.put("id", "java.lang.Integer");
		ht.put("name", "java.lang.String");
		String strTableName = "testingg";

		createTable(strTableName, key, ht);

		Hashtable htblColNameValue = new Hashtable();

		htblColNameValue.put("id", new Integer(2343432));
		htblColNameValue.put("name", new String("Noor Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(453455));
		htblColNameValue.put("name", new String("Ahmed Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(5674567));
		htblColNameValue.put("name", new String("Dalia Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(23498));
		htblColNameValue.put("name", new String("John Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(78452));
		htblColNameValue.put("name", new String("Zaky Noor"));
		insertIntoTable(strTableName, htblColNameValue);

		// htblColNameValue.clear();
		// htblColNameValue.put("id", new Integer(23498));
		// htblColNameValue.put("name", new String("John Noor"));
		// deleteFromTable(strTableName, htblColNameValue);
		//
		// htblColNameValue.clear();
		// htblColNameValue.put("id", new Integer(5674567));
		// htblColNameValue.put("name", new String("Dalia Noor"));
		// deleteFromTable(strTableName, htblColNameValue);

	}
}
