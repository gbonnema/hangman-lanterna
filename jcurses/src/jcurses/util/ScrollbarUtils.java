package jcurses.util;

import jcurses.system.CharColor;
import jcurses.system.Toolkit;

/**
 * This class is used be widgets to painting scroll bars. There isn't a scroll
 * bar widget in the library, instead of this, instead of this widget's that has
 * scroll bars as part, use this class for painting.
 */
public final class ScrollbarUtils {

	/**
	 * A private default constructor to prevent instantiation.
	 */
	private ScrollbarUtils() {
	}

	/**
	 * 
	 */
	public static final int HORIZONTAL = 0;
	/**
	 * 
	 */
	public static final int VERTICAL = 1;

	/**
	 * 
	 */
	private static final CharColor color = new CharColor(CharColor.BLACK,
			CharColor.WHITE, CharColor.REVERSE);

	/**
	 * The method to paint a scroll bar.
	 * 
	 * @param start
	 *            the start coordinate of the scroll bar (x or y dependent on
	 *            <code>alignment</code>
	 * @param end
	 *            the end coordinate of the scroll bar (x or y dependent on
	 *            <code>alignment</code>
	 * @param cst
	 *            the width (height) of the scroll bars line
	 * @param firstPart
	 *            the part of the scroll bar before the beam ( 0=> <1)
	 * @param lastPart
	 *            the part of the scroll bar after the beam ( 0=> <1)
	 * @param alignment
	 *            scroll bar alignment <code>HORIZONTAL</code> or
	 *            <code>VERTICAL</code>
	 */
	public static void drawScrollBar(final int start, final int end,
			final int cst, final float firstPart, final float lastPart,
			final int alignment) {
		if ((firstPart == 0) && (lastPart == 0)) {
			// no scroll bar necessary if all is visible
			return;
		}
		int length = end - start + 1;
		float barLength2 = (((float) (1.0 - firstPart - lastPart)) * length);
		int barLength = Math.round(barLength2);

		if (barLength == 0) {
			barLength = 1;
		}

		int firstIntervall = Math.round((firstPart * length));

		while ((barLength + firstIntervall) > (length)) {
			firstIntervall--;
		}

		if (lastPart == 0) {
			firstIntervall = (length - barLength);
		}

		if (alignment == HORIZONTAL) {
			Toolkit.drawHorizontalThickLine(start + firstIntervall, cst, start
					+ firstIntervall + barLength - 1, color);
		} else {
			Toolkit.drawVerticalThickLine(cst, start + firstIntervall, start
					+ firstIntervall + barLength - 1, color);
		}

	}

}