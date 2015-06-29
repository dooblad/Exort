/**
 * Provides the ability to write individual bits to a file in a compact form.
 * One major limitation of this approach is that the resulting file will always
 * have a number of bits that is a multiple of 8. In effect, whatever bits are
 * output to the file are padded at the end with 0's to make the total number of
 * bits a multiple of 8.
 */

import java.io.*;
import java.util.*;

public class BitOutputStream {
    private PrintStream output;
    private List<Integer> buffer;
    private int currentByte; // A buffer used to build up next set of digits.
    private int numBits; // How many digits are currently in the buffer.
    private boolean debug; // Set to true to write ASCII 0s and 1s rather than
			   // bits.

    private static final int BYTE_SIZE = 8; // Digits per byte.

    /**
     * Pre: The file at "fileName" must exist. Otherwise, throws
     * FileNotFoundException.
     * 
     * Creates a BitOutputStream that sends output to the file at
     * "fileName".
     */
    public BitOutputStream(String fileName) throws FileNotFoundException {
	this(new PrintStream(fileName));
    }

    /**
     * Creates a BitOutputStream that sends output to "output".
     */
    public BitOutputStream(PrintStream output) {
	this(output, false);
    }

    /**
     * Creates a BitOutputStream sending output to the given stream. If
     * "debug" == true, bits are printed as ASCII 0s and 1s.
     */
    public BitOutputStream(PrintStream output, boolean debug) {
	this.buffer = new ArrayList<Integer>();
	this.output = output;
	this.debug = debug;
    }

    /**
     * Writes given bit to output.
     */
    public void write(int bit) {
	if (debug) {
	    System.out.print(bit);
	}
	if (bit < 0 || bit > 1) {
	    throw new IllegalArgumentException("Illegal bit: " + bit);
	}
	currentByte |= bit << (BYTE_SIZE - this.numBits - 1);
	this.numBits++;
	if (this.numBits == BYTE_SIZE) {
	    buffer.add(this.currentByte);
	    this.numBits = 0;
	    this.currentByte = 0;
	}
    }

    /**
     * "output" is closed.
     */
    public void close() {
	int remaining = BYTE_SIZE - this.numBits;

	if (remaining == 8) {
	    remaining = 0;
	}

	// Flush the last byte (if there is one).
	if (remaining > 0) {
	    buffer.add(this.currentByte);
	}

	/*
	 * Now that we've received all the output, prepend it with the number of
	 * missing bits from the end.
	 */
	output.write(remaining);
	for (int b : this.buffer) {
	    this.output.write(b);
	}
	output.close();
    }

    /**
     * Ensures the stream is closed when garbage collection occurs.
     */
    protected void finalize() {
	this.close();
    }
}
