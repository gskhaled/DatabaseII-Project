package PageConfig;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

public class Table {

	static String tableName;
	Vector<Page> pages;
	int numOfPages;

	public Table(String name, String key, Hashtable<String, String> ht) {
		tableName = name;
		this.pages = new Vector<Page>();
		// getting the local path
		Path currentRelativePath = Paths.get("");
		String x = currentRelativePath.toAbsolutePath().toString();

		// create a directory (folder) with the given name inside the current local path
		new File(x + "/" + name).mkdir();

		// create metadata.csv
		File file = new File(name + "/metadata.csv");

		// write into the metadata.csv file the required info
		try {
			PrintWriter writer = new PrintWriter(file);
			Set<String> keys = ht.keySet();
			for (String element : keys) {
				writer.write(name + ", " + element + ", " + ht.get(element) + ", ");
				writer.write((element == key ? "True" : "False") + ", ");
				writer.write('\n');
			}
			writer.close();
			System.out.println("Done!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getName() {
		return tableName;
	}

	public void insert(String tableName, Hashtable<String, Object> ht) {
		Set<String> keys = ht.keySet();
		// vector set of attributes
		Vector<Attribute> toFill = new Vector<Attribute>();
		for (String k : keys)
			toFill.addElement(new Attribute(k, ht.get(k)));

		Page latestPage;
		// initially, pages would be empty, so I add one page
		if (pages.isEmpty()) {
			this.numOfPages = 1;
			this.pages.addElement(new Page(1));
			System.out.println("added a page because pageS was empty! called it 1");
			latestPage = this.pages.get(0);
		}
		// otherwise I get the latest page to see if it has space
		else {
			int size = pages.size();
			// getting the last page in the pages vector
			latestPage = pages.get(size - 1);
		}

		// whether the last page still has space
		if (!latestPage.isFull()) {
			System.out.println("found space in page number: " + numOfPages + " so inserting here");
			Tuple tuple = new Tuple(toFill);
			latestPage.addContentToPage(tuple);
		}
		// means last page is full, so we make a new one, then add to it the tuple
		else {
			this.numOfPages++;
			System.out.println("last page was full so i made a new one and numbered it: " + numOfPages + " ..then added tuple to it");
			Page page = new Page(numOfPages);
			Tuple tuple = new Tuple(toFill);
			page.addContentToPage(tuple);
			pages.addElement(page);
		}
	}
}
