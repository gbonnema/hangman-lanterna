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

	private final int					docPadding	= 2;

	private TextDraw					_mainScreen;

	private TerminalPosition	panelSize;

	private TextView					view;
	private TerminalPosition	docBoxTopLeft;
	private TerminalPosition	docBoxBottomRight;
	private TerminalPosition	docContentsTopLeft;
	private TerminalPosition	docContentsBottomRight;
	private int								docPageSize;
	private int								docPageWidth;

	/**
	 * Constructor.
	 */
	public DocPanel(TextDraw mainScreen_, int panelId_)
			throws ExperimentException {
		_mainScreen = mainScreen_;
		createLongDocPanel();
	}

	/**
	 * @param _mainScreen
	 *          TODO
	 * @throws ExperimentException
	 */
	void createLongDocPanel() throws ExperimentException {

		/* Calculate start and end of the doc box */
		calcDocBox();

		/* set hangman doc area */
		_mainScreen.drawBox(docBoxTopLeft, docBoxBottomRight);

		/* get the text */
		HangmanDoc docText = new HangmanDoc();
		String text = docText.getLongDescription();
		view = new TextView();
		view.formatPage(text, docPageSize, docPageWidth);
		/* Write the docs */
		writeTextInBox(docBoxTopLeft, docBoxBottomRight);
	}

	void calcDocBox() {
		int x;
		int y;
		// calculate top lines
		x = _mainScreen.centerline + _mainScreen.padding;
		y = 2;
		docBoxTopLeft = new TerminalPosition(x, y);
		x = x + docPadding;
		y = y + 1;
		docContentsTopLeft = new TerminalPosition(x, y);

		// calculate bottom lines
		x = _mainScreen.screenWidth - _mainScreen.padding;
		y = (int) (_mainScreen.screen.getTerminalSize().getRows() * 0.6);

		docBoxBottomRight = new TerminalPosition(x, y);
		x = x - docPadding;
		y = y - 1;
		docContentsBottomRight = new TerminalPosition(x, y);
		docPageSize =
				docContentsBottomRight.getRow() - docContentsTopLeft.getRow() + 1;
		docPageWidth =
				docContentsBottomRight.getColumn() - docContentsTopLeft.getColumn() + 1;
	}

	private void writeTextInBox(final TerminalPosition from,
			final TerminalPosition to) throws ExperimentException {
		int top = from.getRow();
		int left = from.getColumn() + docPadding;
		int bottom = to.getRow();

		List<String> lines = view.nextPage();
		int nextLine = top + 1;
		for (String line : lines) {
			_mainScreen.drawHorLine(left, nextLine, docPageWidth, " ");
			_mainScreen.drawString(left, nextLine, line);
			nextLine++;
		}
		/* Overwrite any following lines from the previous write */
		while (nextLine < bottom) {
			_mainScreen.drawHorLine(left, nextLine, docPageWidth, " ");
			nextLine++;
		}

	}

	public void refresh() {
		writeTextInBox(docBoxTopLeft, docBoxBottomRight);
	}
}