package shared.util.compression;

import java.util.*;

/**
 * Creates HuffmanCodes from letter frequencies, compresses Strings, and decompresses byte
 * arrays.
 */
public class HuffmanCompressor {
	private HuffmanNode root;

	// Fast mapping from a character to its Huffman code in the form of a bit String.
	private Map<Character, String> charToCode;

	/**
	 * Creates a HuffmanCompressor without any initial Huffman tree.
	 */
	public HuffmanCompressor() {

	}

	/**
	 * Pre: "phrase" must not be empty. Otherwise, throws IllegalArgumentException.
	 *
	 * Creates Huffman codes from the of characters in "phrase".
	 */
	public HuffmanCompressor(String phrase) {
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
	 * Returns the decompressed String from "input".
	 */
	public String translate(byte[] input) {
		StringBuilder result = new StringBuilder();
		BitReader bits = new BitReader(input);
		while (bits.hasNextBit()) {
			HuffmanNode current = this.root;
			while (current.character == null) {
				if (bits.nextBit() == 0) {
					current = current.left;
				} else {
					current = current.right;
				}
			}
			result.append(current.character);
		}
		return result.toString();
	}

	/**
	 * Returns the compressed "input".
	 */
	public byte[] compress(String input) {
		BitString result = new BitString();
		for (int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);
			result.add(this.charToCode.get(ch));
		}
		return result.toByteArray();
	}

	/**
	 * Returns the decompressed "input", if decompression is required. Otherwise, returns
	 * the input (except for the first compression flag bit) as a String.
	 */
	public String decompress(byte[] input) {
		BitReader reader = new BitReader(input);
		if (reader.nextBit() == 0) { // Compressed flag is 0.
			return reader.restToString();
		} else {
			return this.translate(input);
		}
	}
}