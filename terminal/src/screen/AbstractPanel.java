/**
 * 
 */
package screen;

import util.Utils;

import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.screen.ScreenWriter;
import com.googlecode.lanterna.terminal.TerminalPosition;

/**
 * @author gbonnema
 * 
 */
public abstract class AbstractPanel {

	private final TerminalPosition	_defTopLeft	= new TerminalPosition(0, 0);
	private final TerminalPosition	_defSize		= new TerminalPosition(10, 10);

	private int											_padding		= 2;

	private TextDraw								_mainScreen;
	private ScreenWriter						_absScreenWriter;
	private TerminalPosition				_absTopLeftCorner;
	private TerminalPosition				_absPanelSize;
	private int											_absLeft, _absRight, _absTop, _absBottom;
	private int											_height, _width;

	/**
	 * Constructor for this panel.
	 * 
	 * @param topleftCorner
	 *          The top left corner of the panel area.
	 * @param size
	 *          the length and width of the panel in columns and rows.
	 */
	public AbstractPanel(TextDraw mainScreen) {
		_mainScreen = mainScreen;
		resetPanel(_defTopLeft, _defSize);
		_absScreenWriter = _mainScreen.getAbsScreenWriter();
		calcBorders();
		/* make sure the content is on screen */
	}

	/**
	 * Called by constructor. Uses topleft corner and size to determine left,
	 * right, top and bottom.
	 */
	private void calcBorders() {
		_absLeft = _absTopLeftCorner.getColumn();
		_absRight = _absTopLeftCorner.getColumn() + _absPanelSize.getColumn() - 1;
		_absTop = _absTopLeftCorner.getRow();
		_absBottom = _absTopLeftCorner.getRow() + _absPanelSize.getRow() - 1;
		_height = _absPanelSize.getRow();
		_width = _absPanelSize.getColumn();
	}

	protected void drawHorDashLine(int line) {
		drawHorLine(line, "-");
	}

	protected void drawHorLine(int line, String ch) {
		String horLine = createHorLine(ch);
		int x = 0;
		int y = line;

		panelWrite(x, y, horLine);
	}

	/**
	 * Draws a box within the panel.
	 * 
	 * @param from
	 * @param to
	 */
	public void drawBox(final TerminalPosition from, final TerminalPosition to) {

		String topStr, bottomStr, midStr;
		int left = from.getColumn();
		int right = to.getColumn();
		int top = from.getRow();
		int bottom = to.getRow();
		int width = right - left;
		Utils.checkArg(width >= 5, "minimum width box = 5. specified: " + width
				+ "\n" + "\n" + "from = " + from + ", to = " + to + "\n");

		topStr = fillLine('+', '+', '-', width);
		bottomStr = fillLine('+', '+', '-', width);
		midStr = fillLine('|', '|', ' ', width);
		/* Draw top and bottom first */
		drawString(left, top, topStr);
		drawString(left, bottom, bottomStr);
		/* Now draw the middle */
		int height = bottom - top + 1;
		int innerheight = height - 2;
		for (int i = 0; i < innerheight; i++) {
			int y = top + 1 + i;
			drawString(left, y, midStr);
		}
	}

	/**
	 * Builds a line start with one startchar en ending with an endchar, in the
	 * middle a midchar. Returns the result.
	 * 
	 * You can use this to create a box, or just a line
	 * 
	 * @param startchar
	 *          The first character of the line.
	 * @param endchar
	 *          The last character of the line.
	 * @param midchar
	 *          The characters in the middle
	 * @param len
	 *          The length of the line.
	 * @return the resulting line as a String.
	 */
	public String fillLine(final char startchar, final char endchar,
			final char midchar, final int len) {
		Utils.checkArg(len > 2, "wrong length for filling a line.");
		StringBuilder result = new StringBuilder();
		result.append(startchar);
		for (int i = 1; i < len; i++) {
			result.append(midchar);
		}
		result.append(endchar);
		return result.toString();
	}

	/**
	 * Prints the specified string at the specified x and y coordinates. If the
	 * string is outside panel that part is not printed.
	 * 
	 * @param x
	 *          the relative x coordinate.
	 * @param y
	 *          the relative y coordinate.
	 * @param string
	 *          the specified string to be printed on panel.
	 */
	private void panelWrite(int x_, int y_, String string_) {
		String string = clipString(string_, x_, y_);

		int x = rel2absX(x_);
		int y = rel2absY(y_);
		_absScreenWriter.drawString(x, y, string);
	}

