package exort.util.compression;

import java.io.*;
import java.util.*;

/**
 * A better file compressor than the one that was given to me for this assignment.
 */
public class BetterHuffmanCompressor {
	/**
	 * Builds Huffman codes for the file at "input", then returns the HuffmanCode object.
	 */
	private static HuffmanCode buildCode(String input) {
		return new HuffmanCode(buildFrequencies(input));
	}

	/**
	 * Returns the compressed "input".
	 */
	public static byte[] compress(String input) {
		HuffmanCode code = buildCode(input);
		BitString result = new BitString();
		for (int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);
			
		}
		return null;
	}

	/**
	 * Returns the decompressed "input".
	 */
	public static String decompress(byte[] input) throws IOException {
		// TODO: WRITE IT

		// Separate codes from compressed part.
		HuffmanCode translator = null;
		return translator.translate(input);
	}

	/**
	 * Pre: "phrase" must not be empty. Otherwise, throws IllegalArgumentException.
	 * 
	 * Returns an integer array of the frequencies of ASCII characters in "phrase".
	 * Specifically, the number of each array index corresponds with the ASCII value, and
	 * the value at that array index corresponds with the frequency of that character.
	 */
	private static Map<Character, Integer> buildFrequencies(String phrase) {
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
		return frequencies;
	}
}
