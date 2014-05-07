package jcurses.widgets;

import java.util.Vector;

import jcurses.event.ItemEvent;
import jcurses.event.ItemListener;
import jcurses.event.ItemListenerManager;
import jcurses.system.CharColor;
import jcurses.system.InputChar;
import jcurses.system.Toolkit;
import jcurses.themes.Theme;
import jcurses.util.Paging;
import jcurses.util.Rectangle;

/**
 * This class implements a list widget to select and 'invoke' one ore more
 * items. Listeners can be registered to track selecting or deselecting and
 * 'invoking' of items.
 */
public class List extends Widget implements IScrollable {
	/**
	 * 
	 */
	private static InputChar changeStatusChar = new InputChar(' ');
	/**
	 * 
	 */
	private static InputChar callItemChar = new InputChar('\n');
	/**
	 * 
	 */
	private ItemListenerManager listenerManager = new ItemListenerManager();
	/**
	 * 
	 */
	private ScrollbarPainter scrollbars = null;
	/**
	 * Internal fields.
	 */
	private String title = null;

	private Vector itemVector = new Vector();
	private Vector selectedVector = new Vector();

	private boolean multiple = false;
	private boolean selectable = true;
	/**
	 * 
	 */
	private int startIndex = 0;
	/**
	 * 
	 */
	private int startPos = 0;
	/**
	 * 
	 */
	private int trackedIndex = 0;
	/**
	 * 
	 */
	private int visibleSize = -1;

	/**
	 * The constructor.
	 * 
	 * @param aVisibleSize
	 *            number of visible items. If the entire number of items is
	 *            more, the widget scrolls items 'by a window'. If -1 is given,
	 *            than the visible size is defined dependent of the layout size,
	 *            that is, the widget has no preferred y size.
	 * @param aMultiple
	 *            true, if more as one items can be selected a time, false, if
	 *            only one item can be selected at a time, in this case
	 *            selecting of an item causes deselecting of the previous
	 *            selected item.
	 */
	public List(final int aVisibleSize, final boolean aMultiple) {
		visibleSize = aVisibleSize;
		multiple = aMultiple;
		scrollbars = new ScrollbarPainter(this);
	}

	/**
	 * The constructor.
	 * 
	 * @param aVisibleSize
	 *            number of visible items. If the entire number of items is
	 *            more, the widget scrolls items 'by a window'. If -1 is given,
	 *            than the visible size is defined dependent of the layout size,
	 *            that is, the widget has no preferred y size.
	 */
	public List(final int aVisibleSize) {
		this(aVisibleSize, false);
	}

	/**
	 * The constructor.
	 */
	public List() {
		this(-1, false);
	}

	/**
	 * @return a copy of the current border rectangle.
	 */
	public final Rectangle getBorderRectangle() {
		Rectangle rect = (Rectangle) getSize().clone();
		rect.setLocation(getAbsoluteX(), getAbsoluteY());
		return rect;
	}

	/**
	 * @return the length of the scrollbar.
	 */
	public final float getHorizontalScrollbarLength() {
		if ((itemVector.size() == 0) || (getMaxLength() <= getLineWidth())) {
			// no items, no scrollbar
			return 0;
		}

		if (getMaxLength() > getLineWidth()) {
			return ((float) getLineWidth()) / ((float) getMaxLength());
		}

		return 0;
	}

	/**
	 * @return the offset of the scrollbar or zero when no items are there.
	 */
	public final float getHorizontalScrollbarOffset() {
		if ((itemVector.size() == 0) || (getMaxLength() <= getLineWidth())) {
			// no items, no scrollbar.
			return 0;
		}

		if (getMaxLength() > getLineWidth()) {
			return ((float) startPos) / ((float) getMaxLength());
		}

		return 0;
	}

	/**
	 * Returns the String value of the item at the specified position.
	 * 
	 * @param index
	 *            specified position
	 * 
	 * @return item at the specified position
	 */
	public final String getItem(final int index) {
		return (String) itemVector.elementAt(index);
	}

