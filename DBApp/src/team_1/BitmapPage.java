package team_1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@SuppressWarnings("serial")
public class BitmapPage implements Serializable {

	Vector<Index> indices;
	int count;
	int capacity;
	int id;
	File file;

	String tableName;

	public BitmapPage(String tableName, int num) {
		this.indices = new Vector<Index>();
		this.count = 0;

		String[] parts = readFile().split("= ");
		this.capacity = Integer.parseInt(parts[1]);
		this.tableName = tableName;

		this.id = num;
		this.file = new File("data/" + tableName + " bitmap page " + this.id);
	}

	// this method writes a GZIP Output file, hence compressing bitmap pages
	public void writeBitmapPageFile() {
		try {
			FileOutputStream outputStream = new FileOutputStream(this.file);
			GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);
			// FileOutputStream fileOut = new FileOutputStream(this.file);
			ObjectOutputStream out = new ObjectOutputStream(gzipOutputStream);
			System.out.println("Writing a file to represent a BITMAP PAGE: " + this.file.getName());
			out.writeObject(this);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// this method de-serializes a bitmap page that is a GZIP
	public static Object deSerialization(File file) {
		try {
			FileInputStream fin = new FileInputStream(file);
			GZIPInputStream gis = new GZIPInputStream(fin);
			ObjectInputStream ois = new ObjectInputStream(gis);
			// FileInputStream fileInputStream = new FileInputStream(file);
			// BufferedInputStream bufferedInputStream = new
			// BufferedInputStream(fileInputStream);
			// ObjectInputStream objectInputStream = new
			// ObjectInputStream(bufferedInputStream);
			Object object = ois.readObject();
			ois.close();
			return object;
		} catch (ClassNotFoundException | IOException e) {
			System.out.println("Error in deSerialization......");
		}
		return null;
	}

	public void addIndexToPage(Index i) {
		this.indices.add(i);
		this.count++;

		writeBitmapPageFile();
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
