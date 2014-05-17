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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * This class documents the game Hangman.
 * 
 * @author gbonnema
 * 
 */
public class HangmanDoc {

	private final String longDescrFilename = "src/hangman/hangman.txt";

	private String shortDescription =
			"Hangman. The game to save a man from the gallows by guessing"
					+ "a word before he gets executed.";

	private String longDescription;

	/**
	 * Default constructor for HangmanDoc.
	 */
	public HangmanDoc() {
		/* Read the file with descriptions */
		StringBuilder result = new StringBuilder();
		try {
			FileInputStream fin = new FileInputStream(longDescrFilename);
			Scanner in = new Scanner(fin, "UTF-8");
			String line;
			while (in.hasNext()) {
				line = in.nextLine();
				result.append(line).append("\n");
			}
			in.close();
		} catch (IOException ioe) {
			result.append("Hangman description file not found or not readable.");
		} finally {
			longDescription = result.toString();
		}
	}

	/**
	 * 
	 * @return the short description for this game.
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * 
	 * @return the long description for this game.
	 */
	public String getLongDescription() {
		return longDescription;
	}

}
