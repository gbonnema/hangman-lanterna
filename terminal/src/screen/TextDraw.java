/**
 * 
 */
package screen;

import util.ExperimentException;

import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.terminal.TerminalPosition;

/**
 * @author gbonnema
 * 
 */
public interface TextDraw {

	/**
	 * Draws a box using dashes and "+" characters. The width must be sufficient
	 * to write meaningfully in: at least 5 characters.
	 * 
	 * @param from
	 *          the specified from TerminalPosition (left top corner)
	 * @param to
	 *          the specified to TerminalPosition (right bottom corner)
	 * @throws ExperimentException
	 *           if the width of the box is less than 5 characters.
	 */
	void drawBox(final TerminalPosition from, final TerminalPosition to)
			throws ExperimentException;

	/**
	 * Draws a horizontal line using the character given as a string. Throws an
	 * error if the string is not exactly 1 character long.
	 * 
	 * @param col
	 *          The column to start in
	 * @param line
	 *          the line to start in
	 * @param width
	 *          the length of the line
	 * @param ch
	 *          the specified String, must be 1 character
	 * @throws ExperimentException
	 *           when the string is more or less than 1 character.
	 */
	void drawHorLine(int col, int line, int width, String ch)
			throws ExperimentException;

	/**
	 * Draws a horizontal dash line using drawHorLine.
	 * 
	 * @param line
	 *          the line number
	 */
	void drawHorDashLine(int line);

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
	String fillLine(final char startchar, final char endchar, final char midchar,
			final int len) throws ExperimentException;

	/**
	 * Draws a string on the screen at a particular position
	 * 
	 * @param x
	 *          0-indexed column number of where to put the first character in the
	 *          string
	 * @param y
	 *          0-indexed row number of where to put the first character in the
	 *          string
	 * @param string
	 *          Text to put on the screen
	 * @param styles
	 *          Additional styles to apply to the text
	 */
	public void drawString(final int x, final int y, final String string,
			final ScreenCharacterStyle... styles);

}
