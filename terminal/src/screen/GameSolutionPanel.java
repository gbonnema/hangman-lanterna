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

import hangman.HangmanGame;

import java.util.Observable;
import java.util.Observer;

import util.Utils;

/**
 * @author gbonnema
 * 
 */
public class GameSolutionPanel extends AbstractPanel implements Observer {

	private String _solution;
	private String _hexSolution;

	/**
	 * @param mainScreen
	 */
	public GameSolutionPanel(TextDraw mainScreen) {
		super(mainScreen);
	}

	private void setSolution(String solution) {
		_solution = solution;
		_hexSolution = Utils.convert2Hex(_solution);
	}

	public void refreshEntry() {
		refresh();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see screen.AbstractPanel#refresh()
	 */
	@Override
	public void refresh() {
		drawBorder();
		int x = getPadding();
		int y = getPadding();
		String prepend = " ";
		String mid = "  ";
		String postpend = "";
		drawString(x, y, Utils.disperse(_solution, prepend, mid, postpend));
		y++;
		drawString(x, y, _hexSolution);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof HangmanGame) {
			HangmanGame game = (HangmanGame) o;
			setSolution(game.getSolution());
			refreshEntry();
			refreshScreen();
		}
	}
}
