package client.util.compression;

/**
 * Represents a node in a tree of Huffman codes.
 */
public class HuffmanNode implements Comparable<HuffmanNode> {
	// These use wrapper classes so null values can be represented.
	public final Character character;
	public final Integer frequency;
	public HuffmanNode left, right;

	/**
	 * Creates an empty HuffmanNode.
	 */
	public HuffmanNode() {
		this(null, null, null, null);
	}

	/**
	 * Creates a HuffmanNode with "character".
	 */
	public HuffmanNode(Character character) {
		this(character, null, null, null);
	}

	/**
	 * Creates a HuffmanNode with "character" and "frequency".
	 */
	public HuffmanNode(Character character, Integer frequency) {
		this(character, frequency, null, null);
	}

	/**
	 * Creates a HuffmanNode with "frequency" that leads to "left" and "right".
	 */
	public HuffmanNode(Integer frequency, HuffmanNode left, HuffmanNode right) {
		this(null, frequency, left, right);
	}

	/**
	 * Creates a HuffmanNode with "character" and "frequency" that leads to "left" and
	 * "right".
	 */
	public HuffmanNode(Character character, Integer frequency, HuffmanNode left, HuffmanNode right) {
		this.character = character;
		this.frequency = frequency;
		this.left = left;
		this.right = right;
	}

	/**
	 * Makes this class comparable with other HuffmanNodes. Nodes with lower frequencies
	 * are considered less than nodes with higher frequencies.
	 */
	public int compareTo(HuffmanNode other) {
		return this.frequency.compareTo(other.frequency);
	}
}