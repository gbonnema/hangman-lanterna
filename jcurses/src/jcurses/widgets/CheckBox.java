package jcurses.widgets;

import jcurses.event.ValueChangedEvent;
import jcurses.event.ValueChangedListener;
import jcurses.event.ValueChangedListenerManager;
import jcurses.system.CharColor;
import jcurses.system.InputChar;
import jcurses.system.Toolkit;
import jcurses.util.Rectangle;

/**
 * This class implements a checkBox widget. This checkBoxes state is modified by
 * typing a special char (default 'space'). You can register listeners to this
 * widget to track state changes.
 */
public class CheckBox extends Widget {
	/**
	 * The default width of the checkbox.
	 */
	private static final int DEFAULT_WIDTH = 3;
	/**
	 * The default height of the checkbox.
	 */
	private static final int DEFAULT_HEIGHT = 1;
	
	/**
	 * the change status character (default ' ') to tick or untick box.
	 */
	private static InputChar changeStatusChar = new InputChar(' ');
	/**
	 * true if the box is ticked.
	 */
	private boolean boxChecked = false;
	/**
	 * value changed listener manager.
	 */
	private ValueChangedListenerManager listenerManager =
			new ValueChangedListenerManager();

	/**
	 * @deprecated Use getDefaultSelectedColors()
	 * @return the default selected colors.
	 */
	public final CharColor getFocusedCheckboxDefaultColors() {
		return getDefaultSelectedColors();
	}

	/**
	 * @deprecated Use getSelectedColors()
	 * @return checkBox colors, if it is focused
	 */
	public final CharColor getFocusedCheckboxColors() {
		return getSelectedColors();
	}

	/**
	 * Sets colors of the checkBox in focused state.
	 * 
	 * @deprecated Use setSelectedColors()
	 * @param aColor
	 *            checkbox colors, if it is focused
	 */
	public final void setFocusedCheckboxColors(final CharColor aColor) {
		setSelectedColors(aColor);
	}

	/**
	 * Adds listener to the checkbox to track states changes.
	 * 
	 * @param listener
	 *            listener to add
	 */
	public final void addListener(final ValueChangedListener listener) {
		listenerManager.addListener(listener);
	}

	/**
	 * Removes listener from the checkbox.
	 * 
	 * @param listener
	 *            to remove
	 */
	public final void removeListener(final ValueChangedListener listener) {
		listenerManager.removeListener(listener);
	}

	/**
	 * The constructor.
	 * 
	 * @param checked
	 *            true, if the checkbox is checked at first time, false
	 *            otherwise
	 */
	public CheckBox(final boolean checked) {
		boxChecked = checked;
	}

	/**
	 * The default constructor creates an unchecked checkbox.
	 */
	public CheckBox() {
		this(false);

	}

	/**
	 * @return true, if the checkbox is checked , false otherwise.
	 */
	public final boolean getValue() {
		return boxChecked;
	}

	/**
	 * Sets checkbox value.
	 * 
	 * @param value
	 *            if the checkbox becomes checked , false otherwise
	 */
	public final void setValue(final boolean value) {
		boolean oldValue = boxChecked;
		boxChecked = value;
		if (oldValue != boxChecked) {
			listenerManager.handleEvent(new ValueChangedEvent(this));
		}
		paint();
	}
	/**
	 * @return a rectangle with the preferred sizes.
	 */
	protected final Rectangle getPreferredSize() {
		return new Rectangle(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	/**
	 * paint self.
	 */
	protected final void doPaint() {
		Rectangle rect = (Rectangle) getSize().clone();
		rect.setLocation(getAbsoluteX(), getAbsoluteY());
		char mark = ' ';
		if (boxChecked) {
			mark = 'X';
		}
		String text = "[" + mark + "]";
		CharColor colors = getColors();
		if (hasFocus()) {
			colors = getSelectedColors();
		}
		Toolkit.printString(text, rect, colors);
	}

	/**
	 * @return true always.
	 */
	protected final boolean isFocusable() {
		return true;
	}

	/**
	 * Paint self.
	 */
	protected final void doRepaint() {
		doPaint();
	}

	/**
	 * @param ch the input character
	 * @return true if character == changeStatusChar
	 */
	protected final boolean handleInput(final InputChar ch) {
		if (ch.equals(changeStatusChar)) {
			setValue(!(boxChecked));		// reverse check mark
			paint();
			return true;
		}
		return false;
	}

	/**
	 * Show focus on the checkbox.
	 */
	protected final void focus() {
		changeColors();
	}
	/**
	 * Remove focus from the checkbox (change colors).
	 */
	protected final void unfocus() {
		changeColors();
	}

	/**
	 * Do a repaint to change colors.
	 */
	private void changeColors() {
		// CharColor colors = hasFocus() ? getFocusedCheckboxColors() :
		// getColors();
		// Toolkit.changeColors(getRectangle(), colors);
		doRepaint();
	}

}