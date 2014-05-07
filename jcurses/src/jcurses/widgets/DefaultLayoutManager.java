package jcurses.widgets;

import jcurses.util.Rectangle;

/**
 * This is a default layout manager. The constraints state for each widget to
 * layout a coordinates of the rectangle, within that the widget is placed and
 * the alignment of the widget, if its preferred size is smaller as the
 * rectangle's size-
 */
public class DefaultLayoutManager implements LayoutManager, WidgetsConstants {
	/**
	 * 
	 */
	private WidgetContainer parent = null;

	/**
	 * Bind to a parent widget container. Throws a runtime exception if already
	 * bound.
	 * 
	 * @param container
	 *            the widget container.
	 * 
	 */
	public final void bindToContainer(final WidgetContainer container) {
		if (parent != null) {
			throw new RuntimeException("Already bound!!!");
		}
		parent = container;
	}

	/**
	 * Unbind from the current parent container. No problem if there is none.
	 */
	public final void unbindFromContainer() {
		parent = null;
	}

	/**
	 * Method description.
	 * 
	 * @param widget
	 *            .
	 * @param constraint
	 *            .
	 */
	public final void layout(final Widget widget, final Object constraint) {
		if (!(constraint instanceof DefaultLayoutConstraint)) {
			throw new RuntimeException("unknown constraint: "
					+ constraint.getClass().getName());
		}

		DefaultLayoutConstraint cstr = (DefaultLayoutConstraint) constraint;

		Rectangle prefSize = widget.getPreferredSize();

		int prefWidth = prefSize.getWidth();
		int prefHeight = prefSize.getHeight();
		/*
		 * Negative or 0 means that no preferred size was indicated.
		 * Negativ oder 0 bedeutet, da? keine bevorzugte Gr?sse angegeben wurde
		 */
		if (prefWidth <= 0) {
			prefWidth = cstr.width;
		}

		if (prefHeight <= 0) {
			prefHeight = cstr.height;
		}

		int width = 0;
		int height = 0;

		if (prefWidth < cstr.width) {
			widget.setX(getAlignedCoordinate(prefWidth, cstr.width, cstr.x,
					cstr.horizontalConstraint));
			width = prefWidth;
		} else {
			widget.setX(cstr.x);
			width = cstr.width;
		}

		if (prefHeight < cstr.height) {
			widget.setY(getAlignedCoordinate(prefHeight, cstr.height, cstr.y,
					cstr.verticalConstraint));
			height = prefHeight;
		} else {
			widget.setY(cstr.y);
			height = cstr.height;
		}

		widget.setSize(new Rectangle(width, height));
	}

	/**
	 * 
	 * @param prefG
	 *            .
	 * @param contG
	 *            .
	 * @param contC
	 *            .
	 * @param someAlignment
	 *            .
	 * @return aligned coordinate
	 */
	private int getAlignedCoordinate(final int prefG, final int contG,
			final int contC, final int someAlignment) {
		int alignment = someAlignment;

		if (alignment == ALIGNMENT_CENTER) {
			alignment = 0;
		} else if ((alignment == ALIGNMENT_BOTTOM)
				|| (alignment == ALIGNMENT_RIGHT)) {
			alignment = 1;
		} else {
			alignment = 2;
		}

		int result = 0;
		if (alignment == 2) {
			result = contC;
		} else if (alignment == 1) {
			result = contC + contG - prefG;
		} else {
			result = contC + (contG - prefG) / 2;
		}
		return result;
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
	 * @param width
	 *            the width of the rectangle, within that the widget is placed
	 * @param height
	 *            the hight of the rectangle, within that the widget is placed
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
			final int width, final int height, final int verticalConstraint,
			final int horizontalConstraint) {
		parent.addWidget(widget, new DefaultLayoutConstraint(x, y, width,
				height, horizontalConstraint, verticalConstraint));

	}

	/**
	 * Removes a widget from the container.
	 * 
	 * @param widget
	 *            widget to be removed
	 */

	public final void removeWidget(final Widget widget) {
		parent.removeWidget(widget);

	}
}

/**
 * The default Layout constraint where all attributes have an initial value.
 * 
 */
class DefaultLayoutConstraint {

	/**
	 * 
	 */
	protected int x = 0;
	/**
	 * 
	 */
	protected int y = 0;
	/**
	 * 
	 */
	protected int width = 0;
	/**
	 * 
	 */
	protected int height = 0;
	/**
	 * 
	 */
	protected int horizontalConstraint = 0;
	/**
	 * 
	 */
	protected int verticalConstraint = 0;

	/**
	 * 
	 * @param aX .
	 * @param aY .
	 * @param aWidth .
	 * @param aHeight .
	 * @param aHorizontalConstraint .
	 * @param aVerticalConstraint .
	 */
	protected DefaultLayoutConstraint(final int aX, final int aY,
			final int aWidth, final int aHeight,
			final int aHorizontalConstraint, final int aVerticalConstraint) {
		this.x = aX;
		this.y = aY;
		this.width = aWidth;
		this.height = aHeight;
		this.horizontalConstraint = aHorizontalConstraint;
		this.verticalConstraint = aVerticalConstraint;
	}

}