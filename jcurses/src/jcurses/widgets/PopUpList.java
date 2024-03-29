package jcurses.widgets;

import jcurses.event.ValueChangedEvent;
import jcurses.event.ValueChangedListener;
import jcurses.event.ValueChangedListenerManager;

import jcurses.system.CharColor;
import jcurses.system.InputChar;
import jcurses.system.Toolkit;

import jcurses.util.Rectangle;

import java.util.Vector;

/**
 * This class implements a pop up list. Such list has always one of the items
 * selected and gives the possibility to change this selection ( through a
 * pop up menu that is shown, if the user pressed 'enter')
 */
public class PopUpList extends Widget {
	private Vector _items = new Vector();
	private static InputChar changeValueChar = new InputChar('\n');
	private ValueChangedListenerManager listenerManager =
			new ValueChangedListenerManager();
	private int selectedIndex = -1;

	/**
	 * Sets colors used to paint the widget, if it has focus
	 * 
	 * @param aColors
	 *            colors used to paint the widget, if it has focus
	 * @deprecated Use setSelectedColors()
	 */
	public void setFocusedPopUpColors(final CharColor aColors) {
		setSelectedColors(aColors);
	}

	/**
	 * The the colors used to paint the widget if it has focus.
	 * 
	 * @return focus widget colors
	 * @deprecated Use getSelectedColors()
	 */
	public CharColor getFocusedPopUpColors() {
		return getSelectedColors();
	}

	/**
	 * Sets the currently selected index
	 * 
	 * @param aIdx the index to select.
	 * 
	 * throws IndexOutOfBoundsException DOCUMENT ME!
	 */
	public void setSelectedIndex(final int aIdx) {
		if ((aIdx < -1) || (aIdx >= _items.size())) {
			throw new IndexOutOfBoundsException(aIdx
					+ " is not a valid index and can not be selected.");
		}

		selectedIndex = aIdx;
		repaint();
	}

	/**
	 * Returns the currently selected index
	 * 
	 * @return currently selected index
	 */
	public int getSelectedIndex() {
		if (selectedIndex != -1) {
			return selectedIndex;
		}

		if (_items.size() > 0) {
			return 0;
		}

		return -1;
	}

	/**
	 * Sets the currently selected item
	 * 
	 * @param aItem
	 *            DOCUMENT ME!
	 */
	public void setSelectedItem(final String aItem) {
		setSelectedIndex(_items.indexOf(aItem));
	}

	/**
	 * Returns the currently selected item
	 * 
	 * @return currently selected item
	 */
	public String getSelectedItem() {
		if (getSelectedIndex() >= 0) {
			return (String) _items.elementAt(getSelectedIndex());
		}

		return null;
	}

	/**
	 * Adds an item
	 * 
	 * @param item
	 *            the item to add
	 */
	public void add(final String item) {
		_items.add(item);
	}

	/**
	 * Adds an item at the specified position
	 * 
	 * @param pos
	 *            position
	 * @param item
	 *            the item to add
	 */
	public void add(final int pos, final String item) {
		_items.add(pos, item);
	}

	/**
	 * Adds a listener to register selected value changes
	 * 
	 * @param listener
	 *            DOCUMENT ME!
	 */
	public void addListener(final ValueChangedListener listener) {
		listenerManager.addListener(listener);
	}

	/**
	 * Clears the item list
	 */
	public void clear() {
		_items.clear();
	}

	/**
	 * Removes the first ocuurence of the specified item
	 * 
	 * @param item
	 *            item to be removed
	 */
	public void remove(final String item) {
		_items.remove(item);
	}

	/**
	 * Removes the item at the specified position
	 * 
	 * @param pos
	 *            position
	 */
	public void remove(final int pos) {
		_items.remove(pos);
	}

	/**
	 * Removes a listener
	 * 
	 * @param listener
	 *            DOCUMENT ME!
	 */
	public void removeListener(final ValueChangedListener listener) {
		listenerManager.removeListener(listener);
	}
	/**
	 * @return true always.
	 */
	protected boolean isFocusable() {
		return true;
	}
	/**
	 * @return a rectangle with the preferred size.
	 */
	protected Rectangle getPreferredSize() {
		return new Rectangle(2 + getMaxLength(), 1);
	}

	/**
	 * 
	 */
	protected void doPaint() {
		Rectangle rect = (Rectangle) getSize().clone();
		rect.setLocation(getAbsoluteX(), getAbsoluteY());

		String text = "[" + getText() + "]";
		CharColor colors = hasFocus() ? getFocusedPopUpColors() : getColors();
		Toolkit.printString(text, rect, colors);
	}
	/**
	 * 
	 */
	protected void doRepaint() {
		doPaint();
	}
	/**
	 * 
	 */
	protected void focus() {
		paint();
	}
	/**
	 * Handle the input for this pop up list.
	 * @param ch the input character
	 * @return true if the character could be handled properly.
	 */
	protected boolean handleInput(final InputChar ch) {
		if (ch.equals(changeValueChar)) {
			if (_items.size() > 1) {
				PopUpMenu menu =
						new PopUpMenu(getAbsoluteX(), getAbsoluteY(), null);

				for (int i = 0; i < _items.size(); i++)
					menu.add((String) _items.elementAt(i));

				menu.select(getSelectedIndex());
				menu.show();

				if ((menu.getSelectedIndex() != -1)
						&& (menu.getSelectedIndex() != getSelectedIndex())) {
					selectedIndex = menu.getSelectedIndex();
					paint();
					listenerManager.handleEvent(new ValueChangedEvent(this));
				}
			}

			return true;
		}

		return false;
	}
	/**
	 * 
	 */
	protected void unfocus() {
		paint();
	}

	private int getMaxLength() {
		int result = 0;

		for (int i = 0; i < _items.size(); i++) {
			String item = (String) _items.elementAt(i);

			if (item.length() > result) {
				result = item.length();
			}
		}

		return result;
	}

	private String getText() {
		String result = null;
		int length = getSize().getWidth() - 2;
		String item = (getSelectedItem() == null) ? "" : getSelectedItem();

		if (item.length() > length) {
			result = item.substring(0, length);
		} else {
			StringBuffer buf = new StringBuffer();
			buf.append(item);

			for (int i = 0; i < (length - item.length()); i++) {
				buf.append(' ');
			}

			result = buf.toString();
		}

		return result;
	}
}