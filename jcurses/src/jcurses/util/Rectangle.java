/**
 * jcurses.util.
 */
package jcurses.util;

/**
 * This is a class to represent an screen rectangle. To implement this class was
 * needed, because <code>java.awt.rectangle</code> works with double's, this is
 * by a text based terminal senseless.
 */
public class Rectangle {
	/**
	 * The height of the rectangle.
	 */
	private int height = 0;
	/**
	 * The width of the rectangle.
	 */
	private int width = 0;
	/**
	 * The x coordinate of the rectangle's top left corner.
	 */
	private int x = 0;
	/**
	 * The y coordinate of the rectangle's top left corner.
	 */
	private int y = 0;

	/**
	 * The constructor.
	 * 
	 * @param xLeftTop
	 *            the x coordinate of the top left corner
	 * @param yLeftTop
	 *            the y coordinate of the top left corner
	 * @param wRect
	 *            the width of the rectangle
	 * @param hRect
	 *            the height of the rectangle
	 */
	public Rectangle(final int xLeftTop, final int yLeftTop, final int wRect,
			final int hRect) {
		setX(xLeftTop);
		setY(yLeftTop);
		setWidth(wRect);
		setHeight(hRect);
	}

	/**
	 * Default constructor.
	 */
	public Rectangle() {
		// need zero arg constructor
	}
	
	/**
	 * Copy constructor.
	 * 
	 * @param aRect the rectangle from which we copy construct
	 */
	public Rectangle(final Rectangle aRect) {
		setX(aRect.getX());
		setY(aRect.getY());
		setWidth(aRect.getWidth());
		setHeight(aRect.getHeight());
	}

	/**
	 * The constructor, that defines only the size but no location.
	 * 
	 * @param rectWidth
	 *            the width of the rectangle
	 * @param rectHeight
	 *            the height of the rectangle
	 */
	public Rectangle(final int rectWidth, final int rectHeight) {
		setWidth(rectWidth);
		setHeight(rectHeight);
	}

	/**
	 * Returns true if this rectangle is ABOVE the specified rectangle.
	 * 
	 * @param aRect
	 *            The reference rectangle for the comparison
	 * 
	 * @return true if this rectangle is above, or false if it is not.
	 */
	public final boolean isAbove(final Rectangle aRect) {
		return getBottom() < aRect.getTop();
	}

	/**
	 * Returns true if the this rectangle is BELOW the specified rectangle.
	 * 
	 * @param aRect
	 *            The reference rectangle for the comparison
	 * 
	 * @return true if this rectangle is below, or false if it is not.
	 */
	public final boolean isBelow(final Rectangle aRect) {
		return getTop() > aRect.getBottom();
	}

	/**
	 * Returns the bottom coordinate.
	 * 
	 * @return the bottom coordinate.
	 */
	public final int getBottom() {
		return (getTop() + getHeight()) - 1;
	}

	/**
	 * Returns true or false indicating if the Rectangle is Empty. Empty is
	 * defined in this case as covering 0 units of area in the arbitrary
	 * coordinate system used for specifying location and size. If the rectangle
	 * occupies at least one unit of area, is is considered not empty, and false
	 * is returned.
	 * 
	 * @return <code>true</code> if the rectangle is empty in other case
	 *         <code>false</code>
	 */
	public final boolean isEmpty() {
		return (getWidth() <= 0) || (getHeight() <= 0);
	}

	/**
	 * Sets the height of the rectangle.
	 * 
	 * @param rectHeight
	 *            the height of the rectangle to set
	 */
	public final void setHeight(final int rectHeight) {
		this.height = rectHeight;
	}

	/**
	 * Gets the height of the rectangle.
	 * 
	 * @return the height of the rectangle.
	 */
	public final int getHeight() {
		return this.height;
	}

	/**
	 * Get the X coordinate.
	 * 
	 * @return the X coordinate.
	 */
	public final int getLeft() {
		return getX();
	}

