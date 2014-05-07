package jcurses.widgets;

import jcurses.util.Rectangle;

/******************************************************************************
 * This class is a layout manager that works like the
 * <code>DefaultLayoutManager</code> with a difference: the painting rectangle
 * is shared in many grid cells and the constraints are stated not in real
 * coordinates on the painting rectangle, but in 'grid-coordinates'.
 */

public class GridLayoutManager implements LayoutManager, WidgetsConstants {

	/**
	 * the default layout manager.
	 */
	private DefaultLayoutManager defLayout = new DefaultLayoutManager();
	/**
	 * Parent widget container.
	 */
	private WidgetContainer container = null;
	/**
	 *
	 */
	private int width = 0;
	/**
	 *
	 */
	private int height = 0;
	/**
	 * 
	 */
	private Grid grid = null;

	/**
	 * Bind the grid layout manager to a widget container. Throws a runtime
	 * exception if it is already bound.
	 * 
	 * @param aContainer
	 *            the widget container
	 */
	public final void bindToContainer(final WidgetContainer aContainer) {
		if (container != null) {
			throw new RuntimeException("Already bound!!!");
		}
		container = aContainer;
	}

	/**
	 * Unbind from any widget container, if present.
	 */
	public final void unbindFromContainer() {
		container = null;
	}

	/**
	 * The constructor.
	 * 
	 * @param aWidth
	 *            the width of the grid ( in cells )
	 * @param aHeight
	 *            the height of the grid ( in cells )
	 * 
	 */
	public GridLayoutManager(final int aWidth, final int aHeight) {
		width = aWidth;
		height = aHeight;
	}

	/**
	 * Layout the specified widget with specified constraint. Throws a runtime
	 * exception if the constraint is not a GridLayoutConstraint.
	 * 
	 * @param widget
	 *            the widget.
	 * @param constraint
	 *            the constraint.
	 */
	public final void layout(final Widget widget, final Object constraint) {
		if (!(constraint instanceof GridLayoutConstraint)) {
			throw new RuntimeException("unknown constraint: "
					+ constraint.getClass().getName());
		}
		Rectangle rect = container.getClientArea();
		if (rect == null) {
			rect = container.getSize();
		}

		grid = new Grid(rect, width, height);
		defLayout.layout(widget, ((GridLayoutConstraint) constraint)
				.getDefaultLayoutConstraint(grid));

	}

	/**
	 * Adds a widget to the bounded container.
	 * 
	 * @param widget
	 *            widget to be added
	 * @param x
	 *            the x coordinate of the top left corner of the rectangle,
	 *            within that the widget is placed
	 * @param y
	 *            the y coordinate of the top left corner of the rectangle,
	 *            within that the widget is placed
	 * @param rectWidth
	 *            the width of the rectangle, within that the widget is placed
	 * @param rectHeight
	 *            the height of the rectangle, within that the widget is placed
	 * @param verticalConstraint
	 *            vertical alignment constraint. Following values a possible:
	 *            <code>WidgetConstraints.ALIGNMENT_CENTER</code>,
	 *            <code>WidgetConstraints.ALIGNMENT_TOP</code>,
	 *            <code>WidgetConstraints.ALIGNMENT_BOTTOM</code>
	 * @param horizontalConstraint
	 *            vertical alignment constraint, Following values are possible:
	 *            *<code>WidgetConstraints.ALIGNMENT_CENTER</code>,
	 *            <code>WidgetConstraints.ALIGNMENT_LEFT</code>,
	 *            <code>WidgetConstraints.ALIGNMENT_RIGHT</code>
	 */
	public final void addWidget(final Widget widget, final int x, final int y,
			final int rectWidth, final int rectHeight,
			final int verticalConstraint, final int horizontalConstraint) {
		container.addWidget(widget, new GridLayoutConstraint(x, y, rectWidth,
				rectHeight, horizontalConstraint, verticalConstraint));
	}

	/**
	 * Removes a widget.
	 * 
	 * @param widget
	 *            widget to remove
	 */
	public final void removeWidget(final Widget widget) {
		container.removeWidget(widget);

	}
}

/**
 * 
 * This class describes a GridLayout constraint.
 * 
 */
class GridLayoutConstraint {

	/**
	 * 
	 */
	private int x = 0;
	/**
	 * 
	 */
	private int y = 0;
	/**
	 * 
	 */
	private int width = 0;
	/**
	 * 
	 */
	private int height = 0;
	/**
	 * 
	 */
	private int horizontalConstraint = 0;
	/**
	 * 
	 */
	private int verticalConstraint = 0;

	/**
	 * The constructor for the GridLayoutConstraint.
	 * 
	 * @param aX
	 *            The x coordinate.
	 * @param aY
	 *            The y coordinate.
	 * @param aWidth
	 *            the width.
	 * @param aHeight
	 *            the height.
	 * @param aHorizontalConstraint
	 *            the horizontal constraint.
	 * @param aVerticalConstraint
	 *            the vertical constraint.
	 */
	public GridLayoutConstraint(final int aX, final int aY, final int aWidth,
			final int aHeight, final int aHorizontalConstraint,
			final int aVerticalConstraint) {
		this.x = aX;
		this.y = aY;
		this.width = aWidth;
		this.height = aHeight;
		this.horizontalConstraint = aHorizontalConstraint;
		this.verticalConstraint = aVerticalConstraint;
	}

	/**
	 * Retrieve the default layout constraint.
	 * 
	 * @param grid
	 *            the grid.
	 * @return the default layout constraint.
	 */
	public DefaultLayoutConstraint getDefaultLayoutConstraint(final Grid grid) {

		Rectangle rect = grid.getRectangle(x, y, width, height);
		return new DefaultLayoutConstraint(rect.getX(), rect.getY(),
				rect.getWidth(), rect.getHeight(), horizontalConstraint,
				verticalConstraint);

	}

}
