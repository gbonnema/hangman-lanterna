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

import com.googlecode.lanterna.screen.ScreenCharacterStyle;

/**
 * @author gbonnema
 * 
 */
public class GameMessagePanel extends AbstractPanel implements Observer {

	private String _gameMessage;

	/**
	 * @param mainScreen
	 */
	public GameMessagePanel(TextDraw mainScreen) {
		super(mainScreen);
		_gameMessage = "";
	}

	public void refreshEntry() {
		int x = getPadding();
		int y = getPadding();
		ScreenCharacterStyle style = ScreenCharacterStyle.Bold;
		String msg = fillTrailingSpace(_gameMessage);
		drawString(x, y, msg, style);
	}

	private String fillTrailingSpace(String msg) {
		StringBuilder result = new StringBuilder(msg);
		int len = getWidth() - getPadding() - result.length();
		if (len > 0) {
			for (int i = 0; i < len; i++) {
				result.append(" ");
			}
		}
		return result.toString();
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
			_gameMessage = game.getGameMessage();
			refreshEntry();
			refreshScreen();
		}
	}

}
