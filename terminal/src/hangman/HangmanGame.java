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

import hangman.Hangman.Guess;

import java.util.ArrayList;
import java.util.Observable;

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

	/******** Game messages ********************************************/
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
	// private ArrayList<Character> _guessedArray;
	private String _gameMessage;

	private VocabEntry _entry;

	public HangmanGame() {
		String emsg = "";
		try {
			_vocab = new Vocab("nederlands.csv");
		} catch (Exception e) {
			emsg = "File not found or not readable. Error message: " + e.getMessage();
			_vocab = null;
		}
		Utils.checkInternal(_vocab != null, emsg);
		_entry = _vocab.new VocabEntry(0, "not a word", "not a word");
		_gameMessage = _startMsg;
	}

	public void createGame() {
		_entry = _vocab.getRandomEntry();
		_hangman = new Hangman(_entry._wordNL);
		_guessArray = _hangman.refreshGuess();
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
	 *          the string containing exactly one character. Throws a runtime
	 *          exception if it contains more or less than one character.
	 */
	public void guess(String charStr) {
		Utils.checkInternal(_hangman != null, "Internal: _hangman == null.");

		Character ch = charStr.charAt(0);
		// Are we still guessing?
		if (_hangman.isFinished()) {
			_gameMessage = _hangman.wonGuess() ? _endMsgWon : _endMsgLost;
		} else {
			// We are still guessing : check for duplicate
			if (wasCharGuessed(ch)) {
				_gameMessage = _startMsgDup;
				return;
			}

			_hangman.guess(charStr);

			if (_hangman.isGuessing()) {
				Guess guess = _hangman.getLastGuess();
				_gameMessage = guess.isInWord() ? _contMsgRight : _contMsgWrong;
			} else {
				_gameMessage = _hangman.wonGuess() ? _endMsgWon : _endMsgLost;
			}
		}

		setChanged();
		notifyObservers();
	}

	/**
	 * @param ch
	 *          the character to be checked.
	 * @return true if the character was guessed earlier.
	 */
	private boolean wasCharGuessed(Character ch) {
		Guess lastGuess = _hangman.getLastGuess();
		return lastGuess != null && lastGuess.contains(ch);
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

}