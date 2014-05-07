package jcurses.widgets;

import jcurses.event.ItemEvent;
import jcurses.event.ItemListener;

/**
 * This class implements a pop up menu window. Such windows can be used for
 * example to implement menu bars ( currently not contained in the library ). A
 * pop up menu window gives a user the possibility to select and invoke an item
 * from a list and is than closed. Separator items can be used as by
 * <code>MenuList</code> described.
 */
public class PopUpMenu implements WidgetsConstants, ItemListener {

	private ModalDialog peer = null;
	private MenuList menuList = new MenuList();
	private int x = 0;
	private int y = 0;

	/**
	 * The constructor
	 * 
	 * @param aX
	 *            the x coordinate of the dialog window's top left corner
	 * @param aY
	 *            the y coordinate of the dialog window's top left corner
	 * @param aTitle
	 *            window's title
	 */
	public PopUpMenu(final int aX, final int aY, final String aTitle) {
		x = aX;
		y = aY;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param index
	 *            DOCUMENT ME!
	 * 
	 * @return the item at the specified position
	 */
	public String getItem(final int index) {
		return menuList.getItem(index);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return the number of items
	 */
	public int getItemsCount() {
		return menuList.getItemsCount();
	}

	/**
	 * Returns the last selected index. Should be invoked after the return of
	 * the <code>show</code> to get the result
	 * 
	 * @return last selected index
	 */
	public int getSelectedIndex() {
		return menuList.getTrackedIndex();
	}

	/**
	 * Returns the last selected item. Should be invoked after the return of the
	 * <code>show</code> to get the result
	 * 
	 * @return last selected index
	 */
	public String getSelectedItem() {
		return menuList.getTrackedItemStr();
	}

	/**
	 * Adds an item at the specified position
	 * 
	 * @param pos
	 *            position
	 * @param item
	 *            item to add
	 */
	public void add(final int pos, final String item) {
		menuList.add(pos, item);
	}

	/**
	 * Adds an item at the end of the list.
	 * 
	 * @param item
	 *            DOCUMENT ME!
	 */
	public void add(final String item) {
		menuList.add(item);
	}

	/**
	 * Adds a separator item at the specified position
	 * 
	 * @param index
	 *            position
	 */
	public void addSeparator(final int index) {
		menuList.addSeparator(index);
	}

	/**
	 * Adds a separator at the end of the list.
	 */
	public void addSeparator() {
		menuList.addSeparator();
	}

	/**
	 * Removes the item at the specified position
	 * 
	 * @param pos
	 *            position
	 */
	public void remove(final int pos) {
		menuList.remove(pos);
	}

	/**
	 * Removes the first ocuurence of the specified item
	 * 
	 * @param item
	 *            item to be removed
	 */
	public void remove(final String item) {
		menuList.remove(item);
	}

	/**
	 * Makes the window visible. Blocks, until the window is closed.
	 */
	public void show() {
		int width = menuList.getPreferredSize().getWidth();
		int height = menuList.getPreferredSize().getHeight();

		peer = new ModalDialog(x, y, width, height, false, null);
		GridLayoutManager manager1 = new GridLayoutManager(1, 1);
		peer.getRootPanel().setLayoutManager(manager1);
		manager1.addWidget(menuList, 0, 0, 1, 1, ALIGNMENT_CENTER,
				ALIGNMENT_CENTER);
		menuList.addListener(this);
		peer.show();
	}
	/**
	 * Once the state changes, the object removes
	 * itself as listener to events
	 * closes the peer. TODO: what is the peer?
	 * @param e event triggering this method.
	 */
	public void stateChanged(final ItemEvent e) {
		menuList.removeListener(this);
		peer.close();
	}
	/**
	 * select one of the items specified by aIndex.
	 * @param aIndex the index pointing to an item.
	 */
	public void select(final int aIndex) {
		menuList.setTrackedItem(aIndex);
	}
}