/**
 * 
 */
package util;

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
	 *            The condition
	 * @param message
	 *            The error message.
	 * @throws ExperimentException
	 *             The exception to be thrown if the condition is false.
	 */
	public static void check(boolean cond, String message)
			throws ExperimentException {
		if (!cond) {
			throw new ExperimentException(message);
		}
	}

	/**
	 * Check the variable is not null, otherwise an ExperimentException
	 * 
	 * @param cond
	 *            The condition
	 * @param varName
	 *            The variable name
	 * @throws ExperimentException
	 *             The exception
	 */
	public static void checkNotNull(Object obj, String varName)
			throws ExperimentException {
		if (obj == null) {
			String msg = varName + " should not be null.";
			throw new ExperimentException(msg);
		}
	}

}
