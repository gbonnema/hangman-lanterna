package jcurses.widgets;

import jcurses.system.CharColor;
import jcurses.system.Toolkit;
import jcurses.themes.Theme;
import jcurses.util.Rectangle;

/**
 * This class implements a text area to edit a text with many lines
 */
public class TextArea extends TextComponent implements IScrollable {

	private ScrollbarPainter scrollbars = null;

	/**
	 * The constructor
	 * 
	 * @param width
	 *            the preferred width of the component. If -1 is stated, there
	 *            is no preferred width and the component is layouted dependend
	 *            on the container and the text
	 * @param height
	 *            the preferred height of the component. If -1 is stated, there
	 *            is no preferred width and the component is layouted dependend
	 *            on the container.
	 * @param text
	 *            the initial text, if <code>null</code> the component is empty
	 */
	public TextArea(final int width, final int height, final String text) {
		super(width, height, text);
		scrollbars = new ScrollbarPainter(this);
	}

	/**
	 * The constructor
	 * 
	 * @param width
	 *            the preferred width of the component. If -1 is stated, there
	 *            is no preferred width and the component is layouted dependend
	 *            on the container and the text
	 * @param height
	 *            the preferred height of the component. If -1 is stated, there
	 *            is no preferred width and the component is layouted dependend
	 *            on the container.
	 */
	public TextArea(final int width, final int height) {
		this(width, height, null);
	}

	/**
	 * Constructor without arguments
	 */
	public TextArea() {
		this(-1, -1);
	}

	/**
	 * @return rectangle containing the border coordinates.
	 */
	public Rectangle getBorderRectangle() {
		Rectangle rect = (Rectangle) getSize().clone();
		rect.setLocation(getAbsoluteX(), getAbsoluteY());
		return rect;
	}

	/**
	 * @return the horizontal scrollbar length.
	 */
	public float getHorizontalScrollbarLength() {
		/* @formatter:off */
		if (!((getTextWidth() > 0) 
				&& (getTextWidth() > getVisibleTextWidth()))) {
			return 0;
		}
		/* @formatter:on */

		return ((float) getVisibleTextWidth()) / ((float) getTextWidth());
	}

	/**
	 * @return horizontal scrollbar offset.
	 */
	public float getHorizontalScrollbarOffset() {
		/* @formatter:off */
		if (!((getTextWidth() > 0) 
				&& (getTextWidth() > getVisibleTextWidth()))) {
			return 0;
		}
		/* @formatter:on */

		return ((float) getTextX()) / ((float) getTextWidth());
	}

	/**
	 * 
	 * @param aColors
	 *            the colors for the scrollbar
	 */
	public void setScrollbarColors(final CharColor aColors) {
		setColors(Theme.COLOR_WIDGET_SCROLLBAR, aColors);
	}

	/**
	 * @return the colors of the scrollbar
	 */
	public CharColor getScrollbarColors() {
		return getColors(Theme.COLOR_WIDGET_SCROLLBAR);
	}

	/**
	 * 
	 * @return the default colors for the scrollbar.
	 */
	public CharColor getScrollbarDefaultColors() {
		return getDefaultColors(Theme.COLOR_WIDGET_SCROLLBAR);
	}

	/**
	 * @return the vertical scrollbar length.
	 */
	public float getVerticalScrollbarLength() {
		/* @formatter:off */
		if (!((getTextHeight() > 0) 
				&& (getTextHeight() > getVisibleTextHeight()))) {
			return 0;
		}
		/* @formatter:on */

		return ((float) getVisibleTextHeight()) / ((float) getTextHeight());
	}
	/**
	 * @return the vertical scrollbar offset.
	 */
	public float getVerticalScrollbarOffset() {
		/* @formatter:off */
		if (!((getTextHeight() > 0) 
				&& (getTextHeight() > getVisibleTextHeight()))) {
			return 0;
		}
		/* @formatter:on */

		return ((float) getTextY()) / ((float) getTextHeight());
	}
	/**
	 * @return true alsways.
	 */
	public boolean hasHorizontalScrollbar() {
		return true;
	}
	/**
	 * @return true always.
	 */
	public boolean hasVerticalScrollbar() {
		return true;
	}
	/**
	 * @return rectangle containing coordinates for text.
	 */
	protected Rectangle getTextRectangle() {
		Rectangle result = (Rectangle) getSize().clone();
		result.setLocation(getAbsoluteX() + 1, getAbsoluteY() + 1);
		result.setWidth(result.getWidth() - 2);
		result.setHeight(result.getHeight() - 2);

		return result;
	}
	/**
	 * 
	 */
	protected void doPaint() {
		super.doPaint();
		Toolkit.drawBorder(getBorderRectangle(), getBorderColors());
		drawAdditionalThings();
	}

	/**
	 * 
	 */
	protected void drawAdditionalThings() {
		scrollbars.paint();
	}

	/**
	 * 
	 */
	protected void refreshAdditionalThings() {
		scrollbars.refresh();
	}

	private int getVisibleTextHeight() {
		return getSize().getHeight() - 2;
	}

	
	private int getVisibleTextWidth() {
		return getSize().getWidth() - 2;
	}
}