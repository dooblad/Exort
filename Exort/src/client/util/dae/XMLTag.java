package client.util.dae;

import java.util.*;

import client.util.*;

/**
 * Represents a tag in XML format.
 */
public class XMLTag {
	private String name;
	private boolean closing, selfClosing;
	private Map<String, String> attributes;
	// Indexes on the line.
	private int tagStartIndex, tagEndIndex;

	/**
	 * Extracts the first XMLTag from "line".
	 */
	public XMLTag(String line) {
		this(line, 0);
	}

	public XMLTag(String line, int from) {
		this.tagStartIndex = line.indexOf('<', from);
		int nameStartIndex = this.tagStartIndex + 1;

		// Backslash indicates a closing tag, which doesn't contain attributes.
		if (line.charAt(nameStartIndex) == '/') {
			this.closing = true;
			// Remove the slash from the tag name.
			nameStartIndex += 1;
		} else {
			this.closing = false;
		}

		int nameEndIndex = FileReadingUtils.earliestIndex(line, nameStartIndex + 1, ' ', '/', '>');
		this.name = line.substring(nameStartIndex, nameEndIndex);

		this.tagEndIndex = line.indexOf('>', nameEndIndex);

		// Still need to initialize "this.attributes", even if it's closing (to prevent
		// NullPointerExceptions).
		this.attributes = new HashMap<String, String>();
		// Closing tags have no attributes.
		if (!this.closing) {
			// Attributes.
			int attributeStartIndex = nameEndIndex + 1;
			while (attributeStartIndex < this.tagEndIndex) {
				int split = line.indexOf('=', attributeStartIndex);
				// Find the first occurence of ' ' or '>' starting from "split".
				int attributeEnd = FileReadingUtils.earliestIndex(line, split, ' ', '/', '>');
				// Split + 2 to remove the equals sign and the quotes.
				this.attributes.put(line.substring(attributeStartIndex, split), line.substring(split + 2, attributeEnd - 1));
				attributeStartIndex = attributeEnd + 1;
			}
		}

		// Self-closing tags.
		if (line.charAt(this.tagEndIndex - 1) == '/') {
			this.selfClosing = true;
		}
	}

	/**
	 * Returns a String representation of this XMLTag.
	 */
	public String toString() {
		StringBuilder result = new StringBuilder();
		if (this.closing) {
			result.append("</");
		} else {
			result.append("<");
		}

		result.append(this.name);
		for (String attribute : this.attributes.keySet()) {
			result.append(" " + attribute + "=\"" + this.attributes.get(attribute) + "\"");
		}

		if (this.selfClosing) {
			result.append("/>");
		} else {
			result.append(">");
		}
		return result.toString();
	}

	/**
	 * Returns the index on the line that this tag starts at.
	 */
	public int start() {
		return this.tagStartIndex;
	}

	/**
	 * Returns the index on the line that this tag ends at.
	 */
	public int end() {
		return this.tagEndIndex;
	}

	/**
	 * Returns the value of the attribute with "name".
	 */
	public String getAttribute(String name) {
		return this.attributes.get(name);
	}

	public String getName() {
		return this.name;
	}

	/**
	 * Returns true if this is an opening tag.
	 */
	public boolean isOpening() {
		return !this.closing;
	}

	/**
	 * Returns true if this is a closing tag.
	 */
	public boolean isClosing() {
		return this.closing;
	}

	/**
	 * Returns true if this is a self-closing tag.
	 */
	public boolean isSelfClosing() {
		return this.selfClosing;
	}
}