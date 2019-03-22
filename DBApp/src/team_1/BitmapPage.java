package team_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class BitmapPage {

	Vector<Index> indices;
	int capacity;
	int count;

	public BitmapPage() {
		this.indices = new Vector<Index>();
		this.count = 0;

		String[] parts = readFile().split("= ");
		this.capacity = Integer.parseInt(parts[1]);
	}

	public boolean isFull() {
		return count >= capacity;
	}

	public static String readFile() {
		try {
			BufferedReader br = new BufferedReader(
					new FileReader(Table.getDirectoryPath() + "/config/DBApp.properties"));
			String line = br.readLine();
			line = br.readLine();
			br.close();
			return line.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