	/**
	 * Returns true if the this rectangle is to the LEFT the specified
	 * rectangle.
	 * 
	 * @param aRect
	 *            The reference rectangle for the comparison
	 * 
	 * @return true if this rectangle is to the left, or false if it is not.
	 */
	public final boolean isLeftOf(final Rectangle aRect) {
		return getRight() < aRect.getLeft();
	}

	/**
	 * Sets the location of the rectangle.
	 * 
	 * @param xNew
	 *            new x coordinate
	 * @param yNew
	 *            new y coordinate
	 */
	public final void setLocation(final int xNew, final int yNew) {
		setX(xNew);
		setY(yNew);
	}

	/**
	 * Adjust the size of the rectangle.
	 * 
	 * @param aWide
	 *            the new width
	 * @param aHigh
	 *            the new height
	 */
	public final void setSize(final int aWide, final int aHigh) {
		setWidth(aWide);
		setHeight(aHigh);
	}

	/**
	 * Perform a move by aX and xY.
	 * 
	 * @param aX
	 *            X shift.
	 * @param aY
	 *            Y shift.
	 */
	public final void move(final int aX, final int aY) {
		setX(getX() + aX);
		setY(getY() + aY);
	}

	/**
	 * Calculate the right coordinate.
	 * 
	 * @return the right coordinate.
	 */
	public final int getRight() {
		return (getLeft() + getWidth()) - 1;
	}

	/**
	 * Returns true if the this rectangle is to the RIGHT the specified
	 * rectangle.
	 * 
	 * @param aRect
	 *            The reference rectangle for the comparison
	 * 
	 * @return true if this rectangle is to the right, or false if it is not.
	 */
	public final boolean isRightOf(final Rectangle aRect) {
		return getLeft() > aRect.getRight();
	}

	/**
	 * Get the top Y coordinate.
	 * 
	 * @return the top Y coordinate.
	 */
	public final int getTop() {
		return getY();
	}

	/**
	 * Sets the width of the rectangle.
	 * 
	 * @param wNew
	 *            the width of the rectangle to set
	 */
	public final void setWidth(final int wNew) {
		this.width = wNew;
	}

	/**
	 * Gets the Width of the rectangle.
	 * 
	 * @return the width of the rectangle
	 */
	public final int getWidth() {
		return this.width;
	}

	/**
	 * Sets the x coordinate of the top left corner.
	 * 
	 * @param xNew
	 *            the x coordinate of the top left corner to set
	 */
	public final void setX(final int xNew) {
		this.x = xNew;
	}

	/**
	 * Gets the x coordinate of the top left corner.
	 * 
	 * @return the x coordinate of the top left corner
	 */
	public final int getX() {
		return this.x;
	}

	/**
	 * Sets the y coordinate of the top left corner.
	 * 
	 * @param yNew
	 *            the x coordinate of the top left corner to set
	 */
	public final void setY(final int yNew) {
		this.y = yNew;
	}

	/**
	 * Gets the y coordinate of the top left corner.
	 * 
	 * @return the y coordinate of the top left corner
	 */
	public final int getY() {
		return this.y;
	}

	/**
	 * Clones this rectangle.
	 * 
	 * @return Object a copy of the current rectangle.
	 */
	public final Object clone() {
		return new Rectangle(getX(), getY(), getWidth(), getHeight());
	}

	/**
	 * Copies the size and location to the specified rectangle.
	 * 
	 * @param aTarget
	 *            the target rectangle.
	 */
	public final void copyTo(final Rectangle aTarget) {
		aTarget.setSize(getWidth(), getHeight());
		aTarget.setLocation(getX(), getY());

	}

