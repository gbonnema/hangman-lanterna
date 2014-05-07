/**
 * jcurses.widgets.
 */
package jcurses.widgets;

import jcurses.system.CharColor;
import jcurses.system.Toolkit;
import jcurses.util.Rectangle;

/**
 * This class implements a panel container for widgets. A panel is a rectangle
 * with a specified color to place on it other widgets. Each window has a panel
 * called root panel that serves as root for the widget hierarchy.
 */
public class Panel extends WidgetContainer {

	/**
	 * 
	 */
	public static final int ALIGNMENT_TOP = 0;
	/**
	 * 
	 */
	public static final int ALIGNMENT_BOTTOM = 1;
	/**
	 * 
	 */
	public static final int ALIGNMENT_LEFT = 0;
	/**
	 * 
	 */
	public static final int ALIGNMENT_RIGHT = 1;
	/**
	 * 
	 */
	public static final int ALIGNMENT_CENTER = 2;
	
	private Rectangle prefSize = null;

	/**
	 * Constructor.
	 */
	public Panel() {
		this(-1, -1);
	}

	/**
	 * The constructor
	 * 
	 * @param width
	 *            preferred width, if -1 is passed , no width is preferred, so
	 *            the width of the panel depends only on the layout manager of
	 *            the parent container or on the window width, if this is a root
	 *            panel.
	 * @param height
	 *            preferred height, if -1 is passed , no height is preferred, so
	 *            the width of the panel depends only on the layout manager of
	 *            the parent container or on the window width, if this is a root
	 *            panel.
	 */
	public Panel(final int width, final int height) {
		prefSize = new Rectangle(width, height);
	}
	/**
	 * @return a rectangle with the preferred size.
	 */
	protected Rectangle getPreferredSize() {
		return prefSize;
	}
	/**
	 * 
	 */
	protected void paintSelf() {
		Toolkit.drawRectangle(getRectangle(), getColors());
	}

	/**
	 * Returns panel colors.
	 * 
	 * @return panel colors
	 * @deprecated Use getColors()
	 * 
	 */
	public CharColor getPanelColors() {
		return getColors();
	}

	/**
	 * Sets panel colors.
	 * 
	 * @param aColors
	 *            new panel colors
	 * @deprecated Use setColors()
	 */
	public void setPanelColors(final CharColor aColors) {
		setColors(aColors);
	}

	/**
	 * @return the default colors
	 * @deprecated Use getDefaultColors()
	 */
	protected CharColor getDefaultPanelColors() {
		return getDefaultColors();
	}

	/**
	 * @return a rectangle with the client area
	 * @deprecated Use getClientArea() instead
	 */
	protected Rectangle getPaintingRectangle() {
		return getClientArea();
	}
}