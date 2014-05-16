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

/*
 * TODO The dashes of gamepanel clash with the dashes of solution. TODO Right
 * after guessing The message says "next guess" wrongly
 */

/**
 * This is the controller for the game. It understands what hangman is for and
 * follows the phases toward execution or savior. It finds a word in the
 * vocabulary and uses the English variant to start a hangman game with that
 * word. The player can then create a new guess with
 * <code>guess(character)</code>. It recalculates the guess array using this
 * character and forwards the execution if the guess is wrong.
 * 
 * @author gbonnema
 * 
 */
public class HangmanGame extends Observable {

	private final String _startMsg = "Guess a character";
	private final String _startMsgDup =
			"You already guessed that. Guess another character";
	private final String _contMsgWrong = "Wrong. Guess another character";
	private final String _contMsgRight = "Guess another character";
	private final String _endMsgLost = "You Lost. Innocent hangs.";
	private final String _endMsgWon = "You saved the innocent: hero!";
	/*******************************************************************/

	private Vocab _vocab;
	private Hangman _hangman;
	private ArrayList<Character> _guessArray;
	private ArrayList<Character> _guessedArray;
	private String _gameMessage;

	private VocabEntry _entry;

	public HangmanGame() throws ExperimentException {
		try {
			_vocab = new Vocab("nederlands.csv");
		} catch (IOException e) {
			throw new ExperimentException(
					"File not found or not readable. Error message: " + e.getMessage());
		}
		_entry = _vocab.new VocabEntry(0, "not a word", "not a word");
		_gameMessage = _startMsg;
	}

	public void createGame() {
		_entry = _vocab.getRandomEntry();
		_hangman = new Hangman(_entry._wordNL);
		_guessArray = _hangman.refreshGuess();
		_guessedArray = new ArrayList<>();
		_gameMessage = _startMsg;
		setChanged();
		notifyObservers();
	}

	/**
	 * Performs the guess. Also notifies all observers of a change. If the guess
	 * was entered before the game message will say so. Other wise the word will
	 * be checked for the character.
	 * 
	 * @param charStr
	 */
	public void guess(String charStr) {
		Utils.checkInternal(_hangman != null, "Internal: _hangman == null.");
		updateChar(charStr);
	}

	/*
	 * Updates the character with a guess in hangman.
	 */
	private void updateChar(String charStr) {
		Character ch = charStr.charAt(0);
		// Check finished
		if (!_hangman.isGuessing()) {
			updateHangmanResponse(null);
			return;
		}
		// We are still guessing
		// check duplicate
		if (_guessedArray.contains(ch)) {
			_gameMessage = _startMsgDup;
			return;
		}
		// Ask: is it in the word?
		_guessedArray.add(ch);
		ArrayList<Character> chArr = _hangman.guess(charStr);
		_gameMessage = updateHangmanResponse(chArr);
		/* Send notify to observers */
		setChanged();
		notifyObservers();
	}

	/**
	 * Check hangman's response.
	 * 
	 * @param chArr
	 */
	private String updateHangmanResponse(ArrayList<Character> chArr) {

		String response;
		response = _hangman.guessRight(chArr) ? _contMsgRight : _contMsgWrong;
		if (!_hangman.isGuessing()) {
			response = _hangman.wonGuess() ? _endMsgWon : _endMsgLost;
		}
		return response;
	}

	/**
	 * 
	 * @return the array with characters guessed at the right position.
	 */
	public ArrayList<Character> getWordProgressArray() {
		ArrayList<Character> result = new ArrayList<>();
		for (char ch : _guessArray) {
			result.add(ch);
		}
		return result;
	}

	public ArrayList<String> getFigure() {
		ArrayList<String> result = _hangman.getFigure();
		return result;
	}

	public String getSolution() {
		return _entry._wordNL;
	}

	/**
	 * 
	 * @return the game message.
	 */
	public String getGameMessage() {
		return _gameMessage;
	}

	public ArrayList<Character> getGuessedCharacters() {
		ArrayList<Character> result = new ArrayList<>();
		for (char ch : _guessedArray) {
			result.add(ch);
		}
		return result;
	}

}