	protected void drawHorLine(int col, int line, int width, String ch) {
		String horLine = createHorLine(ch, width);
		panelWrite(col, line, horLine);
	}

	/**
	 * Returns a horizontal line for the width of the panel consisting of the
	 * specified character.
	 * 
	 * @param ch
	 *          the specified character.
	 * @return a string containing the specified character for the width of the
	 *         panel.
	 */
	protected String createHorLine(String ch) {
		return createHorLine(ch, _width);
	}

	/**
	 * Returns a string with the specified character in each position for the
	 * specified length.
	 * 
	 * @param ch
	 *          the specified character.
	 * @param length
	 *          the specified length.
	 * @return a string consisting of the specified character.
	 */
	protected String createHorLine(String ch, int length) {
		Utils.checkArg(ch.length() == 1, "String must have 1 character: " + ch);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(ch);
		}
		/* Clip the extraneous length if more than the panel's width */
		String result = sb.toString();
		result = clipString(result, 0, 0);
		return result;
	}

	/**
	 * Translate local coordinates x_ and y_ to absolute coordinates, relative to
	 * the screen. This is will only draw the part of the string that is inside
	 * the panel.
	 * 
	 * @param x_
	 *          the relative x coordinate.
	 * @param y_
	 *          the relative y coordinate.
	 * @param string_
	 *          The string to be drawn.
	 * @param styles_
	 */
	protected final void drawString(final int x_, final int y_,
			final String string_, final ScreenCharacterStyle... styles_) {
		int x = x_;
		int y = y_;
		String string = string_;

		string = clipString(string, x, y);

		/* translate to absolute coordinates and draw the string */
		x = rel2absX(x);
		y = rel2absY(y);
		_absScreenWriter.drawString(x, y, string, styles_);
	}

	private int rel2absX(final int x) {
		return _absLeft + x;
	}

	private int rel2absY(final int y) {
		return _absTop + y;
	}

	/**
	 * Returns true if the coordinates are inside the panel.
	 * 
	 * @param x
	 *          relative x coordinate.
	 * @param y
	 *          relative y coordinate.
	 * @return true if the relative coordinates are inside the panel.
	 */
	private boolean insidePanel(int x, int y) {
		return !(x < 0 || y < 0 || x > _width || y > _height);
	}

	/**
	 * Return whether the string is fully in the panel. If it is not inside the
	 * panel at all, return an empty string. Otherwise, clip the string to fit
	 * inside the panel.
	 * 
	 * @param s
	 *          The specified string
	 * @param x
	 *          the specified relative x coord.
	 * @param y
	 *          the specified relative y coord.
	 * @return the clipped string or an empty string if outside panel.
	 */
	private String clipString(final String s, final int x, final int y) {
		if (!insidePanel(x, y)) {
			return "";
		}
		String string = s;
		if (string.length() + x > _width) {
			string = string.substring(0, _width - x);
		}
		return string;
	}

	/**
	 * @return the left coordinate of the panel.
	 */
	public int getLeft() {
		return _absLeft;
	}

	/**
	 * @return the right coordinate of the panel.
	 */
	public int getRight() {
		return _absRight;
	}

	/**
	 * @return the top coordinate of the panel.
	 */
	public int getTop() {
		return _absTop;
	}

	/**
	 * @return the bottom coordinate of the panel.
	 */
	public int getBottom() {
		return _absBottom;
	}

	/**
	 * @return the width coordinate of the panel.
	 */
	public int getWidth() {
		return _width;
	}

	/**
	 * @return the height coordinate of the panel.
	 */
	public int getHeight() {
		return _height;
	}

	/**
	 * @return the _padding
	 */
	public int getPadding() {
		return _padding;
	}

	/**
	 * @param _padding
	 *          the _padding to set
	 */
	public void setPadding(int _padding) {
		this._padding = _padding;
	}

	/**
	 * Reset the left corner and size of the panel. The caller should refresh the
	 * contents of the panel.
	 * 
	 * @param topleftCorner
	 *          The topleft corner in absolute coordinates (for the parent)
	 * @param size
	 *          the size
	 */
	public void resetPanel(TerminalPosition topleftCorner, TerminalPosition size) {
		_absTopLeftCorner = topleftCorner;
		_absPanelSize = size;
		calcBorders();
	}

	protected void refreshScreen() {
		this._mainScreen.refreshScreen();
	}

	/**
	 * Abstract method that the extending object should implement. It should
	 * refresh the panel contents.
	 */
	public abstract void refresh();
}