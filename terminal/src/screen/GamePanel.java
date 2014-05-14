/**
 * 
 */
package screen;

import hangman.HangFig;
import hangman.Hangman;

import java.io.IOException;

import util.ExperimentException;
import util.Utils;
import vocabulary.Vocab;
import vocabulary.Vocab.VocabEntry;

import com.googlecode.lanterna.terminal.TerminalPosition;

/**
 * @author gbonnema
 * 
 */
public class GamePanel extends AbstractPanel {

	private HangFig[]		_hangFig;

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
		_hangFig = _hangman.getHangFig();
		_guessArray = _hangman.refreshGuess();
		refreshEntry();
	}

	public char[] newGuess(String charStr) {
		Utils.checkArg(_hangman != null, "Not gaming, yet.");
		char[] chArr = _hangman.guess(charStr);
		chArr = _hangman.updateGuess(chArr);
		return chArr;
	}

	private void refreshEntry() {
		TerminalPosition coord = new TerminalPosition(0, 0);
		String entryStr = new String(_guessArray);
		drawString(coord.getColumn(), coord.getRow(), entryStr);
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

}