	/**
	 * Set an item at a specified index.
	 * 
	 * @param index
	 *            the index.
	 * @param aItem
	 *            the item.
	 */
	public final void setItem(final int index, final String aItem) {
		if (!aItem.equals(itemVector.get(index))) {
			itemVector.set(index, aItem);
			reset();
			if (isVisible()) {
				redrawItem(index);
			}
		}
	}

	/**
	 * Returns the items in the list as a Vector.
	 * 
	 * @return items contained in the list
	 */
	public final Vector getItems() {
		return (Vector) itemVector.clone();
	}

	/**
	 * Returns the number of items in the list.
	 * 
	 * @return number of items
	 */
	public final int getItemsCount() {
		return itemVector.size();
	}

	/**
	 * @return scrollbar colors.
	 */
	public final CharColor getScrollbarColors() {
		return getColors(Theme.COLOR_WIDGET_SCROLLBAR);
	}

	/**
	 * Sets, whether items can be selected at all.
	 * 
	 * @param value
	 *            true, if items can be selected, false otherwise ( in this case
	 *            items can only be 'invoked')
	 */
	public final void setSelectable(final boolean value) {
		selectable = value;
	}

	/**
	 * Sets, whether items can be selected at all.
	 * 
	 * @return true, if items can be selected, false otherwise ( in this case
	 *         items can only be 'invoked')
	 */
	public final boolean getSelectable() {
		return selectable;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param pos
	 *            the position to test, whether selected
	 * 
	 * @return true, if the item at the specified position is selected, false
	 *         otherwise
	 */
	public final boolean isSelected(final int pos) {
		return ((Boolean) selectedVector.elementAt(pos)).booleanValue();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return index of the selected item, if only one item is selected,
	 *         <code>null</code> otherwise.
	 */
	public final int getSelectedIndex() {
		int[] results = getSelectedIndexes();
		int result = -1;

		if (results.length == 1) {
			result = results[0];
		}

		return result;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return indexes of all selected items, contained in the list
	 */
	public final int[] getSelectedIndexes() {
		int size = 0;

		for (int i = 0; i < itemVector.size(); i++) {
			boolean selected = isSelected(i);

			if (selected) {
				size++;
			}
		}

		int[] indexArray = new int[size];
		int currentIndex = 0;

		for (int i = 0; i < itemVector.size(); i++) {
			boolean selected = isSelected(i);

			if (selected) {
				indexArray[currentIndex] = i;
				currentIndex++;
			}
		}

		return indexArray;
	}

	/**
	 * Gets the String value of the currently selected item in the List. If
	 * multiple items are selected or no items are selected, null is returned.
	 * 
	 * @return the selected item, if exactly one item is selected,
	 *         <code>null</code> otherwise.
	 */
	public final String getSelectedItem() {
		Vector results = getSelectedItems();
		String result = null;

		if (results.size() == 1) {
			result = (String) results.elementAt(0);
		}

		return result;
	}

	/**
	 * Sets colors used painting selected items.
	 * 
	 * @param aColors
	 *            colors used painting selected items
	 * @deprecated Use setSelectedColors()
	 */
	public final void setSelectedItemColors(final CharColor aColors) {
		setSelectedColors(aColors);
	}

	/**
	 * @return colors used painting selected items.
	 * @deprecated Use getSelectedColors()
	 */
	public final CharColor getSelectedItemColors() {
		return getSelectedColors();
	}

	/**
	 * Gets a Vector of all the selected items for the list. If no items are
	 * selected, an empty Vector is returned.
	 * 
	 * @return all selected items, contained in the list
	 */
	public final Vector getSelectedItems() {
		Vector result = new Vector();

		for (int i = 0; i < itemVector.size(); i++) {
			boolean selected = isSelected(i);

			if (selected) {
				result.add(itemVector.elementAt(i));
			}
		}

		return result;
	}

	/**
	 * Sets the title of the list.
	 * 
	 * @param aTitle
	 *            the title of the list
	 */
	public final void setTitle(final String aTitle) {
		title = aTitle;
	}

	/**
	 * Gets the title of the list.
	 * 
	 * @return list's title
	 */
	public final String getTitle() {
		return title;
	}

	/**
	 * Gets the index of the currently tracked item. This is where the 'cursor'
	 * line is when the user is navigating the list.
	 * 
	 * @return the index of the current tracked item.
	 */
	public final int getTrackedIndex() {
		return trackedIndex;
	}

	/**
	 * Sets the currently tracked item. This is where the 'cursor' line is when
	 * the user is navigating the list.
	 * 
	 * Throws IllegalArgumentException if pos is out of range.
	 * 
	 * @param pos
	 *            the index of the current tracked item.
	 * 
	 */
	public final void setTrackedItem(final int pos) {
		if ((pos < 0) || (pos >= getItemsCount())) {
			throw new IllegalArgumentException("pos must be in the range: 0,"
					+ (getItemsCount() - 1));
		}

		int backupStartIndex = startIndex;
		int backupTrackedIndex = trackedIndex;

		if (setTrack(pos) && isVisible()) {
			redraw((backupStartIndex == startIndex), trackedIndex,
					backupTrackedIndex);
		}
	}

	/**
	 * This is currently the same as a call to getTrackedIndex(), however that
	 * is inconsistent with the rest of the API. It should return instead the
	 * String value of the item. <br>
	 * <br>
	 * Currently it is simply being marked deprecated to avoid causing people a
	 * lot of grief, however in a future release, it will be changed to return
	 * the correct String value. In the meantime, getTrackedItemStr() has been
	 * introduced to provide the function of returning the String value.
	 * 
	 * @return the index of the current tracked item.
	 * 
	 * @deprecated use {@link getTrackedItemStr}.
	 */
	public final int getTrackedItem() {
		return trackedIndex;
	}

	/**
	 * Gets the String value of the currently tracked item. <br>
	 * <br>
	 * This method is an iterim solution to avoid breaking existing code since
	 * really getTrackedItem() should serve this purpose.
	 * 
	 * @return the index of the current tracked item.
	 */
	public final String getTrackedItemStr() {
		return getItem(trackedIndex);
	}

	/**
	 * @return the scrollbar length.
	 */
	public final float getVerticalScrollbarLength() {
		if (itemVector.size() == 0) {
			// no items, no scrollbar.
			return 0;
		}

		if (itemVector.size() > getVisibleSize()) {
			return ((float) getVisibleSize()) / ((float) itemVector.size());
		}

		return 0;
	}

	/**
	 * @return vertical scrollbar offset.
	 */
	public final float getVerticalScrollbarOffset() {
		if (itemVector.size() == 0) {
			// no items, no scrollbar.
			return 0;
		}

		if (itemVector.size() > getVisibleSize()) {
			return ((float) startIndex) / ((float) itemVector.size());
		}

		return 0;
	}

	/**
	 * Adds an item to the list at the specified position.
	 * 
	 * @param pos
	 *            the position to insert the item
	 * @param item
	 *            item to add
	 */
	public final void add(final int pos, final String item) {
		itemVector.add(pos, item);
		selectedVector.add(pos, new Boolean(false));
		reset();
		if (isVisible(pos)) {
			refresh();
		}
	}

	/**
	 * Adds an item to the end of the list.
	 * 
	 * @param item
	 *            item to add
	 */
	public final void add(final String item) {
		add(itemVector.size(), item);
	}

	/**
	 * Adds a listener to the widget.
	 * 
	 * @param listener
	 *            listener to add
	 */
	public final void addListener(final ItemListener listener) {
		listenerManager.addListener(listener);
	}

	/**
	 * Removes all items from the list.
	 */
	public final void clear() {
		itemVector.clear();
		selectedVector.clear();
		reset();
		refresh();
	}

	/**
	 * Deselects an item at the specified position.
	 * 
	 * @param index
	 *            position
	 */
	public final void deselect(final int index) {
		select(index, false);

		if (isVisible()) {
			redrawItemBySelecting(index);
		}

		dispatchEvent(index, false);
	}

	// Scrollbars
	/**
	 * @return true always
	 */
	public final boolean hasHorizontalScrollbar() {
		return true;
	}

	/**
	 * @return true always
	 */
	public final boolean hasVerticalScrollbar() {
		return true;
	}

	/**
	 * Removes an item from the list at the specified position.
	 * 
	 * @param pos
	 *            position
	 */
	public final void remove(final int pos) {
		itemVector.remove(pos);
		selectedVector.remove(pos);
		reset();
		if (isVisible()) {
			refresh();
		}
	}

	/**
	 * Removes the first occurrence of <code>item</code> from the list.
	 * 
	 * @param item
	 *            string, whose first occurrence is to remove from the list.
	 */
	public final void remove(final String item) {
		int index = itemVector.indexOf(item);

		if (index != -1) {
			itemVector.remove(index);
			selectedVector.remove(index);
			if (isVisible()) {
				refresh();
			}
		}
	}

	/**
	 * Removes a listener from the widget.
	 * 
	 * @param listener
	 *            listener to remove
	 */
	public final void removeListener(final ItemListener listener) {
		listenerManager.removeListener(listener);
	}

	/**
	 * Selects an item at the specified position.
	 * 
	 * @param index
	 *            position
	 */
	public final void select(final int index) {
		select(index, true);

		if (isVisible()) {
			redrawItemBySelecting(index);
		}

		dispatchEvent(index, true);
	}

	/**
	 * TODO: document what this means.
	 * 
	 * @return the change status character.
	 */
	protected final InputChar getChangeStatusChar() {
		return changeStatusChar;
	}

	/**
	 * @return true always.
	 */
	protected final boolean isFocusable() {
		return true;
	}

	/**
	 * The method returns the display representation of the string und is called
	 * by the widget before it paints an item. The idea is to make it possible
	 * in derived classes to paint other strings as managed in the widget. Here
	 * returns always the same string as <code>item</code>.
	 * 
	 * @param item
	 *            string to give display representation
	 * 
	 * @return display representation of the string
	 */
	protected String getItemRepresentation(final String item) {
		return item;
	}

	/**
	 * @return the preferred size as a rectangle.
	 */
	protected Rectangle getPreferredSize() {
		int rectWidth = -1;
		if (visibleSize >= 0) {
			rectWidth = visibleSize + 2;
		}
		return new Rectangle(-1, rectWidth);
	}

	/**
	 * This method tests, if the item at the specified position can be selected
	 * and invoked at all. The reason is, to give derived classes the
	 * opportunity to implement 'separators'. This method always returns
	 * <code>true</code>.
	 * 
	 * @param i
	 *            the position to test
	 * 
	 * @return true if the item at the specified position can be selected and
	 *         invoked, false otherwise : This means always true. TODO: why?
	 */
	protected boolean isSelectable(final int i) {
		return true;
	}

	/**
	 * set the position.
	 * 
	 * @param pos position
	 * @return .
	 */
	protected boolean setTrack(final int pos) {
		return findNextSelectableItem(pos, 1, false, 0);
	}

	/**
	 * paint self.
	 */
	protected void doPaint() {
		Rectangle rect = (Rectangle) getSize().clone();
		rect.setLocation(getAbsoluteX(), getAbsoluteY());
		Toolkit.drawBorder(rect, getBorderColors());
		drawTitle();
		scrollbars.paint();
		drawRectangle();
		drawItems();
	}

	/**
	 * paint self.
	 */
	protected void doRepaint() {
		doPaint();
	}

	/**
	 * redraw the selected items. TODO: why is this called focus?
	 */
	protected void focus() {
		redrawSelectedItems();
	}

	/**
	 * This method checks and executes the requested action
	 * from the specified InputChar.
	 * 
	 * @param ch the input character
	 * @return true if valid action requested, false otherwise.
	 */
	protected boolean handleInput(final InputChar ch) {
		int backupStartIndex = startIndex;
		int backupTrackedIndex = trackedIndex;

		// no items, no input
		if (itemVector.size() == 0) {
			return false;
		}

		if (ch.getCode() == InputChar.KEY_RIGHT) {
			if (incrementStartPos()) {
				refresh();
			}

			return true;
		} else if (ch.getCode() == InputChar.KEY_LEFT) {
			if (decrementStartPos()) {
				refresh();
			}

			return true;
		} else if (ch.getCode() == InputChar.KEY_UP) {
			if (decrementTrack()) {
				redraw((backupStartIndex == startIndex), trackedIndex,
						backupTrackedIndex);
			}

			return true;
		} else if (ch.getCode() == InputChar.KEY_DOWN) {
			if (incrementTrack()) {
				redraw((backupStartIndex == startIndex), trackedIndex,
						backupTrackedIndex);
			}

			return true;
		} else if (ch.getCode() == InputChar.KEY_HOME) {
			if (setTrack(0)) {
				redraw((backupStartIndex == startIndex), trackedIndex,
						backupTrackedIndex);
			}

			return true;
		} else if (ch.getCode() == InputChar.KEY_END) {
			if (setTrack(getItemsCount() - 1)) {
				redraw((backupStartIndex == startIndex), trackedIndex,
						backupTrackedIndex);
			}

			return true;
		} else if (ch.getCode() == InputChar.KEY_NPAGE) {
			if (incrementPage()) {
				redraw((backupStartIndex == startIndex), trackedIndex,
						backupTrackedIndex);
			}

			return true;
		} else if (ch.getCode() == InputChar.KEY_PPAGE) {
			if (decrementPage()) {
				redraw((backupStartIndex == startIndex), trackedIndex,
						backupTrackedIndex);
			}

			return true;
		} else if (ch.equals(changeStatusChar) && getSelectable()) {
			if (isSelected(trackedIndex)) {
				deselect(trackedIndex);
			} else {
				select(trackedIndex);
			}

			return true;
		} else if (ch.equals(callItemChar)) {
			callItem(trackedIndex);
			return true;
		}

		return false;
	}

	/**
	 * 
	 */
	protected void unfocus() {
		redrawSelectedItems();
	}

	/**
	 * get the current page offset.
	 * 
	 * @return the current page offset.
	 */
	protected int getCurrentPageOffset() {
		return getPaging().getPageOffset(trackedIndex);
	}

	/**
	 * Get the page end index. TODO: explain this method.
	 * 
	 * @param pageNumber
	 *            the page number
	 * @return the page end index.
	 */
	protected int getPageEndIndex(final int pageNumber) {
		return getPaging().getPageEndIndex(pageNumber);
	}

	/**
	 * Get the page start index.
	 * 
	 * @param pageNumber
	 *            the page number
	 * @return the page start index.
	 */
	protected int getPageStartIndex(final int pageNumber) {
		return getPaging().getPageStartIndex(pageNumber);
	}

	private int getCurrentPageNumber() {
		return getPageNumber(trackedIndex);
	}

	private int getMaxLength() {
		int result = 0;

		for (int i = 0; i < itemVector.size(); i++) {
			String item = (String) itemVector.elementAt(i);

			if (item.length() > result) {
				result = item.length();
			}
		}

		return result;
	}

	private int getMaxStartPos() {
		Rectangle rect = (Rectangle) getSize().clone();
		int width = rect.getWidth() - 2;
		int result = getMaxLength() - width;
		result = (result < 0) ? 0 : result;

		return result;
	}

	/*
	 * private int getMaximumStartIndex() { return (_items.size() <
	 * getVisibleSize()) ? 0 : (_items.size() - getVisibleSize()); }
	 */
	private int getPageNumber(final int index) {
		return getPaging().getPageNumber(index);
	}

	private int getPageSize() {
		return getPaging().getPageSize();
	}

	private Paging getPaging() {
		return new Paging(getVisibleSize(), getItemsCount());
	}

	private boolean isVisible(final int index) {
		if (itemVector.size() == 0) {
			return false;
		}
		boolean result1 = index >= startIndex;
		boolean result2 = index < startIndex + getVisibleSize();

		return result1 && result2;
	}

	private int getVisibleSize() {
		return getSize().getHeight() - 2;
	}

	private int getLineWidth() {
		return getWidth() - 2;
	}

	private void callItem(final int index) {
		ItemEvent event =
				new ItemEvent(this, index, itemVector.elementAt(index),
						ItemEvent.CALLED);
		listenerManager.handleEvent(event);
	}

	private boolean decrementPage() {
		int nextPos = 0;

		if (getCurrentPageNumber() > 0) {
			nextPos =
					getPaging().getIndexByPageOffset(
							getCurrentPageNumber() - 1, getCurrentPageOffset());
		} else {
			nextPos = 0;
		}

		return findNextSelectableItem(nextPos, -1, false, 0);
	}

	private boolean decrementStartPos() {
		if (startPos > 0) {
			startPos--;
			return true;
		}

		return false;
	}

	private boolean decrementTrack() {
		boolean found = false;

		if (trackedIndex > 0) {
			found = findNextSelectableItem(trackedIndex - 1, -1, true, -1);
		}

		return found;
	}

	private void dispatchEvent(final int index, final boolean value) {
		int aType = value ? ItemEvent.SELECTED : ItemEvent.DESELECTED;
		ItemEvent event =
				new ItemEvent(this, index, itemVector.elementAt(index), aType);
		listenerManager.handleEvent(event);
	}

	private void drawFirstRowSelected() {
		if (hasFocus()) {
			Toolkit.drawRectangle(getAbsoluteX() + 1, getAbsoluteY() + 1,
					getSize().getWidth() - 2, 1, getSelectedColors());
		}
	}

	private void drawItems() {
		Rectangle rect = (Rectangle) getSize().clone();
		rect.setLocation(getAbsoluteX(), getAbsoluteY());

		for (int i = 0; i < getVisibleSize(); i++) {
			int index = startIndex + i;

			if (index < itemVector.size()) {
				drawItem(index, rect);
			} else {
				eraseItem(rect, i);
			}
		}

		if (itemVector.size() == 0) {
			drawFirstRowSelected();
		}
	}

	private void eraseItem(final Rectangle aRect, final int aIndex) {
		Toolkit.drawRectangle(new Rectangle(aRect.getX() + 1, aRect.getY()
				+ aIndex + 1, aRect.getWidth() - 2, 1), getColors());
	}

	private void drawRectangle() {
		Rectangle rect = (Rectangle) getSize().clone();
		rect.setWidth(rect.getWidth() - 2);
		rect.setHeight(rect.getHeight() - 2);
		rect.setLocation(getAbsoluteX() + 1, getAbsoluteY() + 1);
		Toolkit.drawRectangle(rect, getColors());
	}

	private void drawTitle() {
		if (title != null) {
			int mLength = Math.min(getSize().getWidth() - 2, title.length());
			int mX = getAbsoluteX() + ((getSize().getWidth() - mLength) / 2);
			Toolkit.printString(title, mX, getAbsoluteY(), mLength, 1,
					getTitleColors());
		}
	}

	/**
	 * Find the next selectable item.
	 * 
	 * @param aPos
	 *            the startposition from which to search.
	 * @param searchDirection
	 *            the search direction.
	 * @param onlySearchDirection
	 *            Whether to look "backwards" relative to search direction.
	 * @param stepping
	 *            don't know: TODO: document!
	 * @return true if found
	 */
	private boolean findNextSelectableItem(final int aPos,
			final int searchDirection, final boolean onlySearchDirection,
			final int stepping) {

		int pos = aPos;

		if (getItemsCount() == 0) {
			return false;
		}

		// we use a single virtual page if not displayed
		int page = 0;
		int start = 0;
		int end = itemVector.size();

		if (isVisible()) {
			page = getPageNumber(pos);
			start = getPageStartIndex(page);
			end = getPageEndIndex(page);
		}

		boolean found = false;

		if (isSelectable(pos)) {
			found = true;
		} else {
			int searchPos = pos;

			while ((searchPos <= end) && (searchPos >= start) && (!found)) {
				searchPos += searchDirection;
				found = isSelectable(searchPos);
			}

			if (!found && !onlySearchDirection) {
				searchPos = pos;

				while ((searchPos <= end) && (searchPos >= start) && (!found)) {
					searchPos -= searchDirection;
					found = isSelectable(searchPos);
				}
			}

			pos = searchPos;
		}

		// TODO: document why a function returning boolean does this?
		// Maybe to make an invisible item visible?
		if (found) {
			if (stepping == 0) {
				startIndex = start;
			} else if (stepping == -1) {
				if (!isVisible(pos)) {
					startIndex = pos;
				}
			} else {
				if (!isVisible(pos)) {
					startIndex = Math.max(0, pos - getVisibleSize() + 1);
				}
			}

			trackedIndex = pos;
		}

		return found;
	}

	private boolean incrementPage() {
		int nextPos = 0;

		if (getCurrentPageNumber() < (getPageSize() - 1)) {
			nextPos =
					getPaging().getIndexByPageOffset(
							getCurrentPageNumber() + 1, getCurrentPageOffset());
		} else {
			nextPos = getItemsCount() - 1;
		}

		// System.err.println("Next Page ["+nextPos+"]");

		return findNextSelectableItem(nextPos, 1, false, 0);
	}

	private boolean incrementStartPos() {
		if (startPos < getMaxStartPos()) {
			startPos++;
			return true;
		}

		return false;
	}

	private boolean incrementTrack() {
		boolean found = false;

		if (trackedIndex < (getItemsCount() - 1)) {
			found = findNextSelectableItem(trackedIndex + 1, 1, true, 1);
		}

		return found;
	}

	private void drawItem(final int index, final Rectangle rect) {
		// do not draw if not visible
		if (index < startIndex || index > startIndex + getVisibleSize()) {
			return;
		}

		int x = rect.getX() + 1;
		int y = (rect.getY() + 1 + index) - startIndex;
		int width = rect.getWidth() - 2;
		boolean toSelect = (index == trackedIndex && hasFocus());
		if (!toSelect) {
			toSelect = isSelected(index);
		}

		CharColor colors = toSelect ? getSelectedColors() : getColors();

		String item =
				getItemRepresentation((String) itemVector.elementAt(index));

		if (item.length() < (startPos + 1)) {
			item = "";
		} else {
			if (startPos != 0) {
				item = item.substring(startPos, item.length());
			}
		}

		if ((item.length() < width) && (toSelect)) {
			StringBuffer itemBuffer = new StringBuffer();
			itemBuffer.append(item);

			for (int i = 0; i < (width - item.length()); i++) {
				itemBuffer.append(' ');
			}

			item = itemBuffer.toString();
		}

		Toolkit.printString(item, x, y, width, 1, colors);
		if (item.length() < width) {
			Toolkit.drawRectangle(x + item.length(), y, width - item.length(),
					1, colors);
		}
	}

	private void redraw(final boolean flag, final int aTrackedIndex,
			final int backupTrackedIndex) {
		if (flag) {
			// System.err.println("Redraw ["+trackedIndex+" / "+
			// backupTrackedIndex+"]");
			redrawItem(aTrackedIndex, getRectangle());
			redrawItem(backupTrackedIndex, getRectangle());
		} else {
			// System.err.println("Repaint");
			// paint();
			drawItems();
		}
	}

	private void redrawItem(final int index) {
		redrawItem(index, getRectangle());
	}

	private void redrawItem(final int index, final Rectangle rect) {
		drawItem(index, rect);
		// int x = rect.getX() + 1;
		// int y = ( rect.getY() + 1 + index ) - _startIndex;
		// int width = rect.getWidth() - 2;
		// Rectangle itemRect = new Rectangle(x, y, width, 1);
		// printItem(index, itemRect);
		// boolean toSelect = ( ( ( index == _trackedIndex ) && hasFocus() ) ||
		// ( isSelected(index) ) );
		// CharColor colors = toSelect ? getSelectedColors() : getColors();
		// Toolkit.changeColors(itemRect, colors);
	}

	private void redrawItemBySelecting(final int index) {
		if (!((index == trackedIndex) && hasFocus())) {
			redrawItem(index, getRectangle());
		}
	}

	private void redrawSelectedItems() {
		for (int i = 0; i < getVisibleSize(); i++) {
			int index = startIndex + i;

			if (index < itemVector.size()) {
				boolean toSelect =
						((index == trackedIndex) || (isSelected(index)));

				if (toSelect) {
					redrawItem(index, getRectangle());
				}
			}
		}
	}

	private void refresh() {
		scrollbars.refresh();
		drawRectangle();
		drawItems();
	}

	private void reset() {
		startIndex = 0;
		trackedIndex = 0;
		startPos = 0;
	}

	private void select(final int index, final boolean value) {
		if (!(isSelected(index) == value)) {
			int selected = getSelectedIndex();
			selectedVector.set(index, new Boolean(value));

			if ((!multiple) && value) {
				if (selected != -1) {
					deselect(selected);
				}
			}
		}
	}
}