/**
 * 
 */
package screen;

import com.googlecode.lanterna.terminal.TerminalPosition;

/**
 * 
 * This contains the word to be guessed, the hangman internal representation and
 * progress.
 * 
 * @author gbonnema
 * 
 */
public class Hangman {

	private String hideWord;
	private char[] guessCharArray;
	private int phase;
	private TerminalPosition[] hangman;

	public Hangman(String word) {
		hideWord = word;
		createHangman();
		int nrCharsToGuess = hangman.length;
		guessCharArray = new char[nrCharsToGuess];
	}

	private void createHangman() {

	}

	public int getHangmanSize() {
		return 7;
	}

	private static class HangmanPart {

		private TerminalPosition pos;
		private char ch;

		private static HangmanPart hangman1;

		{
			hangman1 = new HangmanPart();
		}

		/* Prevent instantiation */
		private HangmanPart() {
		}
	}

}
