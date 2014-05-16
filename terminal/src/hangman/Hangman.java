/**
 * 
 */
package hangman;

import java.util.ArrayList;

import util.ExperimentException;
import util.Utils;

/**
 * 
 * This class contains the word to be guessed, the hangman internal
 * representation and progress. The class provides the facility to guess the
 * characters in the word and show what was already correctly guessed. ALso, it
 * provides a hangman figure to draw.
 * 
 * @author gbonnema
 * 
 */
public class Hangman {

	private String _hideWord;
	private ArrayList<Character> _correctlyGuessedCharArray;
	private ArrayList<Character> _initialGuessCharArray;
	private int _phase;

	private HangmanDoc _doc = new HangmanDoc();

	private HangFig _hangFig;

	private enum Status {
		GUESSING, WORD_GUESSED, WORD_NOT_GUESSED
	};

	private Status _status;

	/**
	 * Constructor for hangman with a specified word.
	 * 
	 * @param word
	 *          the specified word
	 * @throws ExperimentException
	 *           if the word is empty
	 */
	public Hangman(String word) {
		Utils.checkArg(word.length() > 0, "Empty word in hangman");
		_phase = 0;
		_status = Status.GUESSING;
		_hideWord = word;
		_hangFig = new HangFig();
		_correctlyGuessedCharArray = new ArrayList<>();
		_initialGuessCharArray = new ArrayList<>();
		for (int i = 0; i < _hideWord.length(); i++) {
			_correctlyGuessedCharArray.add('_');
			_initialGuessCharArray.add('_');
		}
	}

	/**
	 * This method returns a character array containing the character in the
	 * positions that it occurs or null if none is found.
	 * 
	 * @param charStr
	 *          the specified character
	 * @return an array containing the positions of the hidden word with the
	 *         specified character where it is in the word and underscore where it
	 *         is not. If the character was not found, the initial array is
	 *         returned where every character has an underscore. if the phase has
	 *         ended, then null is returned in stead.
	 */
	public ArrayList<Character> guess(String charStr) {
		/* Sanity input check */
		boolean charStrIsOne = charStr.length() == 1;
		Utils.checkArg(charStrIsOne, "Error: charStr should be 1 character.");

		ArrayList<Character> resultingHits = null;
		if (_status == Status.GUESSING) {
			char ch = charStr.charAt(0);
			resultingHits = createHitList(ch);
			if (guessedWord()) {
				_status = Status.WORD_GUESSED;
			}
			updateGuessedWord(resultingHits);

		}
		return resultingHits;
	}

	/**
	 * @return
	 */
	private boolean guessedWord() {
		boolean emptyCharFound = false;
		for (int i = 0; i < _correctlyGuessedCharArray.size(); i++) {
			if (_correctlyGuessedCharArray.get(i) == '_') {
				emptyCharFound = true;
				break;
			}
		}
		return !emptyCharFound;
	}

	/**
	 * 
	 * Create a hit list for this character.
	 * 
	 * @param ch
	 *          The character being guessed.
	 * 
	 * @return the character list. Any position equals to char has that char. The
	 *         other positions have an underscore.
	 */
	private ArrayList<Character> createHitList(char ch) {
		ArrayList<Character> result;
		result = new ArrayList<>(_initialGuessCharArray);
		for (int i = 0; i < _hideWord.length(); i++) {
			if (_hideWord.charAt(i) == ch) {
				result.set(i, ch);
			}
		}
		return result;
	}

	/**
	 * This method unites the result from guess() with the guessed characters up
	 * until this point.
	 * 
	 * @param chArr
	 *          The guess array returned from guess();
	 * @return the guess character array
	 */
	public boolean guessRight(ArrayList<Character> chArr) {
		boolean result = false;
		// Check if previous result was null
		if (chArr == null) {
			return result;
		}
		if (wrongGuess(chArr)) {
			_phase++;
		}
		if (executedPhase()) {
			_status = Status.WORD_NOT_GUESSED;
		}
		updateGuessedWord(chArr);
		return true;
	}

	/**
	 * @return
	 */
	private boolean executedPhase() {
		return _phase >= HangFig.PHASEMAX - 1;
	}

	/**
	 * @param chArr
	 * @return
	 */
	private boolean wrongGuess(ArrayList<Character> chArr) {
		return chArr.equals(_initialGuessCharArray);
	}

	/**
	 * @param chArr
	 */
	private void updateGuessedWord(ArrayList<Character> chArr) {
		// update a right guess
		char empty = '_';
		for (int i = 0; i < _hideWord.length(); i++) {
			if (chArr.get(i) == empty) {
				continue;
			}
			_correctlyGuessedCharArray.set(i, chArr.get(i));
		}
	}

	public ArrayList<Character> refreshGuess() {
		return _correctlyGuessedCharArray;
	}

	/**
	 * Return the figure for the current phase.
	 * 
	 * @return
	 */
	public ArrayList<String> getFigure() {
		return _hangFig.getFigure(_phase);
	}

	/**
	 * 
	 * @return the array containing correctly guessed characters.
	 */
	public ArrayList<Character> getCorrectlyGuessedArray() {
		return _correctlyGuessedCharArray;
	}

	/**
	 * @return the _initialGuessCharArray
	 */
	public ArrayList<Character> getInitialGuessCharArray() {
		return _initialGuessCharArray;
	}

	/**
	 * Return the long description for this game.
	 * 
	 * @return the long description for this game.
	 */
	public String getLongDescription() {
		return _doc.getLongDescription();
	}

	/**
	 * Return the short description for this game.
	 * 
	 * @return the short description for this game.
	 */
	public String getShortDescription() {
		return _doc.getShortDescription();
	}

	/**
	 * 
	 * @return true if we are still guessing.
	 */
	public boolean isGuessing() {
		return _status == Status.GUESSING;
	}

	/**
	 * 
	 * @return true if the word was guessed right.
	 */
	public boolean wonGuess() {
		return _status == Status.WORD_GUESSED;
	}

	public boolean lostGuess() {
		return _status == Status.WORD_NOT_GUESSED;
	}

	public Status getStatus() {
		return _status;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("word = ");
		result.append(_hideWord);
		result.append(", guessed = ");
		result.append(_correctlyGuessedCharArray);
		result.append(", _phase = ");
		result.append(_phase);
		return result.toString();
	}
}
