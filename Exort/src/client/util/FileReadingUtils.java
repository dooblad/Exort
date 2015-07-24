package client.util;

@SuppressWarnings("unused")
public class FileReadingUtils {
	/**
	 * Returns true if "ch" is a numeric character.
	 */
	public static boolean isNumber(char ch) {
		return (ch >= 48 && ch <= 57);
	}

	/**
	 * Returns true if "ch" is a numeric character or a component of a number('-' for
	 * negatives, '.' for decimals, and 'e' for scientific notation). "length" is used to
	 * give context on whether the components of a number make sense (e.g. 'e' doesn't
	 * make sense in a number if there is no numerical character preceding it).
	 */
	public static boolean isNumber(char ch, StringBuilder number) {
		// Checks if "ch" is a literal number, a '-' at the beginning of the number or
		// after an 'e', or a '.' or 'e' in the middle of the number.
		return (ch >= 48 && ch <= 57) || (ch == '-' && (number.length() == 0 || number.charAt(number.length() - 1) == 'e'))
				|| ((ch == '.' || ch == 'e') && number.length() != 0);
	}

	/**
	 * Returns the first number found, starting at the index "from". If "s" does not
	 * contain a number at or after "from", returns 0f.
	 */
	public static Number extractNumber(String s, int from) {
		return extractNumbers(s, from, 1)[0];
	}

	/**
	 * Extracts numbers until the end of "s" is reached or "n" numbers have been
	 * extracted. If the end of "s" is reached before "n" numbers have been extracted, the
	 * indices of the missing numbers will remain untouched (initialized to 0f).
	 */
	public static Number[] extractNumbers(String s, int n) {
		return extractNumbers(s, 0, n);
	}

	/**
	 * Extracts numbers (starting from the index "from") until the end of "s" is reached
	 * or "n" numbers have been extracted.If the end of "s" is reached before "n" numbers
	 * have been extracted, the indices of the missing numbers will remain untouched
	 * (initialized to 0f).
	 */
	public static Number[] extractNumbers(String s, int from, int n) {
		Number[] numbers = new Number[n];
		int arrayIndex = 0;

		// Skip to the first number.
		int stringIndex = firstNumericIndex(s, from);

		// Da number assembly line.
		StringBuilder number = new StringBuilder();
		while (stringIndex < s.length() && arrayIndex < n) {
			char ch = s.charAt(stringIndex);
			if (isNumber(ch, number)) {
				number.append(ch);
			} else if (number.length() != 0) {
				numbers[arrayIndex] = Float.parseFloat(number.toString());
				arrayIndex += 1;
				number = new StringBuilder();
			}
			stringIndex += 1;
		}
		// If the number goes to the end of "s", it won't be processed in the loop. So we
		// process it here.
		if (number.length() != 0) {
			numbers[arrayIndex] = Float.parseFloat(number.toString());
		}

		return numbers;
	}

	/**
	 * Returns the first index in "s" that represents a number or a component of a number.
	 */
	private static int firstNumericIndex(String s) {
		return firstNumericIndex(s, 0);
	}

	/**
	 * Returns the first index in "s", starting from the index "from", that represents a
	 * number or a component of a number.
	 */
	private static int firstNumericIndex(String s, int from) {
		for (int i = from; i < s.length(); i++) {
			char ch = s.charAt(i);
			// If there is an '-' but no number following, then disregard it.
			if (ch == '-') {
				if (i < s.length() - 1 && isNumber(s.charAt(i + 1))) {
					return i;
				}
			} else if (isNumber(ch)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns the first index at which any of the characters in "chars" occurs in "s",
	 * starting the search at the index "from". Returns -1 if neither are found.
	 */
	public static int earliestIndex(String s, int from, char... chars) {
		for (int i = from; i < s.length(); i++) {
			char ch = s.charAt(i);
			for (int j = 0; j < chars.length; j++) {
				if (ch == chars[j]) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * Returns the first index at which any of the characters in "chars" occurs in "s".
	 * Returns -1 if neither are found.
	 */
	public static int earliestIndex(String s, char... chars) {
		return earliestIndex(s, 0, chars);
	}
}
