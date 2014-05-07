package jcurses.widgets;

import jcurses.util.Rectangle;

/**
 * This class is a layout manager that works like Swing's BorderLayout. Up to 5
 * widgets can be added to this layout at once, in the following positions:
 * NORTH, SOUTH, WEST, EAST, and CENTER. Components in the outer positions are
 * set to their preferred size, and the component in the CENTER is allocated the
 * remaining screen area.
 * 
 * @author <a href="mailto:lenbok@myrealbox.com">Len Trigg</a>
 */
public class BorderLayoutManager implements LayoutManager {
	/* @formatter:off */
	/* The formatter keeps making the comment larger than 80 chars */
	/**
	 * Constant used to specify widget placement in the center of the 
	 * container.
	 */
	public static final int CENTER = 0;
	/* @formatter:on */

	/**
	 * Constant used to specify widget placement in the north of the container.
	 */
	public static final int NORTH = 1;

	/**
	 * Constant used to specify widget placement in the south of the container.
	 */
	public static final int SOUTH = 2;

	/**
	 * Constant used to specify widget placement in the west of the container.
	 */
	public static final int WEST = 3;

	/**
	 * Constant used to specify widget placement in the east of the container.
	 */
	public static final int EAST = 4;

	/**
	 * The widget we are providing layout service for.
	 */
	private WidgetContainer mFather = null;

	/**
	 * The 5 areas of the border layout.
	 */
	private final int nrBorderAreas = 5;

	/**
	 * Stores constraints for each of the 5 possible positions. Any entry may be
	 * null.
	 */
	private BorderLayoutConstraint[] mSlots =
			new BorderLayoutConstraint[nrBorderAreas];

	/**
	 * Stores separator X positions for each dimension.
	 */
	private int[] mXSep = new int[2];
	/**
	 * Stores separator Y positions for each dimension.
	 */
	private int[] mYSep = new int[2];

	/**
	 * Adds a widget to the bounded container.
	 * 
	 * @param widget
	 *            widget to be added
	 * @param position
	 *            the position in the border layout. The following values are
	 *            possible: <code>NORTH</code>, <code>SOUTH</code>,
	 *            <code>WEST</code>, <code>EAST</code>, <code>CENTER</code>,
	 * @param verticalConstraint
	 *            vertical alignment constraint. The following values are
	 *            possible: <code>WidgetConstraints.ALIGNMENT_CENTER</code>,
	 *            <code>WidgetConstraints.ALIGNMENT_TOP</code>,
	 *            <code>WidgetConstraints.ALIGNMENT_BOTTOM</code>
	 * @param horizontalConstraint
	 *            vertical alignment constraint, The following values are
	 *            possible: <code>WidgetConstraints.ALIGNMENT_CENTER</code>,
	 *            <code>WidgetConstraints.ALIGNMENT_LEFT</code>,
	 *            <code>WidgetConstraints.ALIGNMENT_RIGHT</code>
	 */
	public final void addWidget(final Widget widget, final int position,
			final int verticalConstraint, final int horizontalConstraint) {
		if ((position != NORTH) && (position != SOUTH) && (position != WEST)
				&& (position != EAST) && (position != CENTER)) {
			throw new IllegalArgumentException(
					"Must specify position of NORTH, "
							+ "SOUTH, WEST, EAST, or CENTER.");
		}

		mSlots[position] =
				new BorderLayoutConstraint(widget, position,
						horizontalConstraint, verticalConstraint);
		mFather.addWidget(widget, mSlots[position]);
	}

