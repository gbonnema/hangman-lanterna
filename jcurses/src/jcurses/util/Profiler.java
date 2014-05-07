package jcurses.util;

/**
 * This a library-intern class for performance measurement. The class isn't
 * needed to develop jcurses applications.
 */
public final class Profiler {

	/**
	 * Private default constructor to prevent instantiation.
	 */
	private Profiler() {
	}

	/**
	 * The array containing the starting times of TODO: of what?
	 */
	private static long[] marks = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };

	/**
	 * Set a mark for the current time in milliseconds.
	 * Throws a runtime exception if the index is out of bounds.
	 * 
	 * @param index the index into marks
	 */
	public static void setMark(final int index) {
		
		if (!(index >= 0 && index < marks.length)) {
			throw new RuntimeException("illegal index");
		}
		marks[index] = System.currentTimeMillis();
	}
	
	/**
	 * Calculate running time and issue a debugging message
	 * with the result.
	 * 
	 * @param message The message saying how long the process ran.
	 * @param index the index of the mark being calculated.
	 */
	public static void time(final String message, final int index) {
		
		long time = System.currentTimeMillis() - marks[index];
		Protocol.debug(message + ": " + time);

	}
	/**
	 * Retrieve the mark for a given index.
	 * @param index the index
	 * @return the mark on the specified index.
	 */
	public static long getMarkTime(final int index) {
		if (!(index >= 0 && index < marks.length)) {
			throw new RuntimeException("illegal index");
		}
		return marks[index];
	}

}
