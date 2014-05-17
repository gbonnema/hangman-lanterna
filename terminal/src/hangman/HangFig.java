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

import util.Utils;

/**
 * A helper class for the hangman figure. For text contains numbers.
 * 
 * @author gbonnema
 * 
 */
public class HangFig {

	private ArrayList<ArrayList<String>> _figureList;
	private ArrayList<String> _lostFigure;
	private ArrayList<String> _wonFigure;

	/* @formatter:off */
	private static final String[]	won	= {
		"                                    ", 
		"          Y O U                     ", 
		"                                    ", 
		"        S A V E D     T H E         ", 
		"                                    ", 
		"     I N N O C E N T ! ! ! !        ", 
		};
	/* @formatter:on */

	/* @formatter:off */
	private static final String[]	lost	= {
		"                                    ", 
		"                                    ", 
		"          Y O U   L O S T ! ! !     ", 
		"                                    ", 
		"                                    ", 
		"                                    ", 
		};
	/* @formatter:on */

	/* @formatter:off */
	private static final String[]	F0	= {
		"                                    ", 
		"                                    ", 
		"                                    ", 
		"                                    ", 
		"                                    ", 
		"                                    ", 
		};
	/* @formatter:on */

	public static final int PHASEMAX = 6;
	public static final int HANGMAN_FIG_HEIGHT = F0.length;
	public static final int HANGMAN_FIG_WIDTH = F0[0].length();

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
		_lostFigure = convertArray(lost);
		_wonFigure = convertArray(won);
	}

	public ArrayList<String> getFigure(int phase) {
		Utils.checkArg(phase >= 0 && phase < PHASEMAX, "Phase has illegal value: "
				+ phase);
		return _figureList.get(phase);
	}

	public ArrayList<String> getFigureWon() {
		return _wonFigure;
	}

	public ArrayList<String> getFigureLost() {
		return _lostFigure;
	}

	private ArrayList<String> convertArray(String[] figure) {
		ArrayList<String> lineList = new ArrayList<>();
		for (String line : figure) {
			lineList.add(line);
		}
		return lineList;
	}

}