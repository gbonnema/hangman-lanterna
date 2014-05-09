/**
 * 
 */
package terminal;

import java.nio.charset.Charset;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalSize;

/**
 * The main program for terminal.
 * @author gbonnema
 *
 */
public final class Experiment {

	/**
	 * Private constructor to prevent instantiation.
	 */
	private Experiment() {
	}
	
	private static void printString(Terminal terminal, String message) {
		for(int i = 0; i < message.length(); i++) {
			terminal.putCharacter(message.charAt(i));
		}
	}
	
	private static char readChar(Terminal terminal) {
		char ch = ' ';
		Key key;
		while ((key = terminal.readInput()) == null) {
		}
		if (key.getKind() == Key.Kind.NormalKey) {
			ch = key.getCharacter();
		}
		return ch;
	}
	
	private static void sleep(int sec) {
		try {
			Thread.sleep(sec * 1000);
		} catch (InterruptedException e) {
		}
		return;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		char ch = ' ';
		String message = "You typed: ";
		
		Terminal terminal = TerminalFacade.createTerminal(Charset.forName("UTF-8"));
		terminal.enterPrivateMode();
		/* Do something useful with the screen */
		terminal.clearScreen();
		TerminalSize tsize = terminal.getTerminalSize();
		terminal.moveCursor(tsize.getColumns() -1, tsize.getRows() - 1);
		ch = readChar(terminal);
		terminal.moveCursor(10, 10);
		printString(terminal, message);
		terminal.putCharacter(ch);
		sleep(2);
		terminal.moveCursor(10, 10);
		printString(terminal, "Druk een toets om te eindigen: ");
		ch = readChar(terminal);
		terminal.exitPrivateMode();
	}

}
