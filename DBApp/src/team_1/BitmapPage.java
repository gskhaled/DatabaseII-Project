package team_1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

@SuppressWarnings("serial")
public class BitmapPage implements Serializable {

	Vector<Index> indices;
	int count;
	int capacity;
	int id;
	File file;

	String tableName;

	public BitmapPage(int num) {
		this.indices = new Vector<Index>();
		this.count = 0;

		String[] parts = readFile().split("= ");
		this.capacity = Integer.parseInt(parts[1]);

		this.id = num;
		this.file = new File("data/bitmapFile " + this.id);

		// this.tableName = tableName;
	}

	public void writePageFile() {
		try {
			FileOutputStream fileOut = new FileOutputStream(this.file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			System.out.println("Writing a file to represent a BITMAP PAGE: " + this.file.getName());
			out.writeObject(this);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addIndexToPage(Index i) {
		this.indices.add(i);
		this.count++;

		writePageFile();
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
