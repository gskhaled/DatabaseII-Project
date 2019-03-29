package team_1;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;

@SuppressWarnings("rawtypes")
public class DBApp {

	// THIS METHOD HAS TO BE THE FIRST THING CALLED IN THE MAIN METHOD.
	public static void init() {
		// try {
		// deleteDirectoryRecursion(new File(Table.getDirectoryPath() + "/data"));
		// } catch (IOException e) {
		//
		// }
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

	public static Iterator selectFromTable(SQLTerm[] arrSQLTerms, String[] starOperators) {
		Iterator resultSet = Table.selectFromTable(arrSQLTerms, starOperators);
		return resultSet;
	}

}
