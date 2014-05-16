/**
 * 
 */
package util;

import java.util.ArrayList;

/**
 * @author gbonnema
 * 
 */
public final class Utils {

	/* prevent instantiation */
	private Utils() {
	}

	/**
	 * Check the condition is true, if not throw an ExperimentException.
	 * 
	 * @param cond
	 *          The condition
	 * @param message
	 *          The error message.
	 * @throws ExperimentException
	 *           The exception to be thrown if the condition is false.
	 */
	public static void check(boolean cond, String message)
			throws ExperimentException {
		if (!cond) {
			throw new ExperimentException(message);
		}
	}

	/**
	 * Check the condition is true, if not throw an IllegalArgumentException. This
	 * is a runtime error that does not need to be declared.
	 * 
	 * @param cond
	 *          The condition
	 * @param message
	 *          The error message.
	 * @throws IllegalArgumentException
	 *           The exception to be thrown if the condition is false.
	 */
	public static void checkArg(boolean cond, String message) {
		if (!cond) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Execute an internal consistency check on a specified condition. If the
	 * condition is not true, throw an internal error.
	 * 
	 * @param cond
	 *          The specified condition.
	 * @param message
	 *          The specified message when throwing the internal error.
	 */
	public static void checkInternal(boolean cond, String message) {
		if (!cond) {
			throw new InternalError(message);
		}
	}

	/**
	 * Check the variable is not null, otherwise an ExperimentException
	 * 
	 * @param cond
	 *          The condition
	 * @param varName
	 *          The variable name
	 * @throws ExperimentException
	 *           The exception
	 */
	public static void checkNotNull(Object obj, String varName)
			throws ExperimentException {
		if (obj == null) {
			String msg = varName + " should not be null.";
			throw new ExperimentException(msg);
		}
	}

	public static String convert2Hex(String s) {
		char[] hexsymbols = "0123456789ABCDEF".toCharArray();
		StringBuilder result = new StringBuilder();
		byte[] byteArray = s.getBytes();
		String glue = "";
		for (int i = 0; i < byteArray.length; i++) {
			int x = byteArray[i] & 0xff;
			char right = hexsymbols[x & 0x0f];
			char left = hexsymbols[x >> 4];
			result.append(glue).append(left).append(right);
			glue = " ";
		}
		return result.toString();
	}

	private static String disperse(ArrayList<Character> chArr, String pre,
			String mid, String post) {
		StringBuilder result = new StringBuilder();
		result.append(pre).append(chArr.get(0));
		for (int i = 1; i < chArr.size(); i++) {
			result.append(mid).append(chArr.get(i));
		}
		result.append(post);
		return result.toString();
	}

	public static String disperse(ArrayList<Character> chArr) {
		return disperse(chArr, "", " ", "");
	}

	public static String disperse(String s) {
		String pre = "";
		String mid = " ";
		String post = "";
		return disperse(s, pre, mid, post);
	}

	public static String disperse(String s, String pre, String mid, String post) {
		ArrayList<Character> chArr = new ArrayList<>();
		for (byte b : s.getBytes()) {
			char ch = (char) b;
			chArr.add(ch);
		}
		return disperse(chArr, pre, mid, post);
	}

}