	/**
	 * The method verifies, whether a rectangle lies within this rectangle.
	 * 
	 * @param xRect
	 *            x coordinate of the rectangle, whose containment is to verify
	 * @param yRect
	 *            y coordinate of the rectangle, whose containment is to verify
	 * @param wRect
	 *            width of the rectangle, whose containment is to verify
	 * @param hRect
	 *            x height of the rectangle, whose containment is to verify
	 * 
	 * @return <code>true</code> if the parameter rectangle is within this
	 *         rectangle in other case <code>false</code>
	 */
	public final boolean contains(final int xRect, final int yRect,
			final int wRect, final int hRect) {
		int w = getWidth();
		int h = getHeight();

		if ((w <= 0) || (h <= 0) || (wRect <= 0) || (hRect <= 0)) {
			return false;
		}

		return ((xRect >= getX()) && (yRect >= getY())
				&& ((xRect + wRect) <= (getX() + w)) 
				&& ((yRect + hRect) <= (getY() + h)));
	}

	/**
	 * The method verifies, whether a rectangle lies within this rectangle.
	 * 
	 * @param rect
	 *            the rectangle, whose containment is to be verified
	 * 
	 * @return <code>true</code> if the parameter rectangle is within this
	 *         rectangle in other case <code>false</code>
	 */
	public final boolean contains(final Rectangle rect) {
		return contains(rect.getX(), rect.getY(), rect.getWidth(), rect
				.getHeight());
	}

	/**
	 * Returns the square of the distance between the center point of this
	 * Rectangle and the specified Rectangle.
	 * 
	 * @param aRect
	 *            the reference rectangle from which distance is to be
	 *            calculated
	 * 
	 * @return the squared distance in units as an integer
	 */
	public final int distanceSquaredFrom(final Rectangle aRect) {
		int xDist = (aRect.getX() + (aRect.getWidth() / 2)) - getX()
				+ (getWidth() / 2);
		int yDist = (aRect.getY() + (aRect.getHeight() / 2)) - getY()
				+ (getHeight() / 2);
		return (xDist * xDist) + (yDist * yDist);
	}

	/**
	 * Returns the horizontally biased distance between the origin point of this
	 * Rectangle and the specified Rectangle.
	 * 
	 * @param aRect
	 *            the reference rectangle from which distance is to be
	 *            calculated
	 * 
	 * @return the horizontal distance in integer units
	 */
	public final int horizontalDistanceFrom(final Rectangle aRect) {
		return Math.abs(aRect.getX() - getX());
	}

	/**
	 * Returns the horizontally biased distance between the center point of this
	 * Rectangle and the specified Rectangle.
	 * 
	 * @param aRect
	 *            the reference rectangle from which distance is to be
	 *            calculated
	 * 
	 * @return horizontal distance in integer units
	 */
	public final int horizontalDistanceFromCenters(final Rectangle aRect) {
		return Math.abs((aRect.getX() + (aRect.getWidth() / 2)) - getX()
				+ (getWidth() / 2));
	}

	/**
	 * The method verifies, whether a point lies within this rectangle.
	 * 
	 * @param xPoint
	 *            x coordinate of the point, whose containment is to verify
	 * @param yPoint
	 *            y coordinate of the point, whose containment is to verify
	 * 
	 * @return <code>true</code> if the point is within this rectangle in other
	 *         case <code>false</code>
	 */
	public final boolean inside(final int xPoint, final int yPoint) {
		return (xPoint >= getX()) && ((xPoint - getX()) < getWidth())
				&& (yPoint >= getY()) && ((yPoint - getY()) < getHeight());
	}

