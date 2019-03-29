package team_1;

import java.util.Hashtable;
import java.util.Iterator;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DBAppTest {

	public static void main(String[] args) {
		DBApp.init();

		String key = "id";
		Hashtable<String, String> ht = new Hashtable<String, String>();
		ht.put("id", "java.lang.Integer");
		ht.put("name", "java.lang.String");
		ht.put("gpa", "java.lang.Double");
		String strTableName = "testing!!";

		DBApp.createTable(strTableName, key, ht);

		Hashtable htblColNameValue = new Hashtable();

		htblColNameValue.put("id", new Integer(400));
		htblColNameValue.put("name", new String("G Noor"));
		htblColNameValue.put("gpa", new Double(1.2));
		DBApp.insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(300));
		htblColNameValue.put("name", new String("H Noor"));
		htblColNameValue.put("gpa", new Double(1.1));
		DBApp.insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(100));
		htblColNameValue.put("name", new String("J Noor"));
		htblColNameValue.put("gpa", new Double(1.2));
		DBApp.insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(500));
		htblColNameValue.put("name", new String("F Noor"));
		htblColNameValue.put("gpa", new Double(5.0));
		DBApp.insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(200));
		htblColNameValue.put("name", new String("I Noor"));
		htblColNameValue.put("gpa", new Double(5.0));
		DBApp.insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(800));
		htblColNameValue.put("name", new String("B Noor"));
		htblColNameValue.put("gpa", new Double(1.2));
		DBApp.insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(700));
		htblColNameValue.put("name", new String("C Noor"));
		htblColNameValue.put("gpa", new Double(1.2));
		DBApp.insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(10));
		htblColNameValue.put("name", new String("K Noor"));
		htblColNameValue.put("gpa", new Double(21.8));
		DBApp.insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(50000));
		htblColNameValue.put("name", new String("A Noor"));
		htblColNameValue.put("gpa", new Double(1.2));
		DBApp.insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(5));
		htblColNameValue.put("name", new String("K Noor"));
		htblColNameValue.put("gpa", new Double(1.2));
		DBApp.insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(600));
		htblColNameValue.put("name", new String("E Noor"));
		htblColNameValue.put("gpa", new Double(1.2));
		DBApp.insertIntoTable(strTableName, htblColNameValue);

		htblColNameValue.clear();
		htblColNameValue.put("id", new Integer(650));
		htblColNameValue.put("name", new String("D Noor"));
		htblColNameValue.put("gpa", new Double(1.2));
		DBApp.insertIntoTable(strTableName, htblColNameValue);

		// ht.clear();
		// key = "name";
		// ht.put("id", "java.lang.Integer");
		// ht.put("name", "java.lang.String");
		// strTableName = "second testing!!";
		//
		// DBApp.createTable(strTableName, key, ht);
		//
		// htblColNameValue.clear();
		// htblColNameValue.put("id", new Integer(1));
		// htblColNameValue.put("name", new String("Catnis"));
		// DBApp.insertIntoTable(strTableName, htblColNameValue);
		//
		// htblColNameValue.clear();
		// htblColNameValue.put("id", new Integer(2));
		// htblColNameValue.put("name", new String("Badr"));
		// DBApp.insertIntoTable(strTableName, htblColNameValue);
		//
		// htblColNameValue.clear();
		// htblColNameValue.put("id", new Integer(3));
		// htblColNameValue.put("name", new String("Ahmed"));
		// DBApp.insertIntoTable(strTableName, htblColNameValue);
		//
		// System.out.println(".................................................");
		// DBApp.createBitmapIndex("second testing!!", "name");
		//
		// htblColNameValue.clear();
		// htblColNameValue.put("id", new Integer(123456789));
		// htblColNameValue.put("name", new String("UPDATED"));
		// DBApp.updateTable(strTableName, "Ahmed", htblColNameValue);

		System.out.println(".................................................");
		DBApp.createBitmapIndex("testing!!", "gpa");

		// htblColNameValue.clear();
		// htblColNameValue.put("id", new Integer(650));
		// htblColNameValue.put("name", new String("D Noor"));
		// DBApp.deleteFromTable("testing!!", htblColNameValue);

		BitmapIndex.printIndex("testing!!");

		System.out.println("**************************************************************************");
		SQLTerm[] arrSQLTerms = new SQLTerm[3];
		arrSQLTerms[0] = new SQLTerm("testing!!", "name", "=", "K Noor");
		arrSQLTerms[1] = new SQLTerm("testing!!", "id", ">", 1);
		arrSQLTerms[2] = new SQLTerm("testing!!", "gpa", ">=", 4.9);

		String[] strarrOperators = new String[2];
		strarrOperators[0] = "AND";
		strarrOperators[1] = "OR";

		// select * from Student where name = “K Noor” AND id > 1 OR gpa >= 4.9;
		Iterator resultSet = DBApp.selectFromTable(arrSQLTerms, strarrOperators);

		while (resultSet.hasNext()) {
			Tuple t = (Tuple) resultSet.next();
			System.out.print(t.getAttributes().get(0).value);
			System.out.println(" " + t.getAttributes().get(1).value);
		}
		System.out.println("Done");
	}

}
