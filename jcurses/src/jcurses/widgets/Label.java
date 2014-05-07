package jcurses.widgets;

import jcurses.system.CharColor;
import jcurses.system.Toolkit;
import jcurses.util.Rectangle;
import jcurses.util.TextUtils;

/**
 * This class implements a label widget.
 */
public class Label extends Widget {
	/**
	 * 
	 */
	private String label = null;

	/**
	 * The constructor.
	 * 
	 * 
	 * @param aLabel
	 *            label's text
	 * @param aColors
	 *            colors label's colors
	 * 
	 */
	public Label(final String aLabel, final CharColor aColors) {
		this(aLabel);
		setColors(aColors);
	}

	/**
	 * Gets the text of the Label.
	 * 
	 * @return The text value
	 */
	public final String getText() {
		return label;
	}

	/**
	 * Sets the text of the Label.
	 * 
	 * @param aText
	 *            The new text value
	 */
	public final void setText(final String aText) {
		label = aText;
		if (label == null) {
			label = "";
		}
	}

	/**
	 * The constructor which makes a Label of a String.
	 * 
	 * 
	 * @param aLabel
	 *            label's text
	 */
	public Label(final String aLabel) {
		setText(aLabel);
	}

	/**
	 * Calculates the preferred size of the Label.
	 * 
	 * @return The preferred size
	 */
	protected final Rectangle getPreferredSize() {
		String[] mLines = TextUtils.wrapLines(label, Integer.MAX_VALUE);

		int mWide = 0;
		for (int mIdx = 0; mIdx < mLines.length; mIdx++) {
			mWide = Math.max(mWide, mLines[mIdx].length());
		}

		return new Rectangle(mWide, mLines.length);
	}

	/**
	 * The interface method that draws the label in its rectangle and in its
	 * colors.
	 */
	protected final void doPaint() {
		Toolkit.printString(label, getRectangle(), getColors());
	}
}
