/**
 * 
 */
package screen;

import hangman.HangmanGame;

import java.util.Observable;
import java.util.Observer;

import util.Utils;

/**
 * @author gbonnema
 * 
 */
public class GameSolutionPanel extends AbstractPanel implements Observer {

	private String	_solution;
	private String	_hexSolution;

	/**
	 * @param mainScreen
	 */
	public GameSolutionPanel(TextDraw mainScreen) {
		super(mainScreen);
	}

	private void setSolution(String solution) {
		_solution = solution;
		_hexSolution = Utils.convert2Hex(_solution);
	}

	public void refreshEntry() {
		refresh();
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
		String prepend = " ";
		String mid = "  ";
		String postpend = "";
		drawString(x, y, Utils.disperse(_solution, prepend, mid, postpend));
		y++;
		drawString(x, y, _hexSolution);
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
			setSolution(game.getSolution());
			refreshEntry();
			refreshScreen();
		}
	}
}
