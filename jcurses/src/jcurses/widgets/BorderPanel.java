package jcurses.widgets;

import jcurses.system.CharColor;
import jcurses.system.Toolkit;
import jcurses.util.Rectangle;

/**
 * This class implements a panel with a border.
 * 
 */

public class BorderPanel extends Panel {

	/**
	 * the border colors.
	 */
	private CharColor colors = getDefaultBorderColors();
	/**
	 * the default border colors.
	 */
	private static CharColor defaultBorderColors = new CharColor(
			CharColor.WHITE, CharColor.BLACK);

	/**
	 * Constructor for the BorderPanel with default w / h.
	 */
	public BorderPanel() {
		super();
	}

	/**
	 * Constructor for the BorderPanel with width and height provided.
	 * 
	 * @param width
	 *            width
	 * @param height
	 *            height
	 */
	public BorderPanel(final int width, final int height) {
		super(width, height);
	}

	/**
	 * Paints self.
	 * 
	 * TODO: Is this method meant for override?
	 */
	@Override
	protected void paintSelf() {
		Toolkit.drawBorder(getRectangle(), getBorderColors());
		super.paintSelf();
	}

	/**
	 * Repaints self.
	 * 
	 * TODO: Is this method meant for override?
	 */
	@Override
	protected void repaintSelf() {
		Toolkit.drawBorder(getRectangle(), getBorderColors());
		super.repaintSelf();
	}

	/**
	 * Gets the borderColors attribute of the BorderPanel object.
	 * 
	 * @return The borderColors value
	 */
	public final CharColor getBorderColors() {
		return colors;
	}

	/**
	 * Sets the borderColors attribute of the BorderPanel object.
	 * 
	 * @param someColors
	 *            The new borderColors value
	 */
	public final void setBorderColors(final CharColor someColors) {
		colors = someColors;
	}

	/**
	 * Gets the defaultBorderColors attribute of the BorderPanel object.
	 * 
	 * @return The defaultBorderColors value
	 */
	protected final CharColor getDefaultBorderColors() {
		return defaultBorderColors;
	}

	/**
	 * Gets the clientArea attribute of the BorderPanel object.
	 * 
	 * @return The clientArea value
	 */
	protected final Rectangle getClientArea() {
		Rectangle rect = (Rectangle) getSize().clone();
		rect.setLocation(1, 1);
		rect.setWidth(rect.getWidth() - 2);
		rect.setHeight(rect.getHeight() - 2);

		return rect;
	}

}
