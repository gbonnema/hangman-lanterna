/**
 * Copyright 2014 Guus Bonnema, Dieren, The Netherlands.
 * 
 * This file is part of hangman-lanterna.
 * 
 * hangman-lanterna is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * hangman-lanterna is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * hangman-lanterna. If not, see <http://www.gnu.org/licenses/>.
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
	private ArrayList<Guess> _guessHistory;
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
		_guessHistory = new ArrayList<>();
		for (int i = 0; i < _hideWord.length(); i++) {
			_correctlyGuessedCharArray.add('_');
			_initialGuessCharArray.add('_');
		}
	}

	/**
	 * This method updates the guess into the guessed characters array, and
	 * determines whether the prisoner was saved or executed or awaiting
	 * execution. If you offer the same character multiple times, the effect
	 * should be the same every time, until the word is guessed. After that
	 * nothing more registers.
	 * 
	 * @param charStr
	 *          the specified character given as a String.
	 */
	public void guess(String charStr) {
		/* Sanity input check */
		boolean charStrIsOne = charStr.length() == 1;
		Utils.checkArg(charStrIsOne, "Error: charStr should be 1 character.");

		char ch = charStr.charAt(0);
		if (isDupGuess(ch)) {
			return;
		}

		boolean inWord = false;
		ArrayList<Character> resultingHits = null;
		if (_status == Status.GUESSING) {
			resultingHits = createHitList(ch);
			updateGuessedWord(resultingHits);
			if (guessedWord()) {
				_status = Status.WORD_GUESSED;
			} else {
				if (wrongGuess(resultingHits)) {
					inWord = false;
					_phase++;
				} else {
					inWord = true;
				}
				if (executionPhase()) {
					_status = Status.WORD_NOT_GUESSED;
				}
			}
			_guessHistory.add(new Guess(ch, inWord, resultingHits));
		}
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

	private boolean isDupGuess(char ch) {
		boolean found = false;
		for (Guess guess : _guessHistory) {
			if (guess._ch == ch) {
				found = true;
				break;
			}
		}
		return found;
	}

	/**
	 * @return true if the phase past execution phase
	 */
	private boolean executionPhase() {
		return _phase >= HangFig.PHASEMAX - 1;
	}

	/**
	 * @param chArr
	 * @return true if the guessed character is not in the word.
	 */
	private boolean wrongGuess(ArrayList<Character> chArr) {
		return chArr.equals(_initialGuessCharArray);
	}

	/**
	 * @param chArr
	 *          a hit list (array) of characters that the guess was a hit.
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
		if (isFinished()) {
			if (wonGuess()) {
				return _hangFig.getFigureWon();
			} else {
				return _hangFig.getFigureLost();
			}
		}
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
	 * Retrieves and returns the last Guess.
	 * 
	 * Remark: Duplicate guesses should not be in here.
	 * 
	 * @return the last Guess that was submitted and processed.
	 */
	public Guess getLastGuess() {
		int last = _guessHistory.size() - 1;
		if (last < 0) {
			return null;
		}
		return _guessHistory.get(last);
	}

	/**
	 * 
	 * @return true if we are still guessing.
	 */
	public boolean isGuessing() {
		return _status == Status.GUESSING;
	}

	public boolean isFinished() {
		return !isGuessing();
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

	/**
	 * A helper class to clump together some guess data.
	 * 
	 * @author gbonnema
	 * 
	 */
	public class Guess {
		private char _ch;
		private boolean _inWord;
		private ArrayList<Character> _hitList;

		public Guess(char ch, boolean inWord, ArrayList<Character> hitList) {
			_ch = ch;
			_inWord = inWord;
			_hitList = hitList;
		}

		public boolean contains(char ch) {
			return ch == _ch;
		}

		public char getChar() {
			return _ch;
		}

		public boolean isInWord() {
			return _inWord;
		}

		public ArrayList<Character> getHitList() {
			return _hitList;
		}
	}
}
