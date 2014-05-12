/**
 * 
 */
package screen;

import hangman.HangmanDoc;

import java.util.List;

import util.ExperimentException;
import util.TextView;
import util.Utils;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.screen.ScreenWriter;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalPosition;

/**
 * @author gbonnema
 * 
 */
public class MainScreen {

	private Screen screen;
	private Terminal terminal;
	private ScreenWriter screenWriter;

	private TerminalPosition prompt;
	private TerminalPosition promptStr;

	private TerminalPosition docTopLeft, docBottomRight;
	private TerminalPosition docContentsTopLeft, docContentsBottomRight;
	private int docPadding = 2;
	private int docPageSize;
	private int docPageWidth;

	private int nrPromptLines = 2;
	private int promptLine;
	private int centerline;
	private String promptChar;

	private TextView view;

	public MainScreen() throws ExperimentException {
		buildScreen();
		HangmanDoc docText = new HangmanDoc();
		String text = docText.getLongDescription();
		view = new TextView();
		view.formatPage(text, docPageSize, docPageWidth);
		writeTextInBox(view, docTopLeft, docBottomRight);
		screen.refresh();
	}

	public void stopScreen() {
		screen.stopScreen();
	}

	private void buildScreen() throws ExperimentException {
		int x, y, padding;
		screen = TerminalFacade.createScreen();
		terminal = screen.getTerminal();
		screenWriter = new ScreenWriter(screen);
		promptChar = "$ > ";

		/* create the prompt area */
		promptLine = screen.getTerminalSize().getRows() - 1;
		prompt = new TerminalPosition(promptChar.length() + 1, promptLine);
		promptStr = new TerminalPosition(0, promptLine);
		promptLine = screen.getTerminalSize().getRows() - 1;
		screen.startScreen();

		int screenWidth = screen.getTerminalSize().getColumns();
		centerline = screenWidth / 2;

		padding = 2; /* number of characters padding */
		// width = screenWidth - centerline - 2 * padding;
		x = centerline + padding;
		y = 2;
		docTopLeft = new TerminalPosition(x, y);
		x = x + docPadding;
		y = y + 1;
		docContentsTopLeft = new TerminalPosition(x, y);

		x = screenWidth - padding;
		y = screen.getTerminalSize().getRows() - nrPromptLines - 1;
		docBottomRight = new TerminalPosition(x, y);
		x = x - docPadding;
		y = y - 1;
		docContentsBottomRight = new TerminalPosition(x, y);
		docPageSize =
				docContentsBottomRight.getRow() - docContentsTopLeft.getRow()
						+ 1;
		docPageWidth =
				docContentsBottomRight.getColumn()
						- docContentsTopLeft.getColumn() + 1;

		screen.clear(); // is with default back/foreground colors
		screenWriter.setForegroundColor(Terminal.Color.BLUE);
		screenWriter.setBackgroundColor(Terminal.Color.WHITE);

		wipeScreen();
		drawHorDashLine(promptLine - 1);
		drawPrompt();

		drawScreenSize();

		/* set hangman doc area */
		drawBox(docTopLeft, docBottomRight);

		screen.setCursorPosition(prompt);
		screen.refresh();
	}

	private void wipeScreen() throws ExperimentException {
		for (int i = 0; i < screen.getTerminalSize().getRows(); i++) {
			drawHorLine(i, " ");
		}
	}

	private void drawPrompt() {
		int start = promptStr.getColumn();
		screenWriter.drawString(start, promptLine, promptChar,
				ScreenCharacterStyle.Bold);
	}

	private void drawScreenSize() throws ExperimentException {
		int width = terminal.getTerminalSize().getColumns();
		int height = terminal.getTerminalSize().getRows();
		String sizeStr = width + " x " + height;
		int x = width - sizeStr.length() - 1;
		int y = promptLine;
		screenWriter.drawString(x, y, sizeStr);
	}

	private void drawHorDashLine(int line) throws ExperimentException {
		drawHorLine(line, "-");
	}

	private void drawHorLine(int line, String ch) throws ExperimentException {
		String horLine = createHorLine(ch);
		screenWriter.drawString(0, line, horLine);
	}

	private void
			drawBox(final TerminalPosition from, final TerminalPosition to)
					throws ExperimentException {

		String topStr, bottomStr, midStr;
		int left = from.getColumn();
		int right = to.getColumn();
		int top = from.getRow();
		int bottom = to.getRow();
		int width = right - left + 1;
		Utils.check(width >= 5, "minimum width box = 5. specified: " + width
				+ "\n" + "\n" + "from = " + from + ", to = " + to + "\n");

		topStr = fillLine('+', '+', '-', width);
		bottomStr = fillLine('+', '+', '-', width);
		midStr = fillLine('|', '|', ' ', width);
		/* Draw top and bottom first */
		screenWriter.drawString(left, top, topStr);
		screenWriter.drawString(left, bottom, bottomStr);
		/* Now draw the middle */
		int height = bottom - top + 1;
		int innerheight = height - 2;
		for (int i = 0; i < innerheight; i++) {
			int x = left;
			int y = top + 1 + i;
			screenWriter.drawString(x, y, midStr);
		}
	}

	private void writeTextInBox(final TextView view,
			final TerminalPosition from, final TerminalPosition to)
			throws ExperimentException {
		int padding = 2;
		int left = from.getColumn() + padding;
		int top = from.getRow();

		List<String> lines = view.nextPage();
		int nextLine = top + 1;
		for (String line : lines) {
			screenWriter.drawString(left, nextLine, line);
			nextLine++;
		}

	}

	/**
	 * Builds a line start with one startchar en ending with an endchar, in the
	 * middle a midchar. Returns the result.
	 * 
	 * @param startchar
	 *            The first character of the line.
	 * @param endchar
	 *            The last character of the line.
	 * @param midchar
	 *            The characters in the middle
	 * @param len
	 *            The length of the line.
	 * @return the resulting line as a String.
	 */
	private String fillLine(final char startchar, final char endchar,
			final char midchar, final int len) throws ExperimentException {
		Utils.check(len > 2, "wrong length for filling a line.");
		StringBuilder result = new StringBuilder();
		result.append(startchar);
		for (int i = 1; i < len; i++) {
			result.append(midchar);
		}
		result.append(endchar);
		return result.toString();
	}

	private String createHorLine(String ch) throws ExperimentException {
		Utils.check(ch.length() == 1, "String should contain 1 character: "
				+ ch);
		int width = screen.getTerminalSize().getColumns();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < width; i++) {
			sb.append(ch);
		}
		return sb.toString();
	}
}
