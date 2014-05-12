/**
 * 
 */
package screen;

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
 * The main screen for this experiment: a hangman game on text screens using
 * Lanterna.
 * 
 * @author gbonnema
 * 
 */
public class MainScreen {

	final int									padding		= 2;

	private boolean						keepRunning;

	Screen										screen;
	private Terminal					terminal;
	ScreenWriter							screenWriter;
	int												screenWidth;

	private DocPanel					docPanel	= new DocPanel();

	/*
	 * Prompt fields
	 */
	private int								promptLine;
	int												centerline;
	private String						promptChar;
	private TerminalPosition	prompt;
	private TerminalPosition	promptStr;

	/**
	 * The Constructor.
	 * 
	 * @throws ExperimentException
	 *           when something is wrong.
	 */
	public MainScreen() throws ExperimentException {
		initializeScreen();
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

		screen.stopScreen();

	}

	private void handleInput(Key key) throws ExperimentException {
		Kind kind = key.getKind();
		if (kind == Key.Kind.Escape) {
			keepRunning = false;
		} else if (kind == Key.Kind.PageDown) {
			writeTextInBox(docPanel.view, docPanel.docBoxTopLeft,
					docPanel.docBoxBottomRight);
			screen.refresh();
		} else if (kind == Key.Kind.PageUp) {
			docPanel.view.pageUp();
			writeTextInBox(docPanel.view, docPanel.docBoxTopLeft,
					docPanel.docBoxBottomRight);
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
		docPanel.createLongDocPanel(this);

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

	/*
	 * calculates the left top corner and the right top corner relative to the
	 * screen size. Each resize this is done again so the box will change.
	 */
	void calcDocBox() {
		docPanel.calcDocBox(this);
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

	void drawHorLine(int line, String ch, int start, int width)
			throws ExperimentException {
		String horLine = createHorLine(ch, width);
		screenWriter.drawString(start, line, horLine);
	}

	void drawBox(final TerminalPosition from, final TerminalPosition to)
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

	void writeTextInBox(final TextView view, final TerminalPosition from,
			final TerminalPosition to) throws ExperimentException {
		docPanel.writeTextInBox(this, view, from, to);
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
