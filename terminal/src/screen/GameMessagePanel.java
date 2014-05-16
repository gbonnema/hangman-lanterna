/**
 * 
 */
package screen;

import hangman.HangmanGame;

import java.util.Observable;
import java.util.Observer;

import com.googlecode.lanterna.screen.ScreenCharacterStyle;

/**
 * @author gbonnema
 * 
 */
public class GameMessagePanel extends AbstractPanel implements Observer {

	private String _gameMessage;

	/**
	 * @param mainScreen
	 */
	public GameMessagePanel(TextDraw mainScreen) {
		super(mainScreen);
		_gameMessage = "";
	}

	public void refreshEntry() {
		int x = getPadding();
		int y = getPadding();
		ScreenCharacterStyle style = ScreenCharacterStyle.Bold;
		String msg = fillTrailingSpace(_gameMessage);
		drawString(x, y, msg, style);
	}

	private String fillTrailingSpace(String msg) {
		StringBuilder result = new StringBuilder(msg);
		int len = getWidth() - getPadding() - result.length();
		if (len > 0) {
			for (int i = 0; i < len; i++) {
				result.append(" ");
			}
		}
		return result.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see screen.AbstractPanel#refresh()
	 */
	@Override
	public void refresh() {
		refreshEntry();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof HangmanGame) {
			HangmanGame game = (HangmanGame) o;
			_gameMessage = game.getGameMessage();
			refreshEntry();
			refreshScreen();
		}
	}

}
