package jcurses.widgets;

import jcurses.system.Toolkit;
import jcurses.util.Rectangle;

/**
 * This class is used by widgets, having scrollable content, to paint
 * scrollbars. The class <code>jcurses.util.ScrollbarUtils</code> is deprecated.
 * 
 */
public class ScrollbarPainter {
	private IScrollable widget = null;
	private Rectangle borderRectangle = null;
	private ScrollbarData currentScrollbarData = null;

	/**
	 * The constructor
	 * 
	 * @param aWidget
	 *            the widget, that needs scrollbars
	 * 
	 */
	public ScrollbarPainter(final IScrollable aWidget) {
		widget = aWidget;
	}

	/**
	 * This method must be called, if the widget is painted or repainted. If the
	 * the widget was already visible, but the content (position ) has been
	 * changed, the method <code>refresh</code> must be used
	 * 
	 */
	public void paint() {
		currentScrollbarData = readData();
		drawHorizontalScrollbar();
		drawVerticalScrollbar();
	}

	/**
	 * This method is to be called if the scrollable content ( or it's position
	 * ) has been ( or could have been ) changed, to refresh scrollbars only if
	 * needed.
	 * 
	 */
	public void refresh() {
		ScrollbarData saveSB = currentScrollbarData;
		currentScrollbarData = readData();
		/* @formatter:off */
		if ((saveSB.horizontalOffset != currentScrollbarData.horizontalOffset)
			|| 
			(saveSB.horizontalLength 
					!= currentScrollbarData.horizontalLength)) {
			refreshHorizontalScrollbar(saveSB);
		}
		if ((saveSB.horizontalOffset != currentScrollbarData.horizontalOffset)
			|| (saveSB.horizontalLength 
					!= currentScrollbarData.horizontalLength)) {
			refreshVerticalScrollbar(saveSB);
		}
		/* @formatter:on */
	}

	private ScrollbarData readData() {
		ScrollbarData result = new ScrollbarData();
		borderRectangle = widget.getBorderRectangle();
		int horizontalLength = borderRectangle.getWidth() - 2;
		int verticalLength = borderRectangle.getHeight() - 2;
		if (widget.hasHorizontalScrollbar()
				&& (widget.getHorizontalScrollbarLength() > 0)) {
			result.horizontalOffset =
					Math.round(horizontalLength
							* widget.getHorizontalScrollbarOffset());
			result.horizontalLength =
					Math.round(horizontalLength
							* widget.getHorizontalScrollbarLength());
			if (result.horizontalLength == 0) {
				result.horizontalLength = 1;
			}

			/* @formatter:off */
			if ((result.horizontalLength 
					+ result.horizontalOffset) > horizontalLength) {
				result.horizontalOffset =
						horizontalLength - result.horizontalLength;
			}
			/* @formatter:on */

			if (horizontalLength == result.horizontalLength) {
				result.horizontalLength = 0;
			}
		}

		if (widget.hasVerticalScrollbar()
				&& (widget.getVerticalScrollbarLength() > 0)) {

			result.verticalOffset =
					Math.round(verticalLength
							* widget.getVerticalScrollbarOffset());
			result.verticalLength =
					Math.round(verticalLength
							* widget.getVerticalScrollbarLength());
			result.verticalLength =
					(result.verticalLength == 0) ? 1 : result.verticalLength;

			/* @formatter:off */
			if ((result.verticalLength 
					+ result.verticalOffset) > verticalLength) {
				result.verticalOffset = verticalLength - result.verticalLength;
			}
			/* @formatter:on */

			if (verticalLength == result.verticalLength) {
				result.verticalLength = 0;
			}

		}
		return result;
	}

	private void drawHorizontalScrollbar() {
		int offset = currentScrollbarData.horizontalOffset;
		int length = currentScrollbarData.horizontalLength;
		if (widget.hasHorizontalScrollbar() && (length > 0)) {
			Toolkit.drawHorizontalThickLine(borderRectangle.getX() + 1
					+ offset,
					borderRectangle.getY() + borderRectangle.getHeight() - 1,
					borderRectangle.getX() + offset + length,
					widget.getScrollbarColors());
		}
	}

	private void drawVerticalScrollbar() {
		int offset = currentScrollbarData.verticalOffset;
		int length = currentScrollbarData.verticalLength;
		if (widget.hasVerticalScrollbar() && (length > 0)) {
			Toolkit.drawVerticalThickLine(borderRectangle.getX()
					+ borderRectangle.getWidth() - 1, borderRectangle.getY()
					+ 1 + offset, borderRectangle.getY() + offset + length,
					widget.getScrollbarColors());
		}
	}

	private void refreshVerticalScrollbar(final ScrollbarData old) {
		if (widget.hasVerticalScrollbar()) {
			if (old.verticalLength > 0) {
				Toolkit.drawVLineClip(borderRectangle.getX()
						+ borderRectangle.getWidth() - 1,
						borderRectangle.getY() + 1 + old.verticalOffset,
						borderRectangle.getY() + old.verticalOffset
								+ old.verticalLength, widget.getBorderColors());
			}
			drawVerticalScrollbar();
		}
	}

	private void refreshHorizontalScrollbar(final ScrollbarData old) {
		if (widget.hasHorizontalScrollbar()) {
			if (old.horizontalLength > 0) {
				Toolkit.drawHLineClip(borderRectangle.getX() + 1
						+ old.horizontalOffset, borderRectangle.getY()
						+ borderRectangle.getHeight() - 1,
						borderRectangle.getX() + old.horizontalOffset
								+ old.horizontalLength,
						widget.getBorderColors());
			}
			drawHorizontalScrollbar();
		}
	}
	/**
	 * Just a data class for private use: access to items is public. 
	 *
	 */
	private class ScrollbarData {
		public int horizontalOffset = 0;
		public int horizontalLength = 0;
		public int verticalOffset = 0;
		public int verticalLength = 0;

		public String toString() {
			return "hoffset=" + horizontalOffset + ",hlength="
					+ horizontalLength + ",voffset=" + verticalOffset
					+ ",vlength=" + verticalLength;
		}
	}
}