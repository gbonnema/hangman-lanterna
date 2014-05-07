/**
 * File containing the class Grid.
 */
package jcurses.widgets;

import jcurses.util.Rectangle;

/**
 * This class describes the Grid.
 * 
 */
class Grid {

	/**
	 * An array of widths.
	 */
	private int[] widthArray;
	/**
	 * An array of heights.
	 */
	private int[] heightArray;

	/**
	 * A constructor of Grid using the full rectangle to be divided and the
	 * width and height to be used to divide the area.
	 * 
	 * @param rect
	 *            The rectangle specifying the whole area.
	 * @param aWidth
	 *            The width of each grid cell.
	 * @param aHeight
	 *            The height of each grid cell.
	 */
	public Grid(final Rectangle rect, final int aWidth, final int aHeight) {
		if (((rect.getWidth() / aWidth) < 1)
				|| ((rect.getHeight() / aHeight) < 1)) {
			throw new RuntimeException(" the grid is to fine: "
					+ rect.getWidth() + ":" + rect.getHeight() + ":" + aWidth
					+ ":" + aHeight);
		}

		widthArray = new int[aWidth];
		heightArray = new int[aHeight];

		fillArray(widthArray, rect.getWidth(), aWidth);
		fillArray(heightArray, rect.getHeight(), aHeight);

	}

	/**
	 * Fill an array with grid cells.
	 * 
	 * @param array
	 *            The array to be filled.
	 * @param aRectWidth
	 *            The rectangle width : the area to be divided.
	 * @param aWidth
	 *            The width of each cell.
	 */
	private void fillArray(final int[] array, final int aRectWidth,
			final int aWidth) {
		int mod = aRectWidth % aWidth;
		int cellWidth = aRectWidth / aWidth;

		for (int i = 0; i < aWidth; i++) {
			if (mod > 0) {
				array[i] = cellWidth + 1;
				mod--;
			} else {
				array[i] = cellWidth;
			}
		}

	}

	/**
	 * TODO: describe what this method does.
	 * 
	 * @param x
	 *            An x coordinate.
	 * @param y
	 *            A y coordinate.
	 * @param aWidth
	 *            A width.
	 * @param aHeight
	 *            A height.
	 * @return A rectangle of some sort.
	 */
	public Rectangle getRectangle(final int x, final int y, final int aWidth,
			final int aHeight) {
		return new Rectangle(getLength(widthArray, 0, x), getLength(
				heightArray, 0, y), getLength(widthArray, x, x + aWidth),
				getLength(heightArray, y, y + aHeight));

	}

	/**
	 * Get the length of a segment from a specified array and the start and end
	 * of the segment. This array is either the width array or the height array.
	 * So the length is one of both.
	 * 
	 * @param array
	 *            The array.
	 * @param begin
	 *            Where to start.
	 * @param end
	 *            Where to end.
	 * @return The width.
	 */
	private int getLength(final int[] array, final int begin, final int end) {
		int aWidth = 0;
		for (int i = begin; i < end; i++) {
			aWidth += array[i];
		}

		return aWidth;
	}
}