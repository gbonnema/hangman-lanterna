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

	private Vocab									_vocab;
	private Hangman								_hangman;
	private ArrayList<Character>	_guessArray;
	private ArrayList<Character>	_guessedArray;
	private String								_gameMessage;

	private VocabEntry						_entry;

	public HangmanGame() throws ExperimentException {
		try {
			_vocab = new Vocab("nederlands.csv");
		} catch (IOException e) {
			throw new ExperimentException(
					"File not found or not readable. Error message: " + e.getMessage());
		}
		_entry = _vocab.new VocabEntry(0, "not a word", "not a word");
	}

	public void createGame() {
		_entry = _vocab.getRandomEntry();
		_hangman = new Hangman(_entry._wordNL);
		_guessArray = _hangman.refreshGuess();
		_guessedArray = new ArrayList<>();
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
		/* Send notify to observers */
		setChanged();
		notifyObservers();
	}

	/*
	 * Updates the character with a guess in hangman.
	 */
	private void updateChar(String charStr) {
		Character ch = charStr.charAt(0);
		ArrayList<Character> chArr = _hangman.guess(charStr);
		if (chArr == null) {
			_gameMessage = "Game is finished, start a new game.";
		} else {
			/* update and process response */
			if (_hangman.updateGuessOk(chArr)) {
				_guessedArray.add(ch);
				_gameMessage = "Enter the next character";
			} else {
				_gameMessage =
						"Character " + ch + " already guessed. THE INNOCENT WILL HANG!";
			}
		}
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