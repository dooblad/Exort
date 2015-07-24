package client.util.dae;

import java.io.*;

public class TagReader {
	private BufferedReader reader;

	private String line;
	private int index;

	public TagReader(String URL) {
		try {
			this.reader = new BufferedReader(new FileReader(URL));
			this.read();
			this.index = 0;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the next XMLTag in the stream. If there are no more XMLTags, returns null.
	 */
	public XMLTag next() {
		if (!this.hasNext()) {
			return null;
		}

		XMLTag tag = new XMLTag(this.line, this.index);

		this.index = tag.end();

		return tag;
	}

	/**
	 * Returns true if there exists another XMLTag to read.
	 */
	private boolean hasNext() {
		if (this.line == null) {
			return false;
		}

		if (this.line.indexOf('<', this.index) != -1) {
			return true;
		} else {
			// Check the next line for tags... recursively!
			this.read();
			return this.hasNext();
		}
	}

	/**
	 * Reads the next line in "this.reader" into "this.line".
	 */
	private void read() {
		try {
			this.line = this.reader.readLine();
			this.index = 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the current line this TagReader is on.
	 */
	public String getLine() {
		return this.line;
	}

	/**
	 * Closes the stream and releases any system resources from it.
	 */
	public void close() {
		try {
			this.reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
