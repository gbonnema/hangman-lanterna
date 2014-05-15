/**
 * 
 */
package hangman;

import java.util.ArrayList;

import util.Utils;

/**
 * A helper class for the hangman figure. For text contains numbers.
 * 
 * @author gbonnema
 * 
 */
public class HangFig {

	private ArrayList<ArrayList<String>>	_figureList;

	/* @formatter:off */
	private static final String[]	F0	= {
		"                                  ", 
		"                                  ", 
		"                                  ", 
		"                                  ", 
		"                                  ", 
		"                                  ", 
		};
	/* @formatter:on */

	public static final int								PHASEMAX						= F0.length;
	public static final int								HANGMAN_FIG_HEIGHT	= F0.length;
	public static final int								HANGMAN_FIG_WIDTH		= F0[0].length();

	/* @formatter:off */
	private static final String[]	F1= {
		"                                    ", 
		"                                    ", 
		"                                    ", 
		"                                    ", 
		"                                    ", 
		"         --------                   ", 
		};
	/* @formatter:on */
	/* @formatter:off */
	private static final String[]	F2= {
		"                                    ", 
		"                                    ", 
		"                                    ", 
		"            |                       ", 
		"            |                       ", 
		"         --------                   ", 
		};
	/* @formatter:on */
	/* @formatter:off */
	private static final String[]	F3= {
		"                                    ", 
		"            |                       ", 
		"            |                       ", 
		"            |                       ", 
		"            |                       ", 
		"         --------                   ", 
		};
	/* @formatter:on */
	/* @formatter:off */
	private static final String[]	F4= {
		"            ------                  ", 
		"            |                       ", 
		"            |                       ", 
		"            |                       ", 
		"            |                       ", 
		"         --------                   ", 
		};
	/* @formatter:on */
	/* @formatter:off */
	private static final String[]	F5	= {
		"            ------                  ", 
		"            |    |                  ", 
		"            |    x   D E A D        ", 
		"            |                       ", 
		"            |        E X E C U T E D", 
		"         --------                   ", 
		};
	/* @formatter:on */

	/* Prevent instantiation */
	public HangFig() {
		_figureList = new ArrayList<>();
		_figureList.add(convertArray(F0));
		_figureList.add(convertArray(F1));
		_figureList.add(convertArray(F2));
		_figureList.add(convertArray(F3));
		_figureList.add(convertArray(F4));
		_figureList.add(convertArray(F5));
	}

	public ArrayList<String> getFigure(int phase) {
		Utils.checkArg(phase >= 0 && phase < PHASEMAX, "Phase has illegal value: "
				+ phase);
		return _figureList.get(phase);
	}

	private ArrayList<String> convertArray(String[] figure) {
		ArrayList<String> lineList = new ArrayList<>();
		for (String line : figure) {
			lineList.add(line);
		}
		return lineList;
	}

}