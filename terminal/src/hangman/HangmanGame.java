/**
 * 
 */
package hangman;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

import util.ExperimentException;
import util.Utils;
import vocabulary.Vocab;
import vocabulary.Vocab.VocabEntry;

/**
 * @author gbonnema
 * 
 */
public class HangmanGame extends Observable {

	private Vocab				_vocab;
	private Hangman			_hangman;
	private char[]			_guessArray;

	private VocabEntry	_entry;

	public HangmanGame() throws ExperimentException {
		try {
			_vocab = new Vocab("nederlands.csv");
		} catch (IOException e) {
			throw new ExperimentException(
					"File not found or not readable. Error message: " + e.getMessage());
		}
		_entry = _vocab.new VocabEntry(0, "not a word", "not a word");
	}

	public void newGame() {
		_entry = _vocab.getRandomEntry();
		_hangman = new Hangman(_entry._wordNL);
		_guessArray = _hangman.refreshGuess();
		setChanged();
		notifyObservers();
	}

	public void newGuess(String charStr) {
		Utils.checkArg(_hangman != null, "Not gaming, yet.");
		char[] chArr = _hangman.guess(charStr);
		chArr = _hangman.updateGuess(chArr);
		setChanged();
		notifyObservers();
	}

	public ArrayList<Character> getGuessedArray() {
		ArrayList<Character> result = new ArrayList<>();
		for (char ch : _guessArray) {
			result.add(ch);
		}
		return result;
	}

}
