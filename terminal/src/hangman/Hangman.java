/**
 * 
 */
package hangman;

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

	private String hideWord;
	private char[] guessCharArray;
	private char[] initialGuessCharArray;
	private int phase;

	private HangmanDoc doc = new HangmanDoc();

	/**
	 * Constructor for hangman with a specified word.
	 * 
	 * @param word
	 *            the specified word
	 * @throws ExperimentException
	 *             if the word is empty
	 */
	public Hangman(String word) throws ExperimentException {
		Utils.check(word.length() > 0, "Empty word in hangman");
		phase = 0;
		hideWord = word;
		createHangman();
		int nrCharsToGuess = hideWord.length();
		guessCharArray = new char[nrCharsToGuess];
		initialGuessCharArray = new char[nrCharsToGuess];
		for (int i = 0; i < nrCharsToGuess; i++) {
			guessCharArray[i] = '_';
			initialGuessCharArray[i] = '_';
		}
	}

	/* TODO What should I do here? */
	private void createHangman() {
		// to do
	}

	/**
	 * Return the length of the hangman figure
	 * 
	 * @return
	 */
	public int getHangFigSize() {
		return HangFig.HANGMAN_FIG_LEN;
	}

	/**
	 * This method returns a character array containing the character in the
	 * positions that it occurs or null if none is found.
	 * 
	 * @param charStr
	 *            the specified character
	 * @return an array containing the positions of the hidden word with the
	 *         specified character where it is in the word and space where it is
	 *         not. If the character was not found, null is returned.
	 */
	public char[] guess(String charStr) throws ExperimentException {
		Utils.check(charStr.length() == 1,
				"Error: charStr should be 1 character.");
		char ch = charStr.charAt(0);
		char[] result = new char[hideWord.length()];
		for (int i = 0; i < hideWord.length(); i++) {
			if (hideWord.charAt(i) == ch) {
				result[i] = ch;
			} else {
				result[i] = initialGuessCharArray[i];
			}
		}
		return result;
	}

	/**
	 * This methode unites the result from guess() with the guessed characters
	 * up until this point.
	 * 
	 * @param chArr
	 *            The guess array returned from guess();
	 * @return the guess character array
	 */
	public char[] updateGuess(char[] chArr) {
		// Update a wrong guess
		if (chArr == null) {
			phase++;
			return guessCharArray;
		}
		// update a right guess
		char empty = '_';
		for (int i = 0; i < hideWord.length(); i++) {
			if (chArr[i] == empty) {
				continue;
			}
			guessCharArray[i] = chArr[i];
		}
		return guessCharArray;
	}

	/**
	 * @return the partially filled hangman figure depending on the phase we are
	 *         in.
	 * @throws ExperimentException
	 *             if the phase is invalid (internal error). This should never
	 *             happen.
	 */
	public HangFig[] getHangFig() throws ExperimentException {
		// Sanity check
		Utils.check(phase <= HangFig.HANGMAN_FIG_LEN,
				"Internal error: Invalid phase");

		HangFig[] partialFig = new HangFig[phase];
		for (int i = 0; i < phase; i++) {
			partialFig[i] = HangFig.HANG_PART_ARR[i];
		}
		return partialFig;
	}

	/**
	 * @return the initialGuessCharArray
	 */
	public char[] getInitialGuessCharArray() {
		return initialGuessCharArray;
	}

	/**
	 * Return the long description for this game.
	 * 
	 * @return the long description for this game.
	 */
	public String getLongDescription() {
		return doc.getLongDescription();
	}

	/**
	 * Return the short description for this game.
	 * 
	 * @return the short description for this game.
	 */
	public String getShortDescription() {
		return doc.getShortDescription();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("word = ");
		result.append(hideWord);
		result.append(", guessed = ");
		result.append(guessCharArray);
		result.append(", phase = ");
		result.append(phase);
		return result.toString();
	}
}
