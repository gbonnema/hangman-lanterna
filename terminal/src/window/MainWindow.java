/**
 * 
 */
package window;

import util.Utils;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.gui.GUIScreen;

/**
 * @author gbonnema
 * 
 */
public class MainWindow {

	private GUIScreen _gui;

	private HangmanWindow _hangmanWindow;

	/**
	 * 
	 */
	public MainWindow() {
		_gui = TerminalFacade.createGUIScreen();
		Utils.checkInternal(_gui != null,
				"Creating GUI screen failed .... horribly.");
		_gui.getScreen().startScreen();
		_hangmanWindow = new HangmanWindow("Window for playing hangman");
		_gui.showWindow(_hangmanWindow);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// Squelch
		}
		_gui.getScreen().stopScreen();
	}

	public void run() {

	}

}
