/**
 * 
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