	/**
	 * The method returns an intersection of the rectangle with an other
	 * rectangle, that is, the greatest rectangle, that is contained in both.
	 * 
	 * @param r
	 *            rectangle to build intersection with this rectangle
	 * 
	 * @return the intersection rectangle
	 */
	public final Rectangle intersection(final Rectangle r) {
		if (isEmpty()) {
			return (Rectangle) this.clone();
		} else if (r.isEmpty()) {
			return (Rectangle) r.clone();
		} else {
			int x1 = Math.max(getX(), r.getX());
			int x2 = Math.min(getX() + getWidth(), r.getX() + r.getWidth());
			int y1 = Math.max(getY(), r.getY());
			int y2 = Math.min(getY() + getHeight(), r.getY() + r.getHeight());

			if (((x2 - x1) < 0) || ((y2 - y1) < 0)) {
				// Width or height is negative. No intersection.
				return new Rectangle(0, 0, 0, 0);
			}

			return new Rectangle(x1, y1, x2 - x1, y2 - y1);
		}
	}

	/**
	 * Changes the size of the rectangle.
	 * 
	 * @param widthNew
	 *            new width
	 * @param heightNew
	 *            new height
	 */
	public final void resize(final int widthNew, final int heightNew) {
		setWidth(widthNew);
		setHeight(heightNew);
	}

	/**
	 * Returns the square of the shortest distance between the edge of this
	 * Rectangle and the edge of the specified Rectangle.
	 * 
	 * @param aRect
	 *            the reference rectangle from which distance is to be
	 *            calculated
	 * 
	 * @return the squared distance in units as an integer
	 */
	public final int shortestSquaredDistanceFrom(final Rectangle aRect) {
		int xDist = 0;

		if (getRight() <= aRect.getLeft()) {
			xDist = aRect.getLeft() - getRight();
		} else if (getLeft() >= aRect.getRight()) {
			xDist = aRect.getRight() - getLeft();
		} else {
			xDist = 0; // overlapping X
		}

		int yDist = 0;

		if (getTop() >= aRect.getBottom()) {
			yDist = aRect.getBottom() - getTop();
		} else if (getBottom() <= aRect.getTop()) {
			yDist = aRect.getTop() - getBottom();
		} else {
			yDist = 0; // overlapping Y
		}

		return (xDist * xDist) + (yDist * yDist);
	}

	/**
	 * print the rectangle properties.
	 * 
	 * @return properties of this rectangle.
	 */
	public final String toString() {
		return "[x=" + getX() + ",y=" + getY() + ",width=" + getWidth()
				+ ",height=" + getHeight() + ",isEmpty=" + isEmpty() + "]";
	}

	/**
	 * The method returns an union of the rectangle with an other rectangle,
	 * that is, the smallest rectangle, that contains both.
	 * 
	 * @param r
	 *            rectangle to build union with this rectangle
	 * 
	 * @return the union rectangle
	 */
	public final Rectangle union(final Rectangle r) {
		if (isEmpty()) {
			return (Rectangle) r.clone();
		} else if (r.isEmpty()) {
			return (Rectangle) this.clone();
		} else {
			int x1 = Math.min(getX(), r.getX());
			int x2 = Math.max(getX() + getWidth(), r.getX() + r.getWidth());
			int y1 = Math.min(getY(), r.getY());
			int y2 = Math.max(getY() + getHeight(), r.getY() + r.getHeight());
			return new Rectangle(x1, y1, x2 - x1, y2 - y1);
		}
	}

	/**
	 * Returns the vertical distance between the origin point of this Rectangle
	 * and the specified Rectangle.
	 * 
	 * @param aRect
	 *            the reference rectangle from which distance is to be
	 *            calculated
	 * 
	 * @return vertical distance in integer units
	 */
	public final int verticalDistanceFrom(final Rectangle aRect) {
		return Math.abs(aRect.getY() - getY());
	}

	/**
	 * Returns the vertical distance between the center point of this Rectangle
	 * and the specified Rectangle.
	 * 
	 * @param aRect
	 *            the reference rectangle from which distance is to be
	 *            calculated
	 * 
	 * @return vertical distance in integer units
	 */
	public final int verticalDistanceFromCenters(final Rectangle aRect) {
		return Math.abs((aRect.getY() + (aRect.getHeight() / 2)) - getY()
				+ (getHeight() / 2));
	}
}