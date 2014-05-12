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

	private static enum Stage {
		COPY, FLOW
	};

	private String						text;
	private ArrayList<String>	formattedLines;
	private int								curline;
	private int								pageSize;
	private int								nrLines;

	public TextView() {
		curline = -1;
		formattedLines = new ArrayList<>();
	}

	public void formatPage(String text_, int pageSize_, int pageWidth_) {
		text = text_;
		pageSize = pageSize_;
		formattedLines = formatLines(text, pageWidth_);
		nrLines = formattedLines.size();
		curline = 0;
	}

	/**
	 * Retrieve a page of text starting at registered current line.
	 * 
	 * @return List<String> containing the requested lines. The list is a copy.
	 */
	public List<String> nextPage() {
		if (curline < 0) {
			return new ArrayList<>();
		}
		int maxlen = Math.min(pageSize, nrLines - curline);
		maxlen = Math.max(maxlen, 0);
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
	 * @param text_
	 *          the specified text (all lines).
	 * @param width_
	 *          the desired maximum length of each line of text.
	 * @return the ArrayList<String> of lines.
	 */
	public ArrayList<String> formatLines(String text_, int width_) {
		Stage stage;
		int width = width_;
		String[] lines = text_.split("\n"); // skips an empty line at the end
		ArrayList<String> formatted = new ArrayList<>();
		StringBuilder para = new StringBuilder();
		stage = Stage.COPY;
		String glue = "";
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			stage = stageLine(line, width, stage);
			if (stage == Stage.COPY) {
				if (para.length() > 0) {
					List<String> paraLines = flow(para, width);
					formatted.addAll(paraLines);
					para = new StringBuilder();
					glue = "";
				}
				formatted.add(line);
			} else { // stage == flow
				para.append(glue + line);
				glue = " ";
			}
		}
		if (para.length() > 0) {
			formatted.add(para.toString());
		}
		return formatted;
	}

	public Stage stageLine(String line_, int width_, Stage stage_) {
		Stage stage = stage_;
		String line = line_;
		int width = width_;
		if (stage == Stage.COPY) {
			if (line.length() > 0.5 * width) {
				stage = Stage.FLOW;
			}
		}
		if (stage == Stage.FLOW) {
			if (line.length() == 0) {
				stage = Stage.COPY;
			}
		}
		return stage;
	}

	public ArrayList<String> flow(StringBuilder sb_, int width_) {
		int width = width_;
		ArrayList<String> para = new ArrayList<>();
		String paraStr = sb_.toString();
		String[] words = paraStr.split(" ");
		StringBuilder line = new StringBuilder();
		String glue = "";
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			if (line.length() + word.length() > width) {
				para.add(line.toString());
				line = new StringBuilder();
				glue = "";
			}
			line.append(glue);
			line.append(word);
			glue = " ";
		}
		if (line.length() > 0) {
			para.add(line.toString());
		}
		return para;
	}

	/**
	 * Sets the current line to the specified line.
	 * 
	 * @param line_
	 *          the specified line
	 */
	public void setCurline(int line_) {
		this.curline = line_;
	}

}
