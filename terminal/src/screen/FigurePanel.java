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

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Shows the figure of hangman while guessing characters of the word.
 * 
 * @author gbonnema
 * 
 */
public class FigurePanel extends AbstractPanel implements Observer {

	private ArrayList<String> _figure;

	/**
	 * @param mainScreen
	 */
	public FigurePanel(TextDraw mainScreen, String title) {
		super(mainScreen, title);
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
		for (String line : _figure) {
			// int maxWidth = Math.min(getWidth() - 2 * getPadding(), line.length());
			// line = line.substring(0, maxWidth - 1);
			drawString(x, y, line);
			y++;
		}
		refreshScreen();
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
			_figure = game.getFigure();
			refresh();
		}

	}

}
