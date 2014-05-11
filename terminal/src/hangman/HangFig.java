/**
 * 
 */
package hangman;

import com.googlecode.lanterna.terminal.TerminalPosition;

/**
 * A helper class for the hangman figure. For text contains numbers.
 * 
 * @author gbonnema
 * 
 */
public class HangFig {

	private TerminalPosition pos;
	private char ch;
	/**
	 * The length of the hangman figure.
	 */
	public static final int HANGMAN_FIG_LEN = 6;
	/**
	 * The hangman figure in an array.
	 */
	public static final HangFig[] HANG_PART_ARR = new HangFig[HANGMAN_FIG_LEN];

	/**
	 * Static initialization of the hangman figure.
	 */
	{
		// Fill the TerminalPositions relative to origin of hangman
		// For now a number counting down.
		for (int offset = 0; offset < HANGMAN_FIG_LEN; offset++) {
			TerminalPosition p = new TerminalPosition(0, offset + 1);
			char c = Integer.toString(HANGMAN_FIG_LEN - offset).charAt(0);
			HANG_PART_ARR[offset] = new HangFig(p, c);
		}

	}

	/* Prevent instantiation */
	private HangFig(TerminalPosition pos, char ch) {
		this.setPos(pos);
		this.setCh(ch);
	}

	/**
	 * @return the pos
	 */
	public TerminalPosition getPos() {
		return pos;
	}

	/**
	 * @param pos
	 *            the pos to set
	 */
	public void setPos(TerminalPosition pos) {
		this.pos = pos;
	}

	/**
	 * @return the ch
	 */
	public char getCh() {
		return ch;
	}

	/**
	 * @param ch
	 *            the ch to set
	 */
	public void setCh(char ch) {
		this.ch = ch;
	}

}