/**
 * 
 */
package screen;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.screen.ScreenWriter;
import com.googlecode.lanterna.terminal.Terminal;

/**
 * @author gbonnema
 * 
 */
public class MainScreen {

	Screen screen;
	Terminal terminal;
	ScreenWriter screenWriter;

	int promptStrStart, promptLine, promptStart;
	String promptChar;

	public MainScreen() {
		screen = TerminalFacade.createScreen();
		terminal = screen.getTerminal();
		screenWriter = new ScreenWriter(screen);
		promptChar = "$ > ";
		promptStrStart = 3;
		promptLine = screen.getTerminalSize().getRows() - 4;
		screen.startScreen();
		buildScreen();
	}

	public void stopScreen() {
		screen.stopScreen();
	}

	private void drawPrompt() {
		screenWriter.drawString(promptStrStart, promptLine, promptChar,
				ScreenCharacterStyle.Bold);
	}

	private void buildScreen() {
		screen.clear();
		terminal.applyBackgroundColor(Terminal.Color.WHITE);
		terminal.applyForegroundColor(Terminal.Color.BLUE);
		terminal.clearScreen();
		screenWriter.setForegroundColor(Terminal.Color.BLUE);
		screenWriter.setBackgroundColor(Terminal.Color.WHITE);
		drawHorLine(promptLine - 1);
		drawPrompt();
		screen.refresh();
	}

	private void drawHorLine(int line) {
		String horLine = createHorLine();
		screenWriter.drawString(0, line, horLine);
	}

	private String createHorLine() {
		int width = screen.getTerminalSize().getColumns();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < width; i++) {
			sb.append("-");
		}
		return sb.toString();
	}
}
