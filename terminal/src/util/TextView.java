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

	private String text;
	private ArrayList<String> formattedLines;
	private int curline;
	private int pageSize;
	private int nrLines;

	public TextView() {
		curline = -1;
		formattedLines = new ArrayList<>();
	}

	public void formatPage(String text_, int pageWidth_, int pageSize_) {
		text = text_;
		pageSize = pageSize_;
		formattedLines = formatLines(text, pageWidth_);
		curline = 0;
	}

	/**
	 * Retrieve a page of text starting at registered current line.
	 * 
	 * @return List<String> containing the requested lines. The list is a copy.
	 */
	public List<String> nextPage() {
		int textLength = formattedLines.size();
		if (pageSize < textLength) {
			if (curline > textLength - pageSize) {
				curline = textLength - pageSize;
			}
		} else {
			curline = 0;
		}
		if (curline < 0) {
			List<String> list = new ArrayList<>();
			list.add("Negative current line?");
			return list;
		}
		int maxlen = Math.min(pageSize, formattedLines.size() - curline);
		maxlen = Math.max(maxlen, 0);
		if (maxlen == 0) {
			List<String> list = new ArrayList<>();
			list.add("No more lines.");
			return list;
		}
		/* create a sublist */
		List<String> lines = formattedLines.subList(curline, curline + maxlen);
		/* The sublist is an active view, not a copy, so copy it */
		ArrayList<String> result = new ArrayList<>(lines);
		curline = curline + maxlen;
		return result;
	}

	public List<String> samePage() {
		curline = curline <= pageSize ? 0 : curline - pageSize;
		return nextPage();
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
		/* Remark: \n at EOF disappears after split */
		String[] lines = text_.split("\n");
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
		/*
		 * Check for long lines. If so, make it flow.
		 */
		if (stage == Stage.COPY) {
			if (line.length() > 0.5 * width) {
				stage = Stage.FLOW;
			}
		}
		/*
		 * End a paragraph with an empty line. EOF works too.
		 */
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
			if (line.length() + word.length() + glue.length() > width) {
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
		curline = line_;
	}

	public void pageUp() {
		/*
		 * Remark that the curline is pointing to the first line of the next page.
		 * To get the first line of the previous page we have to return curline to
		 * -2 * pageSize (if curline is large enough). if not, curline is set to
		 * zero.
		 */
		if (curline >= 2 * pageSize) {
			curline -= 2 * pageSize;
		} else {
			curline = 0;
		}
	}

}
