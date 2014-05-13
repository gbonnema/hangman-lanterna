/**
 * 
 */
package screen;

import hangman.HangmanDoc;

import java.util.List;

import util.TextView;

import com.googlecode.lanterna.terminal.TerminalPosition;

/**
 * The panel containing the doc for hangman.
 * 
 * @author gbonnema
 */
public class DocPanel extends AbstractPanel {

	private TextView					view;
	private TerminalPosition	origin;
	private String						_docText;

	/**
	 * Constructor. Calls AbstractPanel constructor.
	 * 
	 * @param mainScreen
	 *          the screen that organises the panels in one screen.
	 * @param topleftCorner
	 *          the topleft corner of the panel.
	 * @param size
	 *          the width and height of the panel.
	 */
	public DocPanel(TextDraw mainScreen) {
		super(mainScreen);
		origin = new TerminalPosition(0, 0);
		/* get the text */
		HangmanDoc docText = new HangmanDoc();
		_docText = docText.getLongDescription();
		/* show text */
		recreateLongDocPanel();
	}

	/**
	 * creates the long description panel.
	 */
	void recreateLongDocPanel() {

		/* set hangman doc area */
		TerminalPosition bottomRight =
				new TerminalPosition(getWidth() - 1, getHeight() - 1);
		drawBox(origin, bottomRight);

		/* Prepare and format the text */
		view = new TextView();
		view.formatPage(_docText, getWidth() - 2 * getPadding(), getHeight() - 2);
		/* Show the docText in pages */
		List<String> lines = view.nextPage();
		writeLinesInBox(lines);
	}

	private void writeLinesInBox(final List<String> lines_) {
		int top = 0;
		int left = getPadding();
		int bottom = getHeight();
		int pageWidth = getWidth() - 2 * getPadding();

		/* Write the lines of text on a cleaned line */
		int nextLine = top + 1;
		for (String line : lines_) {
			drawHorLine(left, nextLine, pageWidth, " ");
			drawString(left, nextLine, line);
			nextLine++;
		}
		/* Clean any following lines from the previous write */
		while (nextLine < bottom - 1) {
			drawHorLine(left, nextLine, pageWidth, " ");
			nextLine++;
		}

	}

	/**
	 * Show the previous page
	 */
	public void pageUp() {
		view.pageUp();
		List<String> lines = view.nextPage();
		writeLinesInBox(lines);
	}

	/**
	 * Show the next page
	 */
	public void pageDown() {
		List<String> lines = view.nextPage();
		writeLinesInBox(lines);
	}

	public void refreshPage() {
		List<String> lines = view.samePage();
		writeLinesInBox(lines);
	}

	@Override
	public void refresh() {
		recreateLongDocPanel();
	}

}