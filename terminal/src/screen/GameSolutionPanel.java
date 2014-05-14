/**
 * 
 */
package screen;

import java.util.Observable;
import java.util.Observer;

/**
 * @author gbonnema
 * 
 */
public class GameSolutionPanel extends AbstractPanel implements Observer {

	/**
	 * @param mainScreen
	 */
	public GameSolutionPanel(TextDraw mainScreen) {
		super(mainScreen);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see screen.AbstractPanel#refresh()
	 */
	@Override
	public void refresh() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {

	}
}
