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

	/**
	 * 
	 */
	public MainWindow() {
		_gui = TerminalFacade.createGUIScreen();
		Utils.checkInternal(_gui != null,
				"Creating GUI screen failed .... horribly.");
		_gui.getScreen().startScreen();
		_gui.setTitle("GUI Hangman == not implemented yet ==");
		_gui.getScreen().stopScreen();
		_gui.invalidate();
		_gui.getScreen().refresh();
	}

	public void run() {

	}

}
