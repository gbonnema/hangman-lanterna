/**
 * 
 */
package screen;

import hangman.HangmanDoc;

import java.util.List;

import util.ExperimentException;
import util.TextView;

import com.googlecode.lanterna.terminal.TerminalPosition;

/**
 * @author gbonnema
 */
public class DocPanel {

	public final int					docPadding	= 2;

	public TextView						view;
	public TerminalPosition		docBoxTopLeft;
	public TerminalPosition		docBoxBottomRight;
	public TerminalPosition		docContentsTopLeft;
	public TerminalPosition		docContentsBottomRight;
	public int								docPageSize;
	public int								docPageWidth;

	private TerminalPosition	panelSize;

	/**
	 * Constructor.
	 */
	public DocPanel(TerminalPosition panelSize) {
		this.panelSize = panelSize;
	}

	/**
	 * @param mainScreen
	 *          TODO
	 * @throws ExperimentException
	 */
	void createLongDocPanel(MainScreen mainScreen) throws ExperimentException {

		/* Calculate start and end of the doc box */
		mainScreen.calcDocBox();

		/* set hangman doc area */
		mainScreen.drawBox(docBoxTopLeft, docBoxBottomRight);

		/* get the text */
		HangmanDoc docText = new HangmanDoc();
		String text = docText.getLongDescription();
		view = new TextView();
		view.formatPage(text, docPageSize, docPageWidth);
		/* Write the docs */
		mainScreen.writeTextInBox(view, docBoxTopLeft, docBoxBottomRight);
	}

	void calcDocBox(MainScreen mainScreen) {
		int x;
		int y;
		// calculate top lines
		x = mainScreen.centerline + mainScreen.padding;
		y = 2;
		docBoxTopLeft = new TerminalPosition(x, y);
		x = x + docPadding;
		y = y + 1;
		docContentsTopLeft = new TerminalPosition(x, y);

		// calculate bottom lines
		x = mainScreen.screenWidth - mainScreen.padding;
		y = (int) (mainScreen.screen.getTerminalSize().getRows() * 0.6);

		docBoxBottomRight = new TerminalPosition(x, y);
		x = x - docPadding;
		y = y - 1;
		docContentsBottomRight = new TerminalPosition(x, y);
		docPageSize =
				docContentsBottomRight.getRow() - docContentsTopLeft.getRow() + 1;
		docPageWidth =
				docContentsBottomRight.getColumn() - docContentsTopLeft.getColumn() + 1;
	}

	void writeTextInBox(MainScreen mainScreen, final TextView view,
			final TerminalPosition from, final TerminalPosition to)
			throws ExperimentException {
		int top = from.getRow();
		int left = from.getColumn() + docPadding;
		int bottom = to.getRow();

		List<String> lines = view.nextPage();
		int nextLine = top + 1;
		for (String line : lines) {
			mainScreen.drawHorLine(nextLine, " ", left, docPageWidth);
			mainScreen.screenWriter.drawString(left, nextLine, line);
			nextLine++;
		}
		/* Overwrite any following lines from the previous write */
		while (nextLine < bottom) {
			mainScreen.drawHorLine(nextLine, " ", left, docPageWidth);
			nextLine++;
		}

	}
}