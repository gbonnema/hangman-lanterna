package bugs;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.gui.GUIScreen;

/**
 * 
 */

/**
 * @author gbonnema
 * 
 */
public class Main {

	private static GUIScreen _gui;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		_gui = TerminalFacade.createGUIScreen();
		if (_gui == null) {
			throw new InternalError("Creating GUIScreen failed ... horribly.");
		}
		_gui.getScreen().startScreen();
		BugWindow window = new BugWindow("Bug window");
		_gui.showWindow(window);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// squelch
		}
		_gui.getScreen().stopScreen();
	}

}
