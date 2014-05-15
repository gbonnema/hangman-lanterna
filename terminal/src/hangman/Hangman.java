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

	private String								_hideWord;
	private ArrayList<Character>	_correctlyGuessedCharArray;
	private ArrayList<Character>	_initialGuessCharArray;
	private int										_phase;

	private HangmanDoc						_doc	= new HangmanDoc();

	private HangFig								_hangFig;

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
	 * Return the length of the hangman figure
	 * 
	 * @return
	 */
	public int getHangFigSize() {
		return HangFig.HANGMAN_FIG_HEIGHT;
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
		Utils.checkArg(charStr.length() == 1,
				"Error: charStr should be 1 character.");
		if (_phase > HangFig.PHASEMAX) {
			return null;
		}
		char ch = charStr.charAt(0);
		ArrayList<Character> result = new ArrayList<>(_initialGuessCharArray);
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
	public boolean updateGuessOk(ArrayList<Character> chArr) {
		// Check if previous result was null
		if (chArr == null) {
			return false;
		}
		// Update a wrong guess
		if (chArr.equals(_initialGuessCharArray)) {
			_phase++;
			return false;
		}
		// update a right guess
		char empty = '_';
		for (int i = 0; i < _hideWord.length(); i++) {
			if (chArr.get(i) == empty) {
				continue;
			}
			_correctlyGuessedCharArray.set(i, chArr.get(i));
		}
		return true;
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
