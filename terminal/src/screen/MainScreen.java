/**
 * 
 */
package screen;

import util.ExperimentException;
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
public class MainScreen implements TextDraw {

	final int									padding	= 2;

	private boolean						_keepRunning;

	Screen										_screen;
	private Terminal					terminal;
	ScreenWriter							_screenWriter;
	int												screenWidth;
	int												screenHeight;

	private DocPanel					docPanel;

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
		rebuildScreen();
		_screen.refresh();
	}

	/**
	 * Initialize screen variables, only needed at startup. Do not call after
	 * initial creation.
	 */
	private void initializeScreen() {
		_screen = TerminalFacade.createScreen();
		terminal = _screen.getTerminal();
		_screenWriter = new ScreenWriter(_screen);
		_screenWriter.setForegroundColor(Terminal.Color.BLUE);
		_screenWriter.setBackgroundColor(Terminal.Color.WHITE);

		_screen.startScreen();

	}

	/**
	 * Runs the application until the user presses Escape
	 * 
	 * @throws ExperimentException
	 *           for failed readInput.
	 */
	public void run() throws ExperimentException {
		_keepRunning = true;
		while (_keepRunning) {
			Key key = _screen.readInput();
			if (key != null) {
				handleInput(key);
			}
			if (!_keepRunning) {
				break;
			}

			if (_screen.resizePending()) {
				rebuildScreen();
				rebuildDocPanel();
				docPanel.refresh();
				_screen.refresh();
			}
		}

		_screen.stopScreen();

	}

	private void handleInput(Key key) throws ExperimentException {
		Kind kind = key.getKind();
		if (kind == Key.Kind.Escape) {
			_keepRunning = false;
		} else if (kind == Key.Kind.PageDown) {
			docPanel.pageDown();
			_screen.refresh();
		} else if (kind == Key.Kind.PageUp) {
			docPanel.pageUp();
			_screen.refresh();
		} else if (kind == Key.Kind.F4 && key.isAltPressed()) {
			_keepRunning = false;
		}
	}

	public void stopScreen() {
		_screen.stopScreen();
	}

	private void rebuildScreen() throws ExperimentException {

		initializeTerminalSize();
		initializePrompt();

		wipeScreen();
		drawHorDashLine(promptLine - 1);
		drawPrompt();

		drawScreenSize();

		_screen.setCursorPosition(prompt);
		_screen.refresh();

		docPanel = new DocPanel(this);
		rebuildDocPanel();
	}

	/**
	 * 
	 */
	private void initializeTerminalSize() {
		screenWidth = _screen.getTerminalSize().getColumns();
		screenHeight = _screen.getTerminalSize().getRows();
		centerline = screenWidth / 2;
	}

	/**
	 * Builds the docPanel and writes the doc to screen.
	 */
	private void rebuildDocPanel() {

		int x = centerline + padding;
		int y = padding;
		TerminalPosition topLeft = new TerminalPosition(x, y);

		int width = screenWidth - padding - x;
		int height = (int) ((screenHeight - padding - y) * 0.6);
		TerminalPosition panelSize = new TerminalPosition(width, height);

		docPanel.resetPanel(topLeft, panelSize);
		docPanel.refresh();
	}

	/**
	 * 
	 */
	private void initializePrompt() {
		/* create the prompt area */
		promptChar = "$ > ";
		promptLine = _screen.getTerminalSize().getRows() - 1;
		prompt = new TerminalPosition(promptChar.length() + 1, promptLine);
		promptStr = new TerminalPosition(0, promptLine);
		promptLine = _screen.getTerminalSize().getRows() - 1;
	}

	/*
	 * screen.clear() uses default colors which is why we need to wipe the screen
	 * by writing empty lines.
	 */
	private void wipeScreen() throws ExperimentException {
		for (int i = 0; i < _screen.getTerminalSize().getRows(); i++) {
			drawHorLine(i, " ");
		}
	}

	private void drawPrompt() {
		int start = promptStr.getColumn();
		_screenWriter.drawString(start, promptLine, promptChar,
				ScreenCharacterStyle.Bold);
	}

	private void drawScreenSize() throws ExperimentException {
		int width = terminal.getTerminalSize().getColumns();
		int height = terminal.getTerminalSize().getRows();
		String sizeStr = width + " x " + height;
		int x = width - sizeStr.length() - 1;
		int y = promptLine;
		_screenWriter.drawString(x, y, sizeStr);
	}

	@Override
	public void drawHorDashLine(int line) {
		drawHorLine(line, "-");
	}

	private void drawHorLine(int line, String ch) {
		String horLine = createHorLine(ch);
		_screenWriter.drawString(0, line, horLine);
	}

	@Override
	public void drawHorLine(int col, int line, int width, String ch)
			throws ExperimentException {
		String horLine = createHorLine(ch, width);
		_screenWriter.drawString(col, line, horLine);
	}

	@Override
	public void drawBox(final TerminalPosition from, final TerminalPosition to) {

		String topStr, bottomStr, midStr;
		int left = from.getColumn();
		int right = to.getColumn();
		int top = from.getRow();
		int bottom = to.getRow();
		int width = right - left + 1;
		Utils.checkArg(width >= 5, "minimum width box = 5. specified: " + width
				+ "\n" + "\n" + "from = " + from + ", to = " + to + "\n");

		topStr = fillLine('+', '+', '-', width);
		bottomStr = fillLine('+', '+', '-', width);
		midStr = fillLine('|', '|', ' ', width);
		/* Draw top and bottom first */
		_screenWriter.drawString(left, top, topStr);
		_screenWriter.drawString(left, bottom, bottomStr);
		/* Now draw the middle */
		int height = bottom - top + 1;
		int innerheight = height - 2;
		for (int i = 0; i < innerheight; i++) {
			int x = left;
			int y = top + 1 + i;
			_screenWriter.drawString(x, y, midStr);
		}
	}

	/**
	 * Builds a line start with one startchar en ending with an endchar, in the
	 * middle a midchar. Returns the result.
	 * 
	 * You can use this to create a box, or just a line
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
	@Override
	public String fillLine(final char startchar, final char endchar,
			final char midchar, final int len) {
		Utils.checkArg(len > 2, "wrong length for filling a line.");
		StringBuilder result = new StringBuilder();
		result.append(startchar);
		for (int i = 1; i < len; i++) {
			result.append(midchar);
		}
		result.append(endchar);
		return result.toString();
	}

	private String createHorLine(String ch) {
		int length = _screen.getTerminalSize().getColumns();
		return createHorLine(ch, length);
	}

	private String createHorLine(String ch, int length) {
		Utils.checkArg(ch.length() == 1, "String must have 1 character: " + ch);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(ch);
		}
		return sb.toString();
	}

	/**
	 * Return the absolute screen.
	 */
	@Override
	public ScreenWriter getAbsScreenWriter() {
		return _screenWriter;
	}

}
