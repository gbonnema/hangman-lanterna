package jcurses.widgets;

import java.util.Vector;

import jcurses.event.ActionEvent;
import jcurses.event.ActionListener;
import jcurses.event.ActionListenerManager;
import jcurses.system.CharColor;
import jcurses.system.InputChar;
import jcurses.system.Toolkit;
import jcurses.themes.Theme;
import jcurses.util.Rectangle;

/**
 * This class implements a button-widget. Such button has a label and is
 * 'clicked' by user typing a special character (default 'enter'). If it is
 * 'clicked', it generates an <code>ActionEvent</code>, that is delegated to
 * registered listeners.
 */
public class Button extends Widget {
	/**
	 * The enter generates this special character ('\n'). 
	 */
	private static InputChar actionChar = new InputChar('\n');
	/**
	 * the listener manager.
	 */
	private ActionListenerManager listenerManager =
			new ActionListenerManager();
	/**
	 * 
	 */
	private InputChar shortCut = null;
	/**
	 * label of the button.
	 */
	private String label = null;

	/**
	 * The constructor fo the button.
	 * 
	 * @param aLabel
	 *            button's label
	 */
	public Button(final String aLabel) {
		setLabel(aLabel);
	}

	/**
	 * Sets button's label.
	 * 
	 * @param aLabel
	 *            buttton's label
	 */
	public final void setLabel(final String aLabel) {
		label = aLabel;
	}

	/**
	 * get the label.
	 * 
	 * @return button's label.
	 */
	public final String getLabel() {
		return label;
	}

	/**
	 * @return The focusedButtonColors value
	 * @deprecated Use getSelectedColors()
	 */
	public final CharColor getFocusedButtonColors() {
		return getSelectedColors();
	}

	/**
	 * @return The focusedButtonDefaultColors value
	 * @deprecated Use getDefaultSelectedColors()
	 */
	public final CharColor getFocusedButtonDefaultColors() {
		return getDefaultSelectedColors();
	}

	/**
	 * @param aColor
	 *            The new focusedButtonColors value
	 * @deprecated Use setSelectedColors()
	 */
	public final void setFocusedButtonColors(final CharColor aColor) {
		setSelectedColors(aColor);
	}

	/**
	 * @return colors button's shortcut char's colors
	 */
	public final CharColor getShortCutColors() {
		return getColors(Theme.COLOR_WIDGET_SHORTCUT);
	}

	/**
	 * Sets button's shortcut char's colors. If the button has a shortcut char
	 * and this char is contained by the label, than the char within the label
	 * will be painted in different colors, set by this method.
	 * 
	 * @param aColor
	 *            button's shortcut char's colors
	 */
	public final void setShortCutColors(final CharColor aColor) {
		setColors(Theme.COLOR_WIDGET_SHORTCUT, aColor);
	}

	/**
	 * Adds a listener to the button.
	 * 
	 * @param listener
	 *            listener to add
	 */
	public final void addListener(final ActionListener listener) {
		listenerManager.addListener(listener);
	}

	/**
	 * Removes a listener from the button.
	 * 
	 * @param listener
	 *            listener to remove
	 */
	public final void removeListener(final ActionListener listener) {
		listenerManager.removeListener(listener);
	}

	/**
	 * Set's button's shortcut char. If this shortcut is typed, then the button
	 * will handle the char, as described by <code>Widget</code>, and generate
	 * an Event as if the button were 'clicked'.
	 * 
	 * @param c
	 *            The new shortCut value
	 */
	public final void setShortCut(final char c) {
		shortCut = new InputChar(c);
	}

	/**
	 * Gets the preferredSize attribute of the Button object.
	 * 
	 * @return The preferredSize value
	 */
	protected final Rectangle getPreferredSize() {
		final int padding = 4;
		return new Rectangle(label.length() + padding, 1);
	}

	/**
	 * Paint self.
	 */
	protected final void doPaint() {
		// System.err.println("Button<" + _label + ">.paint()");
		if (hasFocus()) {
			Toolkit.printString("< " + label + " >", getRectangle(),
					getSelectedColors());

		} else {
			Toolkit.printString("< " + label + " >", getRectangle(),
					getActionColors());

			drawShortCutIfNeeded();
		}
	}

	/**
	 * Gets the shortCutList attribute of the Button object.
	 * 
	 * TODO: check out Vector : could it be generic? Needs more research.
	 * 
	 * @return The shortCutList value
	 */
	protected final Vector getShortCutList() {
		if (getShortCut() == null) {
			return null;
		}
		Vector result = new Vector<>();
		result.add(getShortCut());
		return result;
	}

	/**
	 * Gets the focusable attribute of the Button object.
	 * 
	 * @return The focusable value
	 */
	protected final boolean isFocusable() {
		return true;
	}

	/**
	 * Input handler,just looks for the shortcut char and performs the action if
	 * the shortcut char is encountered.
	 * 
	 * @param ch
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	protected final boolean handleInput(final InputChar ch) {
		if ((ch.equals(actionChar))
				|| ((getShortCut() != null) && (getShortCut().equals(ch)))) {
			doAction();
			return true;
		}

		return false;
	}

	/**
	 * Handle getting focus, paint self.
	 */
	protected final void focus() {
		doPaint();
	}

	/**
	 * Description of the Method.
	 */
	protected final void unfocus() {
		doPaint();
	}

	/**
	 * Gets the shortCut character of the Button object.
	 * 
	 * @return The shortCut value
	 */
	private InputChar getShortCut() {
		return shortCut;
	}

	/**
	 * Description of the Method.
	 */
	private void drawShortCutIfNeeded() {
		InputChar theShortCut = getShortCut();
		if (theShortCut != null) {
			String c = theShortCut.toString();
			if (label != null) {
				int index = label.toLowerCase().indexOf(c.toLowerCase());
				if (index != -1) {
					Toolkit.printString(label.substring(index, index + 1),
							getAbsoluteX() + index + 2, getAbsoluteY(),
							getShortCutColors());

				}
			}
		}
	}

	/**
	 * Description of the Method.
	 */
	private void doAction() {
		listenerManager.handleEvent(new ActionEvent(this));
	}

}
