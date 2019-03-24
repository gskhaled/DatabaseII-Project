package team_1;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Index implements Serializable {

	Object value;
	String bitmap;

	public Index(Object value) {
		this.value = value;
		this.bitmap = "";
	}

	public void addToBitmap(char c) {
		this.bitmap += c;
	}
}
