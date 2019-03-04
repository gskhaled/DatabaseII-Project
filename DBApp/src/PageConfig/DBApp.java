package PageConfig;

import java.util.*;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DBApp {
	static Table table;

	public static void createTable(String tableName, String key, Hashtable<String, String> ht) {
		table = new Table(tableName, key, ht);
	}

	public static void insertIntoTable(String tableName, Hashtable<String, Object> ht) {
		table.newInsert(tableName, ht);
	}

	public static void deleteFromTable(String tableName, Hashtable<String, Object> ht) {
		table.delete(tableName, ht);
	}

	public static void updateTable(String tableName, String key, Hashtable<String, Object> ht) {
		table.update(tableName, key, ht);
	}
	
	public static void printTable() {
		for (Page p : table.pages)
			for (Tuple t : p.getTuples())
				for (int i = 0; i < t.getAttributes().size(); i++)
					System.out.println(t.getAttributes().get(i).value);
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

	}
}
