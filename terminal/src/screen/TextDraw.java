/**
 * Copyright 2014 Guus Bonnema, Dieren, The Netherlands.
 * 
 * This file is part of hangman-lanterna.
 * 
 * hangman-lanterna is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * hangman-lanterna is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * hangman-lanterna. If not, see <http://www.gnu.org/licenses/>.
 */
package screen;

import util.ExperimentException;

import com.googlecode.lanterna.screen.ScreenWriter;
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
	 * 
	 * @return the absolute screen writer.
	 */
	public ScreenWriter getAbsScreenWriter();

	/**
	 * Refresh the screen.
	 */
	public void refreshScreen();

}
