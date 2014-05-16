/**
 * 
 */
package screen;

import util.Utils;

/**
 * Utility class for screen.
 * 
 * @author gbonnema
 * 
 */
public class Constructor {

	/**
	 * Private constructor to prevent instantiation.
	 */
	private Constructor() {
	}

	/**
	 * Builds a line start with one startchar en ending with an endchar, in the
	 * middle a midchar. Returns the result. Length must be at least 3.
	 * 
	 * You can use this to create a box, or just a line
	 * 
	 * @param startchar
	 *          The first character of the line.
	 * @param endchar
	 *          The last character of the line.
	 * @param midchar
	 *          The characters in the middle
	 * @param len
	 *          The length of the line.
	 * @return the resulting line as a String. Throws a runtime exception
	 *         IllegalArgumentsException if len is not large than 2.
	 */
	public static String fillLine(final char startchar, final char endchar,
			final char midchar, final int len) {
		Utils.checkArg(len > 2, "wrong length for filling a line.");
		StringBuilder result = new StringBuilder();
		result.append(startchar);
		for (int i = 1; i < len; i++) {
			result.append(midchar);
		}
		result.append(endchar);
		return result.toString();
	}

	public static String createHorLine(String ch, int length) {
		Utils.checkArg(ch.length() == 1, "String must have 1 character: " + ch);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(ch);
		}
		return sb.toString();
	}

}
