package PageConfig;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@SuppressWarnings("resource")
public class DBApp {

	public static void createTable(String name, String key, Hashtable<String, String> ht) {
		// getting the local path
		Path currentRelativePath = Paths.get("");
		String x = currentRelativePath.toAbsolutePath().toString();

		// create a directory (folder) with the given name inside the current local path
		new File(x + "/" + name).mkdir();

		// create metadata.csv
		File file = new File(name + "/metadata.csv");

		// write to the metadata.csv file the required info
		try {
			PrintWriter writer = new PrintWriter(file);
			Set<String> keys = ht.keySet();
			for (String element : keys) {
				writer.write(name + ", " + element + ", " + ht.get(element) + ", ");
				writer.write(element == key ? "True" : "False" + ", ");
				writer.write('\n');
			}
			writer.close();
			System.out.println("Done!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	public static void main(String[] args) {
//		String key = "id";
//		Hashtable<String, String> ht = new Hashtable<String, String>();
//		ht.put("id", "java.lang.Integer");
//		ht.put("name", "java.lang.String");
//		createTable("test table 2", key, ht);
//
//	}
}
