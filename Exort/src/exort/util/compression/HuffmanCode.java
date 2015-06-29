package exort.util.compression;

import java.io.*;
import java.util.*;

/**
 * Creates HuffmanCodes from letter frequencies, a Scanner, or a File.
 */
public class HuffmanCode {
	private HuffmanNode root;

	private Map<Character, String> charToCode;

	/**
	 * Pre: "phrase" must not be empty. Otherwise, throws IllegalArgumentException.
	 * 
	 * Creates Huffman codes from the of characters in "phrase".
	 */
	public HuffmanCode(String phrase) {
		if (phrase.isEmpty()) {
			throw new IllegalArgumentException("\"phrase\" must not be empty.");
		}
		Map<Character, Integer> frequencies = new HashMap<Character, Integer>();
		for (int i = 0; i < phrase.length(); i++) {
			char character = phrase.charAt(i);
			if (!frequencies.containsKey(character)) {
				frequencies.put(character, 0);
			}
			frequencies.put(character, frequencies.get(character) + 1);
		}
		Queue<HuffmanNode> queue = new PriorityQueue<HuffmanNode>();
		Iterator<Character> iterator = frequencies.keySet().iterator();
		while (iterator.hasNext()) {
			char ch = iterator.next();
			int n = frequencies.get(ch);
			// Add nonzero frequencies.
			if (n != 0) {
				queue.add(new HuffmanNode(ch, n));
			}
		}
		this.root = this.combineNodes(queue);
		this.charToCode = new HashMap<Character, String>();
		this.mapCodes();
	}

	/**
	 * Maps every character in the Huffman tree to its corresponding code.
	 */
	private void mapCodes() {
		this.mapCodes(this.root, new StringBuilder());
	}

	/**
	 * Traverses from the "current" node until it finds a character, then adds it to the
	 * "charToCode" map using the accumulated "code" as an entry.
	 */
	private void mapCodes(HuffmanNode current, StringBuilder code) {
		if (current.character != null) {
			this.charToCode.put(current.character, code.toString());
		} else {
			// Recursively backtrack.
			code.append('0');
			this.mapCodes(current.left, code);
			code.setCharAt(code.length() - 1, '1');
			this.mapCodes(current.right, code);
			code.deleteCharAt(code.length() - 1);
		}
	}

	/**
	 * Recursively combines the first two elements in "queue" to form a tree of Huffman
	 * codes. Returns the root node of the completed tree.
	 */
	private HuffmanNode combineNodes(Queue<HuffmanNode> queue) {
		if (queue.size() == 1) {
			// The remaining element is the completed tree.
			return queue.remove();
		} else {
			// Take first two elements.
			HuffmanNode first = queue.remove();
			HuffmanNode second = queue.remove();
			// Add their frequencies together.
			int sum = first.frequency + second.frequency;
			// Create a new node that branches to these nodes.
			HuffmanNode node = new HuffmanNode(sum, first, second);
			// Send it back to the queue.
			queue.add(node);
			return this.combineNodes(queue);
		}
	}

	/**
	 * Recursively returns a HuffmanNode defined by the contents of "input".
	 */
	private HuffmanNode readCodes(HuffmanNode current, char n, String code, Scanner input) {
		if (code.length() == 0) { // Reached the location of the code.
			current = new HuffmanNode(n);
		} else {
			if (current == null) { // Rebuild the structural nodes.
				current = new HuffmanNode();
			}
			// Check the first charcter.
			char ch = code.charAt(0);
			code = code.substring(1);
			// Go down the '0' or '1' branch.
			if (ch == '0') {
				current.left = this.readCodes(current.left, n, code, input);
			} else {
				current.right = this.readCodes(current.right, n, code, input);
			}
		}
		return current;
	}

	/**
	 * Writes the Huffman codes for each character to "output" (in standard format).
	 */
	public void save(PrintStream output) {
		this.save(this.root, "", output);
	}

	/**
	 * Starting at "current", recurses through the tree and accumulates the bit patterns
	 * into "code". Once a node with a character is reached, the character and the "code"
	 * are written to "output".
	 */
	private void save(HuffmanNode current, String code, PrintStream output) {
		if (current.character != null) {
			// Print in standard format.
			output.println(current.character);
			output.println(code);
		} else {
			this.save(current.left, code + "0", output);
			this.save(current.right, code + "1", output);
		}
	}

	/**
	 * Returns the decompressed String from "input".
	 */
	public String translate(byte[] input) {
		BitInputStream bits = new BitInputStream(input);
		while (bits.hasNextBit()) {
			HuffmanNode current = this.root;
			while (current.character == null) {
				if (bits.nextBit() == 0) {
					current = current.left;
				} else {
					current = current.right;
				}
			}
		}
	}

	/**
	 * Returns the Huffman code associated with "ch".
	 */
	public String getCode(char ch) {
		return this.charToCode.get(ch);
	}
}
