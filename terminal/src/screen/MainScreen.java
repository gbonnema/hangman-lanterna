/**
 * 
 */
package screen;

import hangman.HangFig;
import hangman.HangmanGame;
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

	private final String			_version	= "0.2";

	private final int					_padding	= 2;

	private boolean						_keepRunning;

	private Screen						_screen;
	private Terminal					_terminal;
	private ScreenWriter			_screenWriter;
	private int								_screenWidth;
	private int								_screenHeight;

	private DocPanel					_docPanel;
	private WordProgressPanel	_wordProgressPanel;
	private FigurePanel				_figurePanel;

	private HangmanGame				_hangmanGame;

	/*
	 * Prompt fields
	 */
	private int								_promptLine;
	int												_centerline;
	private String						_promptChar;
	private TerminalPosition	_prompt;
	private TerminalPosition	_promptStr;

	/**
	 * The Constructor.
	 * 
	 * @throws ExperimentException
	 *           when something is wrong.
	 */
	public MainScreen() throws ExperimentException {

		_screen = TerminalFacade.createScreen();
		_screen.startScreen();

		initializeScreen();

		_docPanel = new DocPanel(this);
		_wordProgressPanel = new WordProgressPanel(this);
		_figurePanel = new FigurePanel(this);

		_hangmanGame = new HangmanGame();
		_hangmanGame.addObserver(_wordProgressPanel);
		_hangmanGame.addObserver(_figurePanel);

		_hangmanGame.createGame();

		rebuildScreen();

		_screen.refresh();
	}

	/**
	 * Initialize screen variables, only needed at startup. Do not call after
	 * initial creation.
	 */
	private void initializeScreen() {
		_terminal = _screen.getTerminal();
		_screenWriter = new ScreenWriter(_screen);
		_screenWriter.setForegroundColor(Terminal.Color.BLUE);
		_screenWriter.setBackgroundColor(Terminal.Color.WHITE);

	}

	public void stopScreen() {
		_screen.stopScreen();
	}

	/**
	 * Rebuild this screen from scratch. This often occurs as response to an event
	 * of some change, like a resize. It also the initial build.
	 * 
	 * @throws ExperimentException
	 */
	private void rebuildScreen() throws ExperimentException {

		initTerminalSize();
		initializePrompt();

		wipeScreen();
		drawHorDashLine(_promptLine - 1);
		drawPrompt();

		drawInfoLine();

		_screen.setCursorPosition(_prompt);

		rebuildDocPanel();
		rebuildFigurePanel();
		rebuildWordProgressPanel();

		_screen.refresh();
	}

	/**
	 * Initialize the terminal sizes.
	 */
	private void initTerminalSize() {
		_screenWidth = _screen.getTerminalSize().getColumns();
		_screenHeight = _screen.getTerminalSize().getRows();
		_centerline = _screenWidth / 2;
	}

	/**
	 * Builds the docPanel and writes the doc to screen.
	 */
	private void rebuildDocPanel() {

		int left = _centerline + _padding;
		int y = _padding;
		TerminalPosition topLeft = new TerminalPosition(left, y);

		int width = _screenWidth - _padding - left;
		width = width > 80 ? 80 : width;
		int height = (int) ((_screenHeight - _padding - y) * 0.6);
		TerminalPosition panelSize = new TerminalPosition(width, height);

		_docPanel.resetPanel(topLeft, panelSize);
		_docPanel.refresh();
	}

	/**
	 * Builds the gamePanel and writes the game information to the screen.
	 */
	private void rebuildFigurePanel() {
		int left = _padding;
		int y = _padding;
		TerminalPosition topLeft = new TerminalPosition(left, y);

		int width = _centerline - _padding - left;
		int height = HangFig.HANGMAN_FIG_HEIGHT;
		TerminalPosition panelSize = new TerminalPosition(width, height);

		_figurePanel.resetPanel(topLeft, panelSize);
		_figurePanel.refresh();
	}

	/**
	 * Builds the gamePanel and writes the game information to the screen.
	 */
	private void rebuildWordProgressPanel() {
		int left = _padding;
		int y = _figurePanel.getTop() + _figurePanel.getHeight();
		TerminalPosition topLeft = new TerminalPosition(left, y);

		int width = _centerline - _padding - left;
		int height = (int) ((_screenHeight - _padding - y) * 0.6);
		TerminalPosition panelSize = new TerminalPosition(width, height);

		_wordProgressPanel.resetPanel(topLeft, panelSize);
		_wordProgressPanel.refresh();
	}

	/**
	 * 
	 */
	private void initializePrompt() {
		/* create the prompt area */
		_promptChar = "$ > ";
		_promptLine = _screen.getTerminalSize().getRows() - 1;
		_prompt = new TerminalPosition(_promptChar.length() + 1, _promptLine);
		_promptStr = new TerminalPosition(0, _promptLine);
		_promptLine = _screen.getTerminalSize().getRows() - 1;
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
		int start = _promptStr.getColumn();
		_screenWriter.drawString(start, _promptLine, _promptChar,
				ScreenCharacterStyle.Bold);
	}

	/**
	 * 
	 * @throws ExperimentException
	 */
	private void drawInfoLine() {
		int width = _terminal.getTerminalSize().getColumns();
		int height = _terminal.getTerminalSize().getRows();

		StringBuilder sb = new StringBuilder();

		sb.append("(version ").append(_version).append(")");
		sb.append(" - ");
		sb.append(width + " x " + height);

		int x = width - sb.length() - 1;
		int y = _promptLine;
		_screenWriter.drawString(x, y, sb.toString());
	}

	/**
	 * Runs the application until the user presses Escape
	 * 
	 * @throws ExperimentException
	 *           for failed readInput.
	 */
	public void run() throws ExperimentException {

		_keepRunning = true;
		_hangmanGame.createGame();
		while (_keepRunning) {
			Key key = _screen.readInput();
			if (key != null) {
				handleInput(key);
			}
			if (!_keepRunning) {
				break;
			}
			if (_screen.resizePending()) {
				_screen.refresh();
				rebuildScreen();
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
			_docPanel.pageDown();
			_screen.refresh();
		} else if (kind == Key.Kind.PageUp) {
			_docPanel.pageUp();
			_screen.refresh();
		} else if (kind == Key.Kind.F4 && key.isAltPressed()) {
			_keepRunning = false;
		} else if (kind == Key.Kind.F2) {
			// _wordProgressPanel.showWord();
			// _screen.refresh();
		} else {
			handleGameInput(key);
			_screen.refresh();
		}
	}

	private void handleGameInput(Key key) {
		Kind kind = key.getKind();
		if (kind == Key.Kind.NormalKey) {
			char ch = key.getCharacter();
			String charStr = Character.toString(ch);
			_hangmanGame.guess(charStr);
		}
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

	@Override
	public void refreshScreen() {
		_screen.refresh();
	}

}
