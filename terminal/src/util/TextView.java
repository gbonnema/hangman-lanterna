/**
 * 
 */
package util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gbonnema
 * 
 */
public class TextView {

	private String text;
	private ArrayList<String> formattedLines;
	private int curline;
	private int pageSize;
	private int nrLines;

	public TextView() {
		curline = -1;
	}

	public void formatPage(String text_, int pageSize_, int pageWidth_) {
		text = text_;
		pageSize = pageSize_;
		String[] lines = text.split("\n");
		String words = String.join(" ", lines);
		String[] wordArray = words.split(" ");
		formattedLines = formatLines(pageWidth_, wordArray);
		curline = 0;
	}

	/**
	 * Retrieve a page of text starting at registered current line.
	 * 
	 * @return List<String> containing the requested lines. The list is a copy.
	 */
	public List<String> page() {
		int maxlen = Math.min(pageSize, nrLines - curline + 1);
		/* create a sublist */
		List<String> lines = formattedLines.subList(curline, curline + maxlen);
		/* The sublist is an active view, not a copy, so copy it */
		ArrayList<String> result = new ArrayList<>(lines);
		curline = curline + maxlen;
		return result;
	}

	/**
	 * A static method to format an array of words into a list of lines of
	 * specified width (or a little shorter).
	 * 
	 * @param width_
	 *            the length of the lines (width on the screen).
	 * @param wordArray_
	 *            the array of words.
	 * @return the ArrayList<String> of lines.
	 */
	public static ArrayList<String>
			formatLines(int width_, String[] wordArray_) {
		ArrayList<String> lines = new ArrayList<>();
		StringBuilder line = new StringBuilder();
		String glue = "";
		for (int i = 0; i < wordArray_.length; i++) {
			/* Break on a new line if next word is too large */
			if (line.length() + wordArray_[i].length() > width_) {
				lines.add(line.toString());
				line = new StringBuilder();
			}
			/* Add the word to the line */
			line.append(glue);
			line.append(wordArray_[i]);
			glue = " ";
		}
		return lines;
	}

}
