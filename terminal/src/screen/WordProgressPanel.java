/**
 * 
 */
package screen;

import hangman.HangmanGame;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

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

	private ArrayList<Character>	_guessArray;

	/**
	 * @param mainScreen
	 */
	public WordProgressPanel(TextDraw mainScreen) {
		super(mainScreen);
	}

	private void refreshEntry() {
		TerminalPosition coord = new TerminalPosition(getPadding(), getPadding());
		String spacedGuessArray = disperse(_guessArray);
		drawString(coord.getColumn(), coord.getRow(), spacedGuessArray);
	}

	private String disperse(ArrayList<Character> chArr) {
		StringBuilder result = new StringBuilder();
		result.append(chArr.get(0));
		for (int i = 1; i < chArr.size(); i++) {
			result.append(" ").append(chArr.get(i));
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
			_guessArray = game.getWordProgressArray();
			refreshEntry();
			refreshScreen();
		}
	}

}
