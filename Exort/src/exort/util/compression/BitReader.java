package exort.util.compression;

import java.util.*;

/**
 * Retrieves bits sequentially from a given data source.
 */
public class BitReader {
	private byte[] data;
	private int index;

	/**
	 * Creates a BitInputStream from "data".
	 */
	public BitReader(byte[] data) {
		this.data = data;
		this.index = 0;
	}

	/**
	 * Returns true if there is another bit to read.
	 */
	public boolean hasNextBit() {
		return (this.index / 8) < this.data.length;
	}

	/**
	 * Returns the next bit in this BitInputStream.
	 */
	public int nextBit() {
		if (!this.hasNextBit()) {
			throw new NoSuchElementException("No more bits to read.");
		}
		byte b = this.data[this.index / 8];
		int result = (b >> (7 - (this.index % 8))) & 1;
		this.index += 1;
		return result;
	}

	/**
	 * Returns the remaining contents of this BitReader as a String.
	 */
	public String restToString() {
		BitString result = new BitString();
		while (this.hasNextBit()) {
			result.add(this.nextBit());
		}
		return result.toString();
	}
}