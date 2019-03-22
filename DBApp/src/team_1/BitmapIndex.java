package team_1;

import java.util.Vector;

public class BitmapIndex {
	Vector<BitmapPage> pages;
	String colName;

	public BitmapIndex(String colName) {
		this.pages = new Vector<BitmapPage>();
		this.colName = colName;
	}

}
