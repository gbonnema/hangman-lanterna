/**
 * 
 */
package screen;

import hangman.Hangman;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import util.ExperimentException;
import util.Utils;
import vocabulary.Vocab;
import vocabulary.Vocab.VocabEntry;

import com.googlecode.lanterna.terminal.TerminalPosition;

/**
 * @author gbonnema
 * 
 */
public class GamePanel extends AbstractPanel implements Observer {

	private Vocab				_vocab;
	private Hangman			_hangman;
	private char[]			_guessArray;

	private VocabEntry	_entry;

	/**
	 * @param mainScreen
	 */
	public GamePanel(TextDraw mainScreen) throws ExperimentException {
		super(mainScreen);
		// Catch the IOException and transform to an ExperimentException.
		try {
			_vocab = new Vocab("nederlands.csv");
		} catch (IOException e) {
			throw new ExperimentException(
					"File not found or not readable. Error message: " + e.getMessage());
		}
		_entry = _vocab.new VocabEntry(0, "", "");
	}

	public void newGame() {
		_entry = _vocab.getRandomEntry();
		_hangman = new Hangman(_entry._wordNL);
		_guessArray = _hangman.refreshGuess();
		refreshEntry();
	}

	public void newGuess(String charStr) {
		Utils.checkArg(_hangman != null, "Not gaming, yet.");
		char[] chArr = _hangman.guess(charStr);
		chArr = _hangman.updateGuess(chArr);
		refreshEntry();
	}

	private void refreshEntry() {
		TerminalPosition coord = new TerminalPosition(getPadding(), getPadding());
		String spacedGuessArray = disperse(_guessArray);
		drawString(coord.getColumn(), coord.getRow(), spacedGuessArray);
	}

	private String disperse(char[] chArr) {
		StringBuilder result = new StringBuilder();
		result.append(chArr[0]);
		for (int i = 1; i < chArr.length; i++) {
			result.append(" ").append(chArr[i]);
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
		// TODO Auto-generated method stub

	}

}
