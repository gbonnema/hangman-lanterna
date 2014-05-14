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

}
