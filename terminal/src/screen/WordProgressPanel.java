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

import util.Utils;

import com.googlecode.lanterna.terminal.TerminalPosition;

/**
 * Shows the progress of the guessing in hangman game. It Implements observer of
 * hangman game. The parent adds this panel as an observer. When update() runs,
 * it updates the guess array containing the guessed characters and shows them
 * on screen.
 * 
 * @author gbonnema
 * 
 */
public class WordProgressPanel extends AbstractPanel implements Observer {

	private ArrayList<Character> _guessArray;

	/**
	 * @param mainScreen
	 */
	public WordProgressPanel(TextDraw mainScreen) {
		super(mainScreen);
	}

	private void refreshEntry() {
		TerminalPosition coord = new TerminalPosition(getPadding(), getPadding());
		String spacedGuessArray = Utils.disperse(_guessArray);
		drawString(coord.getColumn(), coord.getRow(), spacedGuessArray);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see screen.AbstractPanel#refresh()
	 */
	@Override
	public void refresh() {
		refreshEntry();
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
			_guessArray = game.getWordProgressArray();
			refresh();
			refreshScreen();
		}
	}
}