	/**
	 * Adds a widget to the bounded container using the interface arguments.
	 * 
	 * @param widget
	 *            widget to be added
	 * @param position
	 *            the position in the border layout. The following values are
	 *            possible: <code>NORTH</code>, <code>SOUTH</code>,
	 *            <code>WEST</code>, <code>EAST</code>, <code>CENTER</code>.
	 * @param notUsed
	 *            ignored, it's here for the LayoutManager interface as greatest
	 *            common factoring of the operation for all layouts.
	 * @param width
	 *            ignored, it's here for the LayoutManager interface as greatest
	 *            common factoring of the operation for all layouts.
	 * @param height
	 *            ignored, it's here for the LayoutManager interface as greatest
	 *            common factoring of the operation for all layouts.
	 * @param verticalConstraint
	 *            vertical alignment constraint. The following values are
	 *            possible: <code>WidgetConstraints.ALIGNMENT_CENTER</code>,
	 *            <code>WidgetConstraints.ALIGNMENT_TOP</code>,
	 *            <code>WidgetConstraints.ALIGNMENT_BOTTOM</code>
	 * @param horizontalConstraint
	 *            vertical alignment constraint, The following values are
	 *            possible: <code>WidgetConstraints.ALIGNMENT_CENTER</code>,
	 *            <code>WidgetConstraints.ALIGNMENT_LEFT</code>,
	 *            <code>WidgetConstraints.ALIGNMENT_RIGHT</code>
	 */
	public final void addWidget(final Widget widget, final int position,
			final int notUsed, final int width, final int height,
			final int verticalConstraint, final int horizontalConstraint) {
		addWidget(widget, position, verticalConstraint, horizontalConstraint);
	}

	/**
	 * 
	 * see {@link LayoutManager}.
	 * 
	 * @param container
	 *            the container to have as parent.
	 */
	public final void bindToContainer(final WidgetContainer container) {
		if (mFather != null) {
			throw new IllegalStateException("Already bound!!!");
		}

		mFather = container;
	}

	// inherited docs
	/**
	 * 
	 * see {@link LayoutManager}.
	 * 
	 * @param widget
	 *            the widget.
	 * @param constraint
	 *            the constraints.
	 */
	public final void layout(final Widget widget, final Object constraint) {
		if (!(constraint instanceof BorderLayoutConstraint)) {
			throw new IllegalArgumentException("unknown constraint: "
					+ constraint.getClass().getName());
		}

		// Determine where the cell separators lie, based on preferred sizes
		// Ideally we shouldn't have to calculate this when laying out every
		// widget, only when
		// some widget has changed, c.f.
		// java.awt.LayoutManager2.invalidateLayout()
		updateAllSeparators();

		// Fit the current widget into the appropriate cell
		Rectangle rect = mFather.getClientArea();
		if (rect == null) {
			rect = mFather.getSize();
		}
		BorderLayoutConstraint cstr = (BorderLayoutConstraint) constraint;

		Rectangle prefSize = widget.getPreferredSize();
		int prefWidth = prefSize.getWidth();
		int prefHeight = prefSize.getHeight();
		int maxWidth = rect.getWidth(); // Cell width
		int maxHeight = rect.getHeight(); // Cell height
		int x = 0; // Cell x offset
		int y = 0; // Cell y offset

		switch (cstr.getPosition()) {
		case NORTH:
			x = 0;
			y = 0;
			maxHeight = mYSep[0];
			break;

		case SOUTH:
			x = 0;
			y = mYSep[1];
			maxHeight -= mYSep[1];
			break;

		case CENTER:
			x = mXSep[0];
			y = mYSep[0];
			maxWidth = mXSep[1] - mXSep[0];
			maxHeight = mYSep[1] - mYSep[0];
			break;

		case WEST:
			x = 0;
			y = mYSep[0];
			maxWidth = mXSep[0];
			maxHeight = mYSep[1] - mYSep[0];
			break;

		case EAST:
			x = mXSep[1];
			y = mYSep[0];
			maxWidth -= mXSep[1];
			maxHeight = mYSep[1] - mYSep[0];
			break;

		default:
			throw new IllegalStateException("Unknown position for widget: "
					+ cstr.getPosition());
		}

		if (prefWidth <= 0) {
			prefWidth = maxWidth;
		}

		if (prefHeight <= 0) {
			prefHeight = maxHeight;
		}

		/*
		 * Protocol.debug("Widget prelayout for cell " + cstr.mPosition +
		 * " is offset(" + x + "," + y + ")" + " maxsize(" + maxWidth + "," +
		 * maxHeight + ")");
		 */
		int width = 0;
		int height = 0;

		if (prefWidth < maxWidth) {
			widget.setX(getAlignedCoordinate(prefWidth, maxWidth, x,
					cstr.getHorizontalConstraint()));
			width = prefWidth;
		} else {
			widget.setX(x);
			width = maxWidth;
		}

		if (prefHeight < maxHeight) {
			widget.setY(getAlignedCoordinate(prefHeight, maxHeight, y,
					cstr.getVerticalConstraint()));
			height = prefHeight;
		} else {
			widget.setY(y);
			height = maxHeight;
		}

		/*
		 * Protocol.debug("Widget layout for cell " + cstr.mPosition +
		 * " is offset(" + widget.getX() + "," + widget.getY() + ")" +
		 * " maxsize(" + width + "," + height + ")");
		 */
		widget.setSize(new Rectangle(width, height));
	}

