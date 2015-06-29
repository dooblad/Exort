/**
 * Provides the ability to read individual bits to a file in a compact
 * form. One major limitation of this approach is that the resulting file will
 * always have a number of bits that is a multiple of 8. In effect, whatever
 * bits are output to the file are padded at the end with 0's to make the
 * total number of bits a multiple of 8.
 */

import java.io.*;
import java.util.*;

public class BitInputStream {
    private FileInputStream input;
    private int currentByte; // Current set of bits (buffer).
    private int nextByte; // Next set of bits (buffer).
    private int numBits; // How many bits from buffer have been used.
    private int remainingAtEnd; // How many bits will be remaining at the end,
				// after we're done.

    private static final int BYTE_SIZE = 8; // bits per byte

    /**
     * Pre : Given "fileName" is legal.
     * 
     * Creates a BitInputStream reading input from the file.
     */
    public BitInputStream(String fileName) {
	try {
	    input = new FileInputStream(fileName);

	    // Read in the number of remaining bits at the end.
	    this.remainingAtEnd = input.read();

	    // Set up the nextByte field.
	    this.nextByte = input.read();
	} catch (IOException e) {
	    throw new RuntimeException(e.toString());
	}

	this.nextByte();
    }

    /**
     * Returns true if not at the end of the file.
     */
    public boolean hasNextBit() {
	boolean atEnd = this.currentByte == -1;
	boolean onlyRemaining = this.nextByte == -1
		&& BYTE_SIZE - this.numBits == this.remainingAtEnd;
	return !atEnd && !onlyRemaining;
    }

    /**
     * Pre: "input" is not at end of file. Otherwise, throws
     * NoSuchElementException.
     * 
     * Reads next bit from "input" (-1 if at end of file).
     */
    public int nextBit() {
	if (!this.hasNextBit()) {
	    throw new NoSuchElementException();
	}
	int result = (this.currentByte >> (BYTE_SIZE - this.numBits - 1)) & 1;
	this.numBits++;
	if (this.numBits == BYTE_SIZE) {
	    this.nextByte();
	}
	return result;
    }

    /**
     * Refreshes the internal buffer with the next "BYTE_SIZE" bits.
     */
    private void nextByte() {
	this.currentByte = this.nextByte;
	if (this.currentByte != -1) {
	    try {
		this.nextByte = input.read();
	    } catch (IOException e) {
		throw new RuntimeException(e.toString());
	    }
	}

	this.numBits = 0;
    }

    /**
     * Closes "input".
     */
    public void close() {
	try {
	    input.close();
	} catch (IOException e) {
	    throw new RuntimeException(e.toString());
	}
    }

    /**
     * Ensures the stream is closed when garbage collection occurs.
     */
    protected void finalize() {
	close();
    }
}