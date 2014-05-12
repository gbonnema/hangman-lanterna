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
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.input.Key.Kind;
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

	private final int					padding				= 2;

	private boolean						keepRunning;

	private Screen						screen;
	private Terminal					terminal;
	private ScreenWriter			screenWriter;
	private int								screenWidth;

	private TerminalPosition	prompt;
	private TerminalPosition	promptStr;

	private TerminalPosition	docBoxTopLeft, docBoxBottomRight;
	private TerminalPosition	docContentsTopLeft, docContentsBottomRight;
	private int								docPadding		= 2;
	private int								docPageSize;
	private int								docPageWidth;

	private int								nrPromptLines	= 2;
	private int								promptLine;
	private int								centerline;
	private String						promptChar;

	private TextView					view;

	/**
	 * The Contructor.
	 * 
	 * @throws ExperimentException
	 *           when something is wrong.
	 */
	public MainScreen() throws ExperimentException {
		/* Only once */
		initializeScreen();
		/* every resize */
		buildScreen();

		screen.refresh();
	}

	/**
	 * Initialize screen variables, only needed at startup. Do not call after
	 * initial creation.
	 */
	private void initializeScreen() {
		screen = TerminalFacade.createScreen();
		terminal = screen.getTerminal();
		screenWriter = new ScreenWriter(screen);
		screenWriter.setForegroundColor(Terminal.Color.BLUE);
		screenWriter.setBackgroundColor(Terminal.Color.WHITE);

		screen.startScreen();
	}

	/**
	 * Runs the application until the user presses Escape
	 * 
	 * @throws ExperimentException
	 *           for failed readInput.
	 */
	public void run() throws ExperimentException {
		keepRunning = true;
		while (keepRunning) {
			Key key = screen.readInput();
			if (key != null) {
				handleInput(key);
			}

			if (screen.resizePending()) {
				screen.refresh();
				buildScreen();
			}
		}

	}

	private void handleInput(Key key) throws ExperimentException {
		Kind kind = key.getKind();
		if (kind == Key.Kind.Escape) {
			keepRunning = false;
		} else if (kind == Key.Kind.PageDown) {
			writeTextInBox(view, docBoxTopLeft, docBoxBottomRight);
			screen.refresh();
		} else if (kind == Key.Kind.PageUp) {
			view.pageUp();
			writeTextInBox(view, docBoxTopLeft, docBoxBottomRight);
			screen.refresh();
		}
	}

	public void stopScreen() {
		screen.stopScreen();
	}

	private void buildScreen() throws ExperimentException {

		screenWidth = screen.getTerminalSize().getColumns();
		centerline = screenWidth / 2;

		initializePrompt();

		wipeScreen();
		drawHorDashLine(promptLine - 1);
		drawPrompt();

		drawScreenSize();
		createLongDocPanel();

		screen.setCursorPosition(prompt);
		screen.refresh();
	}

	/**
	 * 
	 */
	private void initializePrompt() {
		/* create the prompt area */
		promptChar = "$ > ";
		promptLine = screen.getTerminalSize().getRows() - 1;
		prompt = new TerminalPosition(promptChar.length() + 1, promptLine);
		promptStr = new TerminalPosition(0, promptLine);
		promptLine = screen.getTerminalSize().getRows() - 1;
	}

	/**
	 * @throws ExperimentException
	 */
	private void createLongDocPanel() throws ExperimentException {

		/* Calculate start and end of the doc box */
		calcDocBox();

		/* set hangman doc area */
		drawBox(docBoxTopLeft, docBoxBottomRight);

		/* get the text */
		HangmanDoc docText = new HangmanDoc();
		String text = docText.getLongDescription();
		view = new TextView();
		view.formatPage(text, docPageSize, docPageWidth);
		/* Write de docs */
		writeTextInBox(view, docBoxTopLeft, docBoxBottomRight);
	}

	/*
	 * calculates the left top corner and the right top corner relative to the
	 * screen size. Each resize this is done again so the box will change.
	 */
	private void calcDocBox() {
		int x;
		int y;
		// calculate top lines
		x = centerline + padding;
		y = 2;
		docBoxTopLeft = new TerminalPosition(x, y);
		x = x + docPadding;
		y = y + 1;
		docContentsTopLeft = new TerminalPosition(x, y);

		// calculate bottom lines
		x = screenWidth - padding;
		y = (int) (screen.getTerminalSize().getRows() * 0.6);

		docBoxBottomRight = new TerminalPosition(x, y);
		x = x - docPadding;
		y = y - 1;
		docContentsBottomRight = new TerminalPosition(x, y);
		docPageSize =
				docContentsBottomRight.getRow() - docContentsTopLeft.getRow() + 1;
		docPageWidth =
				docContentsBottomRight.getColumn() - docContentsTopLeft.getColumn() + 1;
	}

	/*
	 * screen.clear() uses default colors which is why we need to wipe the screen
	 * by writing empty lines.
	 */
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

	private void drawHorLine(int line, String ch, int start, int width)
			throws ExperimentException {
		String horLine = createHorLine(ch, width);
		screenWriter.drawString(start, line, horLine);
	}

	private void drawBox(final TerminalPosition from, final TerminalPosition to)
			throws ExperimentException {

		String topStr, bottomStr, midStr;
		int left = from.getColumn();
		int right = to.getColumn();
		int top = from.getRow();
		int bottom = to.getRow();
		int width = right - left + 1;
		Utils.check(width >= 5, "minimum width box = 5. specified: " + width + "\n"
				+ "\n" + "from = " + from + ", to = " + to + "\n");

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

	private void writeTextInBox(final TextView view, final TerminalPosition from,
			final TerminalPosition to) throws ExperimentException {
		int top = from.getRow();
		int left = from.getColumn() + docPadding;
		int bottom = to.getRow();

		List<String> lines = view.nextPage();
		int nextLine = top + 1;
		for (String line : lines) {
			drawHorLine(nextLine, " ", left, docPageWidth);
			screenWriter.drawString(left, nextLine, line);
			nextLine++;
		}
		/* Overwrite any following lines from the previous write */
		while (nextLine < bottom) {
			drawHorLine(nextLine, " ", left, docPageWidth);
			nextLine++;
		}

	}

	/**
	 * Builds a line start with one startchar en ending with an endchar, in the
	 * middle a midchar. Returns the result.
	 * 
	 * @param startchar
	 *          The first character of the line.
	 * @param endchar
	 *          The last character of the line.
	 * @param midchar
	 *          The characters in the middle
	 * @param len
	 *          The length of the line.
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
		int length = screen.getTerminalSize().getColumns();
		return createHorLine(ch, length);
	}

	private String createHorLine(String ch, int length)
			throws ExperimentException {
		Utils.check(ch.length() == 1, "String should contain 1 character: " + ch);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(ch);
		}
		return sb.toString();
	}

}