	// inherited docs
	public final void removeWidget(final Widget widget) {
		mFather.removeWidget(widget);

		for (int i = 0; i < mSlots.length; i++) {
			if (mSlots[i] != null && mSlots[i].getWidget() == widget) {
				mSlots[i] = null;
			}
		}
	}

	// inherited docs
	public final void unbindFromContainer() {
		mFather = null;
	}

	/**
	 * get the aligned coordinate. TODO: find out what that means.
	 * 
	 * @param prefG
	 *            .
	 * @param contG
	 *            .
	 * @param contC
	 *            .
	 * @param alignment
	 *            .
	 * @return the aligned coordinate.
	 */
	private int getAlignedCoordinate(final int prefG, final int contG,
			final int contC, final int alignment) {
		if (alignment == WidgetsConstants.ALIGNMENT_CENTER) {
			return contC + ((contG - prefG) / 2);
		} else if ((alignment == WidgetsConstants.ALIGNMENT_BOTTOM)
				|| (alignment == WidgetsConstants.ALIGNMENT_RIGHT)) {
			return (contC + contG) - prefG;
		} else {
			return contC;
		}
	}

	/**
	 * Determine separator positions, where top left is 0,0.
	 * 
	 * mXSep[0] | +-+-----+ | N | +-+---+-+- mYSep[0] | | | | |W| C |E| | | | |
	 * +-+---+-+- mYSep[1] | S | +-----+-+ | mXSep[1]
	 */
	private void updateAllSeparators() {
		// Get Rectangle to lay out into -- this is the total area available.
		Rectangle rect = mFather.getClientArea();
		if (rect == null) {
			rect = mFather.getSize();
		}
		updateSeparatorsDimension(mXSep, mSlots[WEST], mSlots[CENTER],
				mSlots[EAST], rect.getWidth(), true);
		updateSeparatorsDimension(mYSep, mSlots[NORTH], mSlots[CENTER],
				mSlots[SOUTH], rect.getHeight(), false);
	}

