/**
 * 
 */
package screen;

import hangman.HangmanGame;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Shows the figure of hangman while guessing characters of the word.
 * 
 * @author gbonnema
 * 
 */
public class FigurePanel extends AbstractPanel implements Observer {

	private ArrayList<String> _figure;

	/**
	 * @param mainScreen
	 */
	public FigurePanel(TextDraw mainScreen, String title) {
		super(mainScreen, title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see screen.AbstractPanel#refresh()
	 */
	@Override
	public void refresh() {
		drawBorder();
		int x = getPadding();
		int y = getPadding();
		for (String line : _figure) {
			// int maxWidth = Math.min(getWidth() - 2 * getPadding(), line.length());
			// line = line.substring(0, maxWidth - 1);
			drawString(x, y, line);
			y++;
		}
		refreshScreen();
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
			_figure = game.getFigure();
			refresh();
		}

	}

}
