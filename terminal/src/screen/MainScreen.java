/**
 * 
 */
package screen;

import util.ExperimentException;
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

	Screen screen;
	Terminal terminal;
	ScreenWriter screenWriter;

	TerminalPosition prompt;
	TerminalPosition promptStr;

	TerminalPosition doc;

	private int promptLine;
	private String promptChar;

	public MainScreen() throws ExperimentException {
		buildScreen();
	}

	public void stopScreen() {
		screen.stopScreen();
	}

	private void buildScreen() throws ExperimentException {
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

		/* set hangman doc area */

		screen.clear(); // is with default back/foreground colors
		screen.refresh();
		screenWriter.setForegroundColor(Terminal.Color.BLUE);
		screenWriter.setBackgroundColor(Terminal.Color.WHITE);

		wipeScreen();
		drawHorDashLine(promptLine - 1);
		drawPrompt();

		drawScreenSize();

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