	/**
	 * Some super logic for how to partition the dimension based on the number
	 * of components and their preferences.
	 * 
	 * This method overwrites one of the input parameters:
	 * <code>int []sep;</code>
	 * 
	 * TODO: find out and document what it all means.
	 * 
	 * @param sep
	 *            separator array.
	 * @param w1
	 *            .
	 * @param w2
	 *            .
	 * @param w3
	 *            .
	 * @param max
	 *            .
	 * @param width
	 *            .
	 */
	private void
			updateSeparatorsDimension(final int[] sep,
					final BorderLayoutConstraint w1,
					final BorderLayoutConstraint w2,
					final BorderLayoutConstraint w3, final int max,
					final boolean width) {
		sep[0] = 0;
		sep[1] = max;

		if (w1 == null && w2 == null && w3 == null) {
			return;
		}

		if (w1 == null && w2 == null && w3 != null) {
			sep[1] = 0;
			return;
		}

		if (w1 != null) {
			Rectangle w1rect = w1.getWidget().getPreferredSize();
			int pref;
			if (width) {
				pref = w1rect.getWidth();
			} else {
				pref = w1rect.getHeight();
			}

			if (pref < 0) {
				sep[0] = -1;
			} else {
				sep[0] += pref;
			}
		}

		if (w3 != null) {
			Rectangle w3rect = w3.getWidget().getPreferredSize();
			int pref;
			if (width) {
				pref = w3rect.getWidth();
			} else {
				pref = w3rect.getHeight();
			}

			if (pref < 0) {
				sep[1] = -1;
			} else {
				sep[1] -= pref;
			}
		}

		// If there are any -1 prefs, distribute them evenly
		// TODO -- should this take into account w2 preferred dimension?
		if (w2 == null) {
			if (sep[0] < 0) {
				if (sep[1] < 0) {
					sep[0] = max / 2;
					sep[1] = max / 2;
				} else {
					sep[0] = sep[1];
				}
			} else if (sep[1] < 0) {
				sep[1] = sep[0];
			}
		} else {
			if (sep[0] < 0) {
				if (sep[1] < 0) {
					sep[0] = max / 3;
					sep[1] = max - sep[0];
				} else {
					sep[0] = sep[1] / 2;
				}
			} else if (sep[1] < 0) {
				sep[1] = max - ((max - sep[0]) / 2);
			}
		}
	}
}

/**
 * Stores the layout preferences for a widget within the BorderLayout.
 */
class BorderLayoutConstraint {
	/**
	 * The widget to be constrained in layout.
	 */
	private Widget mWidget;
	/**
	 * The horizontal constraint.
	 */
	private int mHorizontalConstraint;
	/**
	 * The position.
	 */
	private int mPosition;
	/**
	 * The vertical constraint.
	 */
	private int mVerticalConstraint;

	/**
	 * The constructor for the Border Layout Constraint.
	 * 
	 * @param widget
	 *            .
	 * @param position
	 *            .
	 * @param horizontalConstraint
	 *            .
	 * @param verticalConstraint
	 *            .
	 */
	public BorderLayoutConstraint(final Widget widget, final int position,
			final int horizontalConstraint, final int verticalConstraint) {
		setWidget(widget);
		setPosition(position);
		setHorizontalConstraint(horizontalConstraint);
		setVerticalConstraint(verticalConstraint);
	}

	/**
	 * @return the mWidget
	 */
	public Widget getWidget() {
		return mWidget;
	}

	/**
	 * @param aWidget
	 *            the mWidget to set
	 */
	public void setWidget(final Widget aWidget) {
		this.mWidget = aWidget;
	}

	/**
	 * @return the mHorizontalConstraint
	 */
	public int getHorizontalConstraint() {
		return mHorizontalConstraint;
	}

	/**
	 * @param aHorizontalConstraint
	 *            the mHorizontalConstraint to set
	 */
	public void setHorizontalConstraint(final int aHorizontalConstraint) {
		this.mHorizontalConstraint = aHorizontalConstraint;
	}

	/**
	 * @return the mPosition
	 */
	public int getPosition() {
		return mPosition;
	}

	/**
	 * @param aPosition
	 *            the mPosition to set
	 */
	public void setPosition(final int aPosition) {
		this.mPosition = aPosition;
	}

	/**
	 * @return the mVerticalConstraint
	 */
	protected int getVerticalConstraint() {
		return mVerticalConstraint;
	}

	/**
	 * @param aVerticalConstraint
	 *            the mVerticalConstraint to set
	 */
	protected void setVerticalConstraint(final int aVerticalConstraint) {
		this.mVerticalConstraint = aVerticalConstraint;
	}
}